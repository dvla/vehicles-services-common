package dvla.common.domain

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.httpx.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, deserializationError, JsonFormat, JsString, JsValue}

trait CommonJsonFormats extends DefaultJsonProtocol with SprayJsonSupport {

  // Handles this type of formatted string 2014-03-04T00:00:00.000Z
  implicit object DateTimeJsonFormat extends JsonFormat[DateTime] {
    def write(dateTime: DateTime) = {
      val formatter = ISODateTimeFormat.dateTime
      JsString(formatter.print(dateTime))
    }

    def read(value: JsValue) = value match {
      case JsString(isoString) =>
        ISODateTimeFormat.dateTime().parseDateTime(isoString)
      case _ => deserializationError("Could not deserialize the iso date time")
    }
  }
  implicit val microserviceResponseJsonFormat = jsonFormat2(MicroserviceResponse)
}
