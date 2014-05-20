package dvla.common.domain.vehicle_lookup

import spray.json._
import spray.httpx.SprayJsonSupport
import dvla.common.serialization.StringValueClassJsonFormat

object JsonFormats extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val vehicleRegistrationMarkFormat = new StringValueClassJsonFormat[VehicleRegistrationMark](_.value, VehicleRegistrationMark(_))
  implicit val documentReferenceNumberFormat = new StringValueClassJsonFormat[DocumentReferenceNumber](_.value, DocumentReferenceNumber(_))

  implicit val vehicleDetailsDisposeDtoFormat = jsonFormat3(VehicleDetailsDisposeDto)
  implicit val vehicleDetailsRetentionDtoFormat = jsonFormat5(VehicleDetailsRetentionDto)

  implicit val vehicleLookupRequestFormat = jsonFormat2(VehicleLookupRequest)

  implicit val vehicleLookupDisposeResponseFormat = jsonFormat2(VehicleLookupDisposeResponse)
  implicit val vehicleLookupRetentionResponseFormat = jsonFormat2(VehicleLookupRetentionResponse)

}
