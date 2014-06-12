package dvla.common.microservice

import spray.routing._
import spray.can.server.ServerSettings
import akka.actor._
import spray.http.StatusCodes._
import scala.concurrent.duration.Duration
import spray.util.LoggingContext
import spray.http.HttpRequest
import dvla.common.microservice.SingleAccessLogger.AccessLogRequest
import spray.http.HttpResponse
import spray.http.Timedout

trait AccessLogger {
  def logAccess(request:HttpRequest, response:HttpResponse, time:Long):Unit
  def accessAlreadyLogged(request:HttpRequest, response:HttpResponse, time:Long):Unit
}

object SingleAccessLogger {
  case class AccessLogRequest(request:HttpRequest, response:HttpResponse, time:Long)
}

class SingleAccessLogger(accessLogger: AccessLogger) extends Actor {
  import SingleAccessLogger._

  def receive = handleAccessLogRequest(Map().withDefaultValue(0))

  def handleAccessLogRequest(inProgressRequests: Map[HttpRequest, Int]): Receive = {
    case request:HttpRequest => {
      context.become(handleAccessLogRequest(inProgressRequests.updated(request, inProgressRequests(request)+1)))
    }
    case AccessLogRequest(request, response, time) => {
      inProgressRequests(request) match {
        case 0 => accessLogger.accessAlreadyLogged(request, response, time)
        case 1 => {
          accessLogger.logAccess(request, response, time)
          context.become(handleAccessLogRequest(inProgressRequests - request))
        }
        case _ => {
          accessLogger.logAccess(request, response, time)
          context.become(handleAccessLogRequest(inProgressRequests.updated(request, inProgressRequests(request)-1)))
        }
      }
    }
  }
}

trait LogAccessRouting extends HttpServiceBase {
  var singleAccessLoggerRef:ActorRef
  val requestTimeout:Duration


  def accessLogTimeout: Directive0 = {
    mapRequestContext { ctx =>
      ctx.withHttpResponseMapped { response =>
        singleAccessLoggerRef ! AccessLogRequest(ctx.request, response, requestTimeout.toMillis)
        response
      }
    }
  }


  override def timeoutRoute: Route = {
    accessLogTimeout {
      complete(InternalServerError, "The server was not able to produce a timely response to your request.")
    }
  }

  def accessLog()(implicit eh: ExceptionHandler,rs: RoutingSettings,log: LoggingContext): Directive0 = {
    mapInnerRoute { route => ctx =>
      singleAccessLoggerRef ! ctx.request
      val timeStamp = System.currentTimeMillis

      val ctxWithResponseLogged = ctx.withHttpResponseMapped { response =>
        singleAccessLoggerRef ! AccessLogRequest(ctx.request, response, System.currentTimeMillis - timeStamp)
        response
      }
      route(ctxWithResponseLogged)
    }
  }

  override def runRoute(route: Route)(implicit eh: ExceptionHandler, rh: RejectionHandler, ac: ActorContext,
                                      rs: RoutingSettings, log: LoggingContext): Actor.Receive = {
    val accessLogRoute:Route = accessLog()(eh,rs,log) {
      handleExceptions(eh) {
        route
      }
    }

    {
      case Timedout(request: HttpRequest) ⇒ super.runRoute(timeoutRoute)(eh, rh, ac, rs, log)(request)
      case other => super.runRoute(accessLogRoute)(eh, rh, ac, rs, log)(other)
    }

  }
}

trait LogAccessRoutingActor extends HttpServiceActor with LogAccessRouting {

  val accessLogger:AccessLogger
  val requestTimeout:Duration = ServerSettings(context.system).requestTimeout

  var singleAccessLoggerRef:ActorRef = _

  override def preStart() {
    singleAccessLoggerRef = context.system.actorOf(Props(new SingleAccessLogger(accessLogger)), "single-access-logger")
    super.preStart
  }



}