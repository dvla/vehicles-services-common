package dvla.common.serialization

import spray.json.{DefaultJsonProtocol, JsonParser}
import spray.httpx.SprayJsonSupport
import org.scalatest.WordSpec

class StringValueClassJsonFormatSpec extends WordSpec {

   "string value class JSON format" should {

     "treat string value classes as JSON string literals" in new Fixture {

       val expectedValue = "foo"
       val serializedComplexObject = s"""{ "valueUnderTest": "$expectedValue" }"""
       val deserializedComplexObject = JsonParser(serializedComplexObject)
       val complexObject = deserializedComplexObject.convertTo[ComplexType](complexTypeFormat) // Unsure why this isn't picked up implicitly.
       val actualValue = complexObject.valueUnderTest.value

       assert(actualValue === expectedValue)
     }
   }

   trait Fixture extends DefaultJsonProtocol with SprayJsonSupport {
     implicit val formatUnderTest = new StringValueClassJsonFormat[StringValueClass](_.value, StringValueClass(_))
     implicit val complexTypeFormat = jsonFormat1(ComplexType)
   }
 }

case class ComplexType(valueUnderTest: StringValueClass)

class StringValueClass private(val value: String) extends AnyVal

object StringValueClass {
  def apply(value: String): StringValueClass =
    new StringValueClass(value)
}
