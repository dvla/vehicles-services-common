package dvla.common.domain.vehicle_lookup

import dvla.common.validation.{VrnValidator, DocumentReferenceNumberValidator}

case class VehicleLookupRequest(referenceNumber: String,
                                  registrationNumber: String) {
  require(DocumentReferenceNumberValidator.validate(referenceNumber), "Reference number must be an 11-digit number")
  require(registrationNumber.length >= 2 && registrationNumber.length <= 8, "registrationNumber field must be between 2 and 8 characters")
  require(VrnValidator.validate(registrationNumber), "registrationNumber must match valid format")
}
