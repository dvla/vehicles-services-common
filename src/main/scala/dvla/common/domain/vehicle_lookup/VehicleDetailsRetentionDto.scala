package dvla.common.domain.vehicle_lookup

case class VehicleDetailsRetentionDto(registrationNumber: String,
                                      vehicleMake: String,
                                      vehicleModel: String,
                                      exportMarker: Boolean,
                                      vehicleInspectionMarker: Boolean,
                                      disposalIndicator: Boolean)