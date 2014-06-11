package dvla.common.microservice

import spray.routing.directives.{LoggingMagnet, DebuggingDirectives}
import spray.http.HttpRequest
import akka.actor.ActorLogging

trait RequestResponseLogging { self: ActorLogging =>

  protected val requestResponseLogging = DebuggingDirectives.logRequestResponse(LoggingMagnet (
    (request: HttpRequest) => (response: Any) => {
      ()
    }
  ))
}
