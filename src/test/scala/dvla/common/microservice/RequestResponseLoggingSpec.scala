package dvla.common.microservice

import akka.event.LoggingAdapter
import org.scalatest.mock.MockitoSugar.mock
import org.scalatest.{Matchers, WordSpec}
import spray.routing.{HttpService, ExceptionHandler, RejectionHandler, Rejection, RoutingSettings}
import spray.testkit.ScalatestRouteTest
import org.mockito.Mockito.{verify, verifyNoMoreInteractions}
import org.mockito.ArgumentCaptor
import spray.http.HttpHeader
import spray.http.HttpHeaders.{`Remote-Address`, `X-Forwarded-For`}
import dvla.common.microservice.HttpHeaders.{`X-Real-Ip`, `Tracking-Id`}
import spray.util.LoggingContext
import scala.Some
import akka.actor.ActorContext

class RequestResponseLoggingSpec extends WordSpec with ScalatestRouteTest with Matchers with HttpService {
  def actorRefFactory = system
  final val responseBody = "the response body"
   
  "Incoming Successful Request" should {

    "Get is logged along with the response" in withTestService { testService =>
      doTest(
        testService,
        Some(`Remote-Address`("127.0.0.1")),
        Some(`Tracking-Id`("some tracking id")),
        200,
        responseBody.length
      )
    }

    "X-Forwarded-For should work" in withTestService { testService =>
      doTest(
        testService,
        Some(`X-Forwarded-For`("124.1.1.1")),
        Some(`Tracking-Id`("some tracking id")),
        200,
        responseBody.length,
        Some(`Remote-Address`("127.0.0.1"))
      )
    }

    "X-Real-Ip should work" in withTestService { testService =>
      doTest(
        testService,
        Some(`X-Real-Ip`("124.1.1.1")),
        Some(`Tracking-Id`("some tracking id")),
        200,
        responseBody.length
      )
    }

    "Remote-Address header should take precedence over X-Real-Ip" in withTestService { testService =>
      doTest(
        testService,
        Some(`Remote-Address`("127.0.0.1")),
        Some(`Tracking-Id`("some tracking id")),
        200,
        responseBody.length,
        Some(`X-Real-Ip`("124.1.1.1"))
      )
    }

    "No tracking id" in withTestService { testService =>
      doTest(
        testService,
        Some(`Remote-Address`("127.0.0.1")),
        None,
        200,
        responseBody.length
      )
    }

    "No Ip address" in withTestService { testService =>
      doTest(
        testService,
        None,
        None,
        200,
        responseBody.length
      )
    }

    "Rejected request" should {
      "be logged" in {
        doTest(
          new TestServiceFailing(mock[LoggingAdapter]),
          Some(`Remote-Address`("127.0.0.1")),
          Some(`Tracking-Id`("some tracking id")),
          404,
          42,
          Some(`X-Real-Ip`("124.1.1.1"))
        )
      }
    }

    "Rejected with exception" should {
      "be logged" in {
        doTest(
          new TestServiceException(mock[LoggingAdapter]),
          Some(`Remote-Address`("127.0.0.1")),
          Some(`Tracking-Id`("some tracking id")),
          500,
          35,
          Some(`X-Real-Ip`("124.1.1.1"))
        )
      }
    }
  }

  private def doTest(testService: TestService,
                     ip:Option[HttpHeader],
                     trackingId: Option[HttpHeader],
                     errorCode:Int,
                     bodyLength: Int,
                     extra: Option[HttpHeader] *): Unit = {
    val uri: String = "https://qa-vehicles-online.preview-dvla.co.uk/assets/javascripts-min/custom.js"
    val method = Get(uri, "random content").
      withHeaders((Seq(ip, trackingId) ++ extra ).flatten: _*)
    method ~> testService.route ~> check {
      val warnCapture = ArgumentCaptor.forClass(classOf[String])

      verify(testService.accessLogger).info(warnCapture.capture)
      verifyNoMoreInteractions(testService.accessLogger)
      warnCapture.getAllValues.size should equal(1)

      val logMessage = warnCapture.getAllValues.get(0)

      logMessage should startWith(s"""${ip.fold("-")(_.value)} - - [""")

      logMessage should endWith(
        s"""] "GET /assets/javascripts-min/custom.js HTTP/1.1" $errorCode $bodyLength "${trackingId.fold("-")(_.value)}""""
      )
    }
  }

  private def withTestService(test: TestService => Unit) {
    test(new TestService(mock[LoggingAdapter]))
  }

  implicit val loggingAdapter = mock[LoggingAdapter]

  implicit val exceptionHandler: ExceptionHandler.PF = {
    case t: Throwable => complete("Route not found")
  }

  implicit val rejectionHandler: RejectionHandler.PF = {
    case r: List[Rejection] => complete("Route rejected")
  }

  implicit val routingSettings = mock[RoutingSettings]
  implicit val loggingContext = mock[LoggingContext]
  implicit val actorContext = mock[ActorContext]

  private class TestService(override val accessLogger: LoggingAdapter) extends AccessLogging {
    val route = withAccessLogging (complete(responseBody))
  }

  private class TestServiceFailing(override val accessLogger: LoggingAdapter) extends TestService(accessLogger) {
    override val route = withAccessLogging (reject)
  }

  private class TestServiceException(override val accessLogger: LoggingAdapter) extends TestService(accessLogger) {
    override val route = withAccessLogging {
      case a: Any => throw new Exception
    }
  }
}