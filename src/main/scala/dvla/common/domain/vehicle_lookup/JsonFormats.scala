package dvla.common.domain.vehicle_lookup

import dvla.common.serialization.StringValueClassJsonFormat
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object JsonFormats extends DefaultJsonProtocol with SprayJsonSupport {
  type VRMFormat = StringValueClassJsonFormat[VehicleRegistrationMark]
  type DocRefFormat = StringValueClassJsonFormat[DocumentReferenceNumber]

  implicit val vehicleRegistrationMarkFormat = new VRMFormat(_.value, VehicleRegistrationMark(_))
  implicit val documentReferenceNumberFormat = new DocRefFormat(_.value, DocumentReferenceNumber(_))

  implicit val vehicleDetailsAcquireDtoFormat = jsonFormat4(VehicleDetailsDto)

  implicit val vehicleLookupRequestFormat = jsonFormat3(VehicleLookupRequest)

  implicit val vehicleLookupAcquireResponseFormat = jsonFormat2(VehicleLookupResponse)
}