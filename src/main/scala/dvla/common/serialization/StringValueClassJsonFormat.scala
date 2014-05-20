package dvla.common.serialization

import spray.json.{DeserializationException, JsString, JsValue, JsonFormat}

class StringValueClassJsonFormat[A](unwrap: A => String, wrap: String => A) extends JsonFormat[A] {
  override def read(json: JsValue): A =
    json match {
      case JsString(value) => wrap(value)
      case _ => throw new DeserializationException("String expected")
    }

  override def write(obj: A): JsValue =
    JsString(unwrap(obj))
}
