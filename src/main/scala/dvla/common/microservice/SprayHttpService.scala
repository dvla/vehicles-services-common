package dvla.common.microservice

import akka.actor._
import akka.pattern.ask
import spray.can.server.Stats
import scala.concurrent.duration._
import spray.routing._
import spray.util._
import spray.can.Http
import spray.httpx.marshalling.Marshaller
import spray.http._
import spray.routing.RequestContext
import spray.can.server.Stats

/**
 * SprayHttpService trait is designed to be mixed in with service implementations. It hides away the boilerplate
 * code used to turn a unit-testable HttpService into an actual runnable service and provides utility functionality
 */
trait SprayHttpService extends RequestResponseLogging with Actor with ActorLogging with ActorRequestLogger {
  self: HttpService =>

  // we use the enclosing ActorContext's or ActorSystem's dispatcher for our Futures and Scheduler
  private implicit def executionContext = actorRefFactory.dispatcher

  override def actorRefFactory = context

  val route: RequestContext => Unit
//  implicit val eh: ExceptionHandler, rh: RejectionHandler, ac: ActorContext,
//  rs: RoutingSettings, log: LoggingContext

  def receive = doRunRoute

  private def doRunRoute()(implicit eh: ExceptionHandler, rh: RejectionHandler, ac: ActorContext,
                          rs: RoutingSettings, log: LoggingContext) = {
    runRoute(
      requestResponseLogging {
        handleExceptions(eh) {
          handleRejections(rh) {
            route ~ get {
              path("stats") {
                complete {
                  actorRefFactory.actorSelection("/user/IO-HTTP/listener-0")
                    .ask(Http.GetStats)(1.second)
                    .mapTo[Stats]
                }
              }
            }
          }
        }
      }
    )(eh, rh, ac, rs, log)
  }

  implicit def actorSystem = context.system

  implicit val statsMarshaller: Marshaller[Stats] =
    Marshaller.delegate[Stats, String](ContentTypes.`text/plain`) { stats =>
      "Uptime                : " + stats.uptime.formatHMS + '\n' +
        "Total requests        : " + stats.totalRequests + '\n' +
        "Open requests         : " + stats.openRequests + '\n' +
        "Max open requests     : " + stats.maxOpenRequests + '\n' +
        "Total connections     : " + stats.totalConnections + '\n' +
        "Open connections      : " + stats.openConnections + '\n' +
        "Max open connections  : " + stats.maxOpenConnections + '\n' +
        "Requests timed out    : " + stats.requestTimeouts + '\n'
    }
}
