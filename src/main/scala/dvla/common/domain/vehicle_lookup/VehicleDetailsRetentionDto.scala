package dvla.common.domain.vehicle_lookup

case class VehicleDetailsRetentionDto(registrationNumber: VehicleRegistrationMark,
                                      vehicleMake: String,
                                      vehicleModel: String,
                                      exportMarker: Boolean,
                                      scrappedMarker: Boolean)
