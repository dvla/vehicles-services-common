package dvla.common.serialization

import java.util.TimeZone
import javax.xml.datatype.DatatypeFactory

import org.joda.time.{DateTimeZone, DateTime}
import org.scalatest.{Matchers, WordSpec}
import DateConverter.xmlDateToDateTime

class DateConverterSpec extends WordSpec with Matchers {
  private val dateFactory = DatatypeFactory.newInstance()

  "Conversion of dates" should {
    "always use timezone Europe/London" in {
      timeZoneFixture {
        xmlDateToDateTime(dateFactory.newXMLGregorianCalendar("2015-04-02+01:00")) should equal(
          Some(new DateTime("2015-04-02T00:00:00.000+01:00", DateTimeZone.forID("Europe/London")))
        )
      }
    }

    "return None if passed xmlDate is null" in {
      xmlDateToDateTime(null) should be(None)
    }
  }

  private def timeZoneFixture(test: => Unit): Unit = {
    val defaultJodaTimeZone = DateTimeZone.getDefault
    val defaultTimeZone = TimeZone.getDefault
    try {
      DateTimeZone.setDefault(DateTimeZone.forID("UTC"))
      TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
      test
    } finally {
      DateTimeZone.setDefault(defaultJodaTimeZone)
      TimeZone.setDefault(defaultTimeZone)
    }
  }
}
