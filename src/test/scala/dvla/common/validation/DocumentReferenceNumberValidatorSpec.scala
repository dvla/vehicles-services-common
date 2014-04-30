package dvla.common.validation

import org.scalatest.{Matchers, WordSpec}

class DocumentReferenceNumberValidatorSpec extends WordSpec with Matchers {

  val minLength = 11
  val maxLength = 11

  "Document reference number validator" should {
    "indicate a valid reference number is valid" in {
      val valid = DocumentReferenceNumberValidator.validate("1" * maxLength)
      valid should equal(true)
    }

    "indicate a truncated reference number is invalid" in {
      val valid = DocumentReferenceNumberValidator.validate("1" * (minLength - 1))
      valid should equal(false)
    }

    "indicate a excessively long reference number is invalid" in {
      val valid = DocumentReferenceNumberValidator.validate("1" * (maxLength + 1))
      valid should equal(false)
    }

    "indicate a reference number with a non digit character is invalid" in {
      val valid = DocumentReferenceNumberValidator.validate("1" * (maxLength - 1) + "x")
      valid should equal(false)
    }

  }
}
