package dvla.common.clientsidesession


// When modifying this class be sure to replicate the changes in
//    vehicles-presentation-common/app/uk/.../common/clientsidesession/ClientSideSession.scala
case class TrackingId(value: String) {
  override def toString:String = value
}