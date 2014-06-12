package dvla.common.microservice

import akka.event.LoggingAdapter
import org.scalatest.mock.MockitoSugar._
import org.scalatest.{Matchers, WordSpec}
import spray.http.StatusCodes._
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest
import org.mockito.Matchers._
import org.mockito.Mockito.{verify, verifyNoMoreInteractions}
import org.mockito.ArgumentCaptor
import spray.http.HttpHeader
import spray.http.HttpHeaders.{`User-Agent`, `Remote-Address`, `X-Forwarded-For`}
import dvla.common.microservice.HttpHeaders.{`X-Real-Ip`, `Tracking-Id`}

class RequestResponseLoggingSpec extends WordSpec with ScalatestRouteTest with Matchers with HttpService {
  def actorRefFactory = system
  final val responseBody = "the response body"
   
  "Incoming Successful Request" should {

    "Get is logged along with the response" in withTestService { testService =>
      doTest(testService, Some(`Remote-Address`("127.0.0.1")), Some(`Tracking-Id`("some tracking id")))
    }

    "X-Forwarded-For should work" in withTestService { testService =>
      doTest(
        testService,
        Some(`X-Forwarded-For`("124.1.1.1")),
        Some(`Tracking-Id`("some tracking id")),
        Some(`Remote-Address`("127.0.0.1"))
      )
    }

    "X-Real-Ip should work" in withTestService { testService =>
      doTest(
        testService,
        Some(`X-Real-Ip`("124.1.1.1")),
        Some(`Tracking-Id`("some tracking id"))
      )
    }

    "Remote-Address header should take precedence over X-Real-Ip" in withTestService { testService =>
      doTest(
        testService,
        Some(`Remote-Address`("127.0.0.1")),
        Some(`Tracking-Id`("some tracking id")),
        Some(`X-Real-Ip`("124.1.1.1"))
      )
    }

    "No tracking id" in withTestService { testService =>
      doTest(
        testService,
        Some(`Remote-Address`("127.0.0.1")),
        None
      )
    }

    "No Ip address" in withTestService { testService =>
      doTest(
        testService,
        None,
        None
      )
    }

    "Rejected request" should {
      "be logged" in withTestServiceWithRejection { testService =>
        doTest(
          testService,
          Some(`Remote-Address`("127.0.0.1")),
          Some(`Tracking-Id`("some tracking id")),
          Some(`X-Real-Ip`("124.1.1.1"))
        )
      }
    }
  }

  private def doTest(testService: TestService, ip:Option[HttpHeader], trackingId: Option[HttpHeader], extra: Option[HttpHeader] *): Unit = {
//    val ip: String = "127.0.0.1"
//    val trackigId: String = "some tracking id"
    val uri: String = "https://qa-vehicles-online.preview-dvla.co.uk/assets/javascripts-min/custom.js"
    val method = Get(uri, "random content").
      withHeaders((Seq(ip, trackingId) ++ extra ).flatten: _*)
    method ~> testService.route ~> check {
      val warnCapture = ArgumentCaptor.forClass(classOf[String])

      verify(testService.requestLogger).info(warnCapture.capture)
      verifyNoMoreInteractions(testService.requestLogger)
      warnCapture.getAllValues.size should equal(1)

      val logMessage = warnCapture.getAllValues.get(0)

      logMessage should startWith(s"""${ip.fold("-")(_.value)} - - [""")

      logMessage should endWith(
        s"""] "GET /assets/javascripts-min/custom.js HTTP/1.1" 200 ${responseBody.length} "${trackingId.fold("-")(_.value)}""""
      )
    }
  }

  private def withTestService(test: TestService => Unit) {
    test(new TestService(mock[LoggingAdapter]))
  }

  private def withTestServiceWithRejection(test: TestServiceFailing => Unit) {
    test(new TestServiceFailing(mock[LoggingAdapter]))
  }

  private class TestService(override val requestLogger: LoggingAdapter) extends RequestResponseLogging {
    val route = requestResponseLogging {
      complete(responseBody)
    }
  }

  private class TestServiceFailing(override val requestLogger: LoggingAdapter) extends TestService(requestLogger) {
    override val route = requestResponseLogging {
      reject
    }
  }
}

