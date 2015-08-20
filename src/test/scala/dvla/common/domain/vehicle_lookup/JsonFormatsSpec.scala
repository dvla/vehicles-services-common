package dvla.common.domain.vehicle_lookup

import dvla.common.domain.vehicle_lookup.JsonFormats.vehicleLookupRequestFormat
import org.scalatest.{Matchers, WordSpec}
import spray.json.pimpString

class JsonFormatsSpec extends WordSpec with Matchers {

  "JsonFormats" should {
    "successfully unmarshall a valid json vehicle lookup request payload into a request object" in {
      val expectedRequest = VehicleLookupRequest(
        DocumentReferenceNumber("12345678901"),
        VehicleRegistrationMark("WV54XKW"),
        "testTraderName"
      )
      val jsonPayload =
        """{
          |"referenceNumber":"12345678901",
          |"registrationNumber":"WV54XKW",
          |"userName":"testTraderName"
          |}""".stripMargin
      val unmarshalledRequest = jsonPayload.parseJson.convertTo[VehicleLookupRequest]
      unmarshalledRequest should equal(expectedRequest)
    }
  }
}