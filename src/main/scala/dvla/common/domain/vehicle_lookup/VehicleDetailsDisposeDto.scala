package dvla.common.domain.vehicle_lookup

case class VehicleDetailsDisposeDto(registrationNumber: VehicleRegistrationMark,
                                    vehicleMake: String,
                                    vehicleModel: String)