package dvla.common.domain.vehicle_lookup

case class VehicleLookupDisposeResponse (responseCode: Option[String],
                                         vehicleDetailsDto: Option[VehicleDetailsDisposeDto])