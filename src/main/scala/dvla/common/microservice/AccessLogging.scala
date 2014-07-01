package dvla.common.microservice

import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.{Actor, ActorContext}
import akka.event.LoggingAdapter
import dvla.common.microservice.AccessLogging.dateFormat
import dvla.common.microservice.HttpHeaders.{`Tracking-Id`, `X-Real-Ip`}
import spray.http.HttpHeaders.{`Remote-Address`, `X-Forwarded-For`}
import spray.http.{HttpRequest, HttpResponse}
import spray.routing.directives.{DebuggingDirectives, ExecutionDirectives, LoggingMagnet}
import spray.routing.{ExceptionHandler, Rejected, RejectionHandler, Route, RoutingSettings}
import spray.util.LoggingContext

trait AccessLogging extends ExecutionDirectives {
  val accessLogger: LoggingAdapter

  private val accessLogging = DebuggingDirectives.logRequestResponse(LoggingMagnet (
    (request: HttpRequest) => (response: Any) => {
      val ipAddress = request.headers.find(_.is(`X-Forwarded-For`.name.toLowerCase)) orElse
        request.headers.find(_.is(`Remote-Address`.name.toLowerCase)) orElse
          request.headers.find(_.is(`X-Real-Ip`.name.toLowerCase))

      val trackingId = request.headers.find(_.is(`Tracking-Id`.name.toLowerCase)).map(_.value).getOrElse("-")
      val method = request.method
      val uri = request.uri.path
      val protocol = request.protocol
      val date = s"[${dateFormat.format(new Date())}]"
      val (responseCode, responseLength) = response match {
        case r: HttpResponse => (r.status.intValue, r.entity.data.length)
        case r: Rejected => (404, 0) // This should never happen as we should handle the Rejections before this directive
      }

      val formattedIp = ipAddress.fold("-")(_.value)
      accessLogger.info(s"""$formattedIp - - $date "$method $uri $protocol" $responseCode $responseLength "$trackingId"""")
     }
  ))

  protected def withAccessLogging(inner: Route)
                                (implicit eh: ExceptionHandler,
                                 rh: RejectionHandler,
                                 ac: ActorContext,
                                 rs: RoutingSettings,
                                 log: LoggingContext) =
    accessLogging {
      handleExceptions(eh) {
        handleRejections(rh) {
          inner
        }
      }
    }
}

trait ActorAccessLogger extends AccessLogging { this: Actor =>
  val accessLogger = akka.event.Logging(context.system, AccessLogging.getClass)
}

object AccessLogging {
  val dateFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss +SSS")
}