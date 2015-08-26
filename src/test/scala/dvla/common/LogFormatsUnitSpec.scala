package dvla.common

import akka.event.LoggingAdapter
import dvla.common.LogFormats.DVLALogger
import dvla.common.clientsidesession.TrackingId
import org.mockito.Mockito.verify
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar.mock


class LogFormatsUnitSpec extends WordSpec with Matchers with DVLALogger {
  val trackingId = TrackingId("test-trackingId")
  val message = "Test message to log"
  val logData = Some(List("one", "two", "three"))
  val expectedLogMessage = s"[TrackingID: test-trackingId]${LogFormats.logSeperator}$message"
  val expectedLogMessageWithlogData = s"$expectedLogMessage${LogFormats.logSeperator}Some(List(one, two, three))"

  "logMessage" should {
    "log correctly with Info" in {
      implicit val log = mock[LoggingAdapter]

      logMessage(trackingId, Info, message)
      verify(log).info(expectedLogMessage)
    }

    "log correctly with Debug" in {
      implicit val log = mock[LoggingAdapter]

      logMessage(trackingId, Debug, message)
      verify(log).debug(expectedLogMessage)
    }

    "log correctly with Error" in {
      implicit val log = mock[LoggingAdapter]

      logMessage(trackingId, Error, message)
      verify(log).error(expectedLogMessage)
    }

    "log correctly with Warn" in {
      implicit val log = mock[LoggingAdapter]

      logMessage(trackingId, Warn, message)
      verify(log).warning(expectedLogMessage)
    }

    "include logData when its included with Info" in {
      implicit val log = mock[LoggingAdapter]

      logMessage(trackingId, Info, message, logData)
      verify(log).info(expectedLogMessageWithlogData)
    }

    "include logData when its included with Debug" in {
      implicit val log = mock[LoggingAdapter]

      logMessage(trackingId, Debug, message, logData)
      verify(log).debug(expectedLogMessageWithlogData)
    }

    "include logData when its included with Error" in {
      implicit val log = mock[LoggingAdapter]

      logMessage(trackingId, Error, message, logData)
      verify(log).error(expectedLogMessageWithlogData)
    }

    "include logData when its included with Warn" in {
      implicit val log = mock[LoggingAdapter]

      logMessage(trackingId, Warn, message, logData)
      verify(log).warning(expectedLogMessageWithlogData)
    }
  }
  "Anonymize" should {
    "empty string should return null" in {
      val inputString = null
      LogFormats.anonymize(inputString) should equal("null")
    }

    "string of greater than 8 characters should return 4 characters and the rest stars" in {
      val inputString = "qwertyuiop"
      LogFormats.anonymize(inputString) should equal("******uiop")
    }

    "string of less than 8 characters should return half characters and the rest stars" in {
      val inputString = "qwer"
      LogFormats.anonymize(inputString) should equal("**er")
    }

    "string with an odd number of characters should return more than half stars and the remainder characters" in {
      val inputString = "qwert"
      LogFormats.anonymize(inputString) should equal("***rt")
    }
  }
}