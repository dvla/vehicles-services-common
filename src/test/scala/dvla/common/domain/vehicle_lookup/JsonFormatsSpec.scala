package dvla.common.domain.vehicle_lookup

import org.scalatest.{Matchers, WordSpec}
import dvla.common.domain.vehicle_lookup.JsonFormats._
import spray.json._
import dvla.common.domain.vehicle_lookup.VehicleLookupRequest

class JsonFormatsSpec extends WordSpec with Matchers {

  "JsonFormats" should {

    "successfully unmarshall a valid json vehicle lookup request payload into a request object" in {

      val expectedRequest = VehicleLookupRequest("12345678901", "WV54XKW")
      val jsonPayload = """{"referenceNumber":"12345678901","registrationNumber":"WV54XKW"}"""
      val unmarshalledRequest = jsonPayload.asJson.convertTo[VehicleLookupRequest]
      unmarshalledRequest should equal(expectedRequest)
    }

  }
}