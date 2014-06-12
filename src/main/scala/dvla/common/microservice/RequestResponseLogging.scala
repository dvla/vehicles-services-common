package dvla.common.microservice

import akka.event.LoggingAdapter
import akka.actor.Actor
import spray.http.{HttpResponse, HttpRequest}
import spray.routing.directives.{LoggingMagnet, DebuggingDirectives}
import spray.http.HttpHeaders.{`X-Forwarded-For`, `Remote-Address`}
import dvla.common.microservice.HttpHeaders.{`X-Real-Ip`, `Tracking-Id`}
import java.text.SimpleDateFormat
import java.util.Date
import spray.routing.Rejected
import RequestResponseLogging._

object RequestResponseLogging {
  val dateFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss +SSS")
}

trait RequestResponseLogging {
  val requestLogger: LoggingAdapter

  protected val requestResponseLogging = DebuggingDirectives.logRequestResponse(LoggingMagnet (
    (request: HttpRequest) => (response: Any) => {
      val ipAddress = request.headers.find(_.is(`X-Forwarded-For`.name.toLowerCase)).fold {
        request.headers.find(_.is(`Remote-Address`.name.toLowerCase)).fold {
          request.headers.find(_.is(`X-Real-Ip`.name.toLowerCase)).fold("-")(_.value)
        } (_.value)
      } (_.value)


      val trackingId = request.headers.find(_.is(`Tracking-Id`.name.toLowerCase)).map(_.value).getOrElse("-")
      val method = request.method
      val uri = request.uri.path
      val protocol = request.protocol
      val date = s"[${dateFormat.format(new Date())}]"
      val (responseCode, responseLength) = response match {
        case r: HttpResponse => (r.status.intValue, r.entity.data.length)
        case r: Rejected => (404, 0)
      }
      requestLogger.info(s"""$ipAddress - - $date "$method $uri $protocol" $responseCode $responseLength "$trackingId"""")
     }
  ))
}

//class ServiceAccessLogger(requestLogger: LoggingAdapter) extends AccessLogger {
//
//  override def logAccess(request:HttpRequest, response:HttpResponse, time:Long):Unit = {
//    val ipAddress = request.headers.find(_.is(`X-Forwarded-For`.name.toLowerCase)).fold {
//      request.headers.find(_.is(`Remote-Address`.name.toLowerCase)).fold {
//        request.headers.find(_.is(`X-Real-Ip`.name.toLowerCase)).fold("-")(_.value)
//      } (_.value)
//    } (_.value)
//
//    val trackingId = request.headers.find(_.is(`Tracking-Id`.name.toLowerCase)).map(_.value).getOrElse("-")
//    val method = request.method
//    val uri = request.uri.path
//    val protocol = request.protocol
//    val date = s"[${dateFormat.format(new Date())}]"
//    val responseCode = response.status.intValue
//    val responseLength = response.entity.data.length
//    requestLogger.info(s"""$ipAddress - - $date "$method $uri $protocol" $responseCode $responseLength "$trackingId"""")
//  }
//
//  override def accessAlreadyLogged(request:HttpRequest, response:HttpResponse, time:Long):Unit = logAccess(request, response, time)
//
//}

//trait ActorAccessLogging extends RequestResponseLogging { this: Actor =>
//  val accessLogger = new ServiceAccessLogger(akka.event.Logging(context.system, RequestResponseLogging.getClass))
//}

trait ActorRequestLogger extends RequestResponseLogging { this: Actor =>
  val requestLogger = akka.event.Logging(context.system, RequestResponseLogging.getClass)
}
