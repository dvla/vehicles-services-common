package dvla.common.domain.vehicle_lookup

case class VehicleLookupAcquireResponse (responseCode: Option[String],
                                    vehicleDetailsDto: Option[VehicleDetailsAcquireDto])
