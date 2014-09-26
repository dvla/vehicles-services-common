package dvla.common.domain.vehicle_lookup

case class VehicleDetailsDto(registrationNumber: VehicleRegistrationMark,
                                    vehicleMake: String,
                                    vehicleModel: String,
                                    disposeFlag: Boolean)
