package dvla.common.domain.vehicle_lookup

import spray.json._
import spray.httpx.SprayJsonSupport

object JsonFormats extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val vehicleDetailsDisposeDtoFormat = jsonFormat3(VehicleDetailsDisposeDto)
  implicit val vehicleDetailsRetentionDtoFormat = jsonFormat5(VehicleDetailsRetentionDto)

  implicit val vehicleLookupRequestFormat = jsonFormat2(VehicleLookupRequest)

  implicit val vehicleLookupDisposeResponseFormat = jsonFormat2(VehicleLookupDisposeResponse)
  implicit val vehicleLookupRetentionResponseFormat = jsonFormat2(VehicleLookupRetentionResponse)

}
