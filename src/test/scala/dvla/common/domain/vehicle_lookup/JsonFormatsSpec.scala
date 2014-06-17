package dvla.common.domain.vehicle_lookup

import org.scalatest.{Matchers, WordSpec}
import dvla.common.domain.vehicle_lookup.JsonFormats._
import spray.json._

class JsonFormatsSpec extends WordSpec with Matchers {

  "JsonFormats" should {

    "successfully unmarshall a valid json vehicle lookup request payload into a request object" in {

      val expectedRequest = VehicleLookupRequest(DocumentReferenceNumber("12345678901"), VehicleRegistrationMark("WV54XKW"), "testTrackingId", "testTraderName")
      val jsonPayload = """{"referenceNumber":"12345678901","registrationNumber":"WV54XKW","trackingId":"testTrackingId", "userName":"testTraderName"}"""
      val unmarshalledRequest = jsonPayload.asJson.convertTo[VehicleLookupRequest]
      unmarshalledRequest should equal(expectedRequest)
    }

  }
}
