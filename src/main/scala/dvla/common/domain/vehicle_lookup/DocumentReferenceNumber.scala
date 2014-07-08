package dvla.common.domain.vehicle_lookup

class DocumentReferenceNumber private(val value: String) extends AnyVal

object DocumentReferenceNumber {
  private final val WhitelistRegexFormula = "[0-9]{11}"
  private final val WhitelistRegex = WhitelistRegexFormula.r

  def apply(value: String): DocumentReferenceNumber = {
    require(validate(value), "Document Reference Number must be an 11-digit number")
    new DocumentReferenceNumber(value)
  }

  def validate(referenceNumber: String): Boolean =
    WhitelistRegex.pattern.matcher(referenceNumber).matches
}