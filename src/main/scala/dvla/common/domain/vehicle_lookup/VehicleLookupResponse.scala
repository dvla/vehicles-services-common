package dvla.common.domain.vehicle_lookup

case class VehicleLookupResponse (responseCode: Option[String],
                                    vehicleDetailsDto: Option[VehicleDetailsDto])
