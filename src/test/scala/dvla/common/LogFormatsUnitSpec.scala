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
  val expectedLogMessage = s"[TrackingID: test-trackingId]${LogFormats.logSeparator}$message"
  val expectedLogMessageWithlogData = s"$expectedLogMessage${LogFormats.logSeparator}Some(List(one, two, three))"
  implicit val logMock = mock[LoggingAdapter]

  "logErrorMessage" should {
    "log correctly" in {
      val throwable = new RuntimeException("BOOM")
      logErrorMessage(trackingId, message, throwable)
      verify(logMock).error(throwable, expectedLogMessage)
    }
  }

  "logMessage" should {
    "log correctly with Info" in {
      logMessage(trackingId, Info, message)
      verify(logMock).info(expectedLogMessage)
    }

    "log correctly with Debug" in {
      logMessage(trackingId, Debug, message)
      verify(logMock).debug(expectedLogMessage)
    }

    "log correctly with Error" in {
      logMessage(trackingId, Error, message)
      verify(logMock).error(expectedLogMessage)
    }

    "log correctly with Warn" in {
      logMessage(trackingId, Warn, message)
      verify(logMock).warning(expectedLogMessage)
    }

    "include logData when its included with Info" in {
      logMessage(trackingId, Info, message, logData)
      verify(logMock).info(expectedLogMessageWithlogData)
    }

    "include logData when its included with Debug" in {
      logMessage(trackingId, Debug, message, logData)
      verify(logMock).debug(expectedLogMessageWithlogData)
    }

    "include logData when its included with Error" in {
      logMessage(trackingId, Error, message, logData)
      verify(logMock).error(expectedLogMessageWithlogData)
    }

    "include logData when its included with Warn" in {
      logMessage(trackingId, Warn, message, logData)
      verify(logMock).warning(expectedLogMessageWithlogData)
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