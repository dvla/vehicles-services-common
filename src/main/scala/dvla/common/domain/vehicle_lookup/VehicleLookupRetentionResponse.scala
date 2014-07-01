package dvla.common.domain.vehicle_lookup

case class VehicleLookupRetentionResponse (responseCode: Option[String],
                                           vehicleDetailsDto: Option[VehicleDetailsRetentionDto])
