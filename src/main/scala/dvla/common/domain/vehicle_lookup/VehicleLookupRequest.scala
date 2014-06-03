package dvla.common.domain.vehicle_lookup

case class VehicleLookupRequest(referenceNumber: DocumentReferenceNumber,
                                registrationNumber: VehicleRegistrationMark,
                                trackingId: String,
                                userName: String)
