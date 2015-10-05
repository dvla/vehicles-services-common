package dvla.common.domain.vehicle_lookup

class VehicleRegistrationMark private(val value: String) extends AnyVal {
  override def toString = value
}

object VehicleRegistrationMark {

  private final val WhitelistRegexFormula =
    """^
      |([A-Za-z]{3}[0-9]{1,4})|
      |([A-Za-z][0-9]{1,3}[A-Za-z]{3})|
      |([A-Za-z]{3}[0-9]{1,3}[A-Za-z])|
      |([A-Za-z]{2}[0-9]{2}[A-Za-z]{3})|
      |([A-Za-z]{1,3}[0-9]{1,3})|
      |([0-9]{1,4}[A-Za-z]{1,3})|
      |([A-Za-z]{1,2}[0-9]{1,4})$"""

  private lazy val WhitelistRegex =
    WhitelistRegexFormula.stripMargin.replace("\n", "").r

  def apply(value: String): VehicleRegistrationMark = {
    require(value.length >= 2 && value.length <= 8, "VRM must be between 2 and 8 characters")
    require(validate(value), "VRM must match valid format")
    new VehicleRegistrationMark(value)
  }

  def validate(vrm: String): Boolean =
    WhitelistRegex.pattern.matcher(vrm).matches
}