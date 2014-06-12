package dvla.common.microservice

import akka.event.LoggingAdapter
import akka.actor.{ActorContext, Actor}
import spray.routing.directives.{ExecutionDirectives, LoggingMagnet, DebuggingDirectives}
import spray.http.HttpHeaders.{`X-Forwarded-For`, `Remote-Address`}
import dvla.common.microservice.HttpHeaders.{`X-Real-Ip`, `Tracking-Id`}
import java.text.SimpleDateFormat
import java.util.Date
import spray.routing._
import AccessLogging._
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.routing.Rejected
import spray.util.LoggingContext

object AccessLogging {
  val dateFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss +SSS")
}

trait AccessLogging extends ExecutionDirectives {
  val accessLogger: LoggingAdapter

  private val accessLogging = DebuggingDirectives.logRequestResponse(LoggingMagnet (
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
        case r: Rejected => (404, 0) // This should never happen as we should handle the Rejections before this directive
      }
      accessLogger.info(s"""$ipAddress - - $date "$method $uri $protocol" $responseCode $responseLength "$trackingId"""")
     }
  ))

  protected def withAccessLogging(inner: Route)
                                (implicit eh: ExceptionHandler, rh: RejectionHandler, ac: ActorContext,
                                               rs: RoutingSettings, log: LoggingContext) = {
    accessLogging {
      handleExceptions(eh) {
        handleRejections(rh) {
          inner
        }
      }
    }
  }
}

trait ActorAccessLogger extends AccessLogging { this: Actor =>
  val accessLogger = akka.event.Logging(context.system, AccessLogging.getClass)
}
