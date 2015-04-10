package dvla.common.serialization

import java.util.Locale
import java.util.TimeZone
import javax.xml.datatype.XMLGregorianCalendar

import org.joda.time.{DateTimeZone, DateTime}

object DateConverter {
  def xmlDateToDateTime(xmlDate: XMLGregorianCalendar): Option[DateTime] = {
    if (xmlDate == null) None
    else Some(new DateTime(
      xmlDate.toGregorianCalendar(
        TimeZone.getTimeZone("Europe/London"),
        Locale.forLanguageTag("en_GB"),
        null
      ).getTime,
      DateTimeZone.forID("Europe/London")
    ))
  }
}
