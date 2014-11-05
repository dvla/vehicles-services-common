package dvla.common.microservice.helpers

import java.util.GregorianCalendar
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

import org.joda.time.DateTime

class XMLDate {
  def toXMLGregorianCalendar(dateTime: DateTime): XMLGregorianCalendar = {
    val cal = new GregorianCalendar
    cal.setTime(dateTime.toDate)
    DatatypeFactory.newInstance().newXMLGregorianCalendar(cal)
  }

  def toXMLGregorianCalendar(dateTimeString: String): XMLGregorianCalendar = {
    val dateTime = new DateTime(dateTimeString)
    toXMLGregorianCalendar(dateTime)
  }

  def toDateTime(xmlGregorianCalendar: XMLGregorianCalendar): DateTime = {
    new DateTime(xmlGregorianCalendar.toGregorianCalendar.getTime)
  }
}
