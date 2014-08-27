package dvla.common.domain.vehicle_lookup

case class VehicleDetailsAcquireDto(registrationNumber: VehicleRegistrationMark,
                                    vehicleMake: String,
                                    vehicleModel: String,
                                    disposeFlag: Boolean)
