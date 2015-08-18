package dvla.common.domain.vehicle_lookup

import org.scalatest.{Matchers, WordSpec}

class VehicleRegistrationMarkSpec extends WordSpec with Matchers {

  /**
   * Vehicle registration mark tests invalid vrms
   */
  val allInvalidVrmFormats = Seq("9A99A99", "9A9A99", "9AAAA9", "9A99AA9", "99AAA99", "99AAAA9", "A9AAA99", "A9A9A9A",
    "A9A9A99", "A9AAAA9", "99A999", "A9A9AAA", "99A9A9", "9AA9AA9", "AAA9A99", "999A999", "AAA9AA9", "A9AA99",
    "A99A999", "A999A9", "9AA999", "A99999A", "A99A9AA", "99A99", "9AAA9", "A99A9", "A99A9A9", "9A9999A", "99AA9A",
    "9A999A", "9A99999", "9AA9A9", "A9999AA", "99AA9", "9A999AA", "999A9A", "99AAAA", "99AA99A", "A99A", "AA9AA9A",
    "AA999AA", "AAAA9A", "AA9AAAA", "AA9A9A", "AAAAAA", "A9A999A", "A9AA9AA", "A9A99AA", "9AAAA9A", "9AA99AA", "999AA9",
    "999AA99", "999AAA9", "A9A99A", "A99AA99", "A999A99", "A99AAA9", "9AA99A9", "AA9AA", "9AAAAA9", "9A9AAA", "9A9AA9",
    "9999", "99A9A9A", "99AAAAA", "99A9", "AA99AA9", "99A9AAA", "9AA99", "999A9", "99999A", "999A99A", "A99999",
    "999A9AA", "999999A", "A9AA9A", "AAA9AA", "AAA9A9", "A99A9A", "A9AAAA", "A9AA99A", "9AAA99A", "9A9", "A9A9A",
    "AAAAA", "9A9AA9A", "9AAA9AA", "9A99AA", "AAAA999", "9999999", "999A9A9", "AAAA9AA", "9A99A", "999A99", "99AAA9",
    "9A9AA", "9A9AAA9", "AA9AA99", "99A9999", "99AA9A9", "AAAA99", "9999A9A", "A9A9", "AA99A99", "AA9AAA9", "99A99AA",
    "99A99A9", "AAAAA9", "A9A9999", "A999A9A", "AA99A", "A9A99A9", "9AAAA99", "A9AAAAA", "9AA9A9A", "9AAAAAA", "99",
    "AAAAA99", "9AA9AAA", "AA9A", "AAA9A9A", "AAAAAA9", "A9A999", "9A9A", "AAAA", "AAA9AAA", "9AA9A", "A999AA",
    "A99A99A", "9A9A99A", "AA9A9", "9AA99A", "99A9A", "9AAAA", "9A9A9AA", "9AA9AA", "99A9A99", "AA9A99A", "AA9A999",
    "AA9999A", "AA9A9AA", "99A9AA9", "9AA9", "AA999A", "AA99AA", "A9A9AA9", "A9A", "AAAA99A", "AAA", "99999AA",
    "99999A9", "A99A99", "A9AAA9", "A999999", "9A9999", "A99AA9", "A9999A9", "9A9AA99", "9A999A9", "9AAA99", "9A99A9",
    "99AA999", "9A9A9A", "9AAAAA", "AAAA9A9", "9A99AAA", "A9AA", "AA99A9A", "99AAA9A", "AAA99A9", "AA9A99", "A9AAA9A",
    "AA9AAA", "AA9AA9", "99999", "9A9A9", "99A99A", "99A9AA", "9999A99", "AA", "9999AA9", "A9999A", "A9A9AA", "A9A9A9",
    "A999A", "A999AA9", "9AA9A99", "999", "A99AA", "9A99", "9A9A999", "999999", "9A9A9A9", "99AA99", "9999A9",
    "AA99999", "AA9A9A9", "999AA9A", "99AA9AA", "99A999A", "AA999A9", "A9AA999", "AA99A9", "999AAAA", "9AAA999",
    "A99AA9A", "A9A99", "AAAA9", "A9AA9A9", "9AA999A", "9AA9999", "9AAA9A", "9AAA9A9", "A99AAAA", "9A99A9A", "A9AA9",
    "9A9AAAA", "AAAAA9A", "AAA99AA", "AAAAAAA", "9A999", "", "ab53ab%")
  allInvalidVrmFormats.foreach(vrm => "indicate the vrm is invalid: " + vrm in {
    val valid = VehicleRegistrationMark.validate(vrm)
    valid should equal(false)
  })

  /**
   * Vehicle registration mark tests valid vrms
   */
  val allValidVrmFormats = Seq("A9", "A99", "A999", "A9999", "AA9", "AA99", "AA999", "AA9999", "AAA9", "AAA99", "AAA999",
    "AAA9999", "AAA9A", "AAA99A", "AAA999A", "9A", "9AA", "9AAA", "99A", "99AA", "99AAA", "999A", "999AA", "999AAA",
    "9999A", "9999AA", "A9AAA", "A99AAA", "A999AAA", "AA99AAA", "9999AAA")
  allValidVrmFormats.foreach(vrm => "indicate the vrm is valid: " + vrm in {
    val valid = VehicleRegistrationMark.validate(vrm)
    valid should equal(true)
  })
}