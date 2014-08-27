package dvla.common.domain.vehicle_lookup

import dvla.common.serialization.StringValueClassJsonFormat
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object JsonFormats extends DefaultJsonProtocol with SprayJsonSupport {
  type VRMFormat = StringValueClassJsonFormat[VehicleRegistrationMark]
  type DocRefFormat = StringValueClassJsonFormat[DocumentReferenceNumber]

  implicit val vehicleRegistrationMarkFormat = new VRMFormat(_.value, VehicleRegistrationMark(_))
  implicit val documentReferenceNumberFormat = new DocRefFormat(_.value, DocumentReferenceNumber(_))

  implicit val vehicleDetailsDisposeDtoFormat = jsonFormat3(VehicleDetailsDisposeDto)
  implicit val vehicleDetailsRetentionDtoFormat = jsonFormat3(VehicleDetailsRetentionDto)
  implicit val vehicleDetailsAcquireDtoFormat = jsonFormat4(VehicleDetailsAcquireDto)

  implicit val vehicleLookupRequestFormat = jsonFormat3(VehicleLookupRequest)

  implicit val vehicleLookupDisposeResponseFormat = jsonFormat2(VehicleLookupDisposeResponse)
  implicit val vehicleLookupRetentionResponseFormat = jsonFormat2(VehicleLookupRetentionResponse)
  implicit val vehicleLookupAcquireResponseFormat = jsonFormat2(VehicleLookupAcquireResponse)
}