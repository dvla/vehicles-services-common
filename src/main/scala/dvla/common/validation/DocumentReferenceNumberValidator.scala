package dvla.common.validation

object DocumentReferenceNumberValidator {
  def validate(referenceNumber: String) = {
    val minLength = 11
    val maxLength = 11
    val regex = s"[0-9]{$minLength,$maxLength}".r // Digits only with specified size.
    regex.pattern.matcher(referenceNumber).matches
  }
}
