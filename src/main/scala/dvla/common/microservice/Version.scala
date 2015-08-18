package dvla.common.microservice

import scala.io.Source.fromInputStream

trait Version {

  def version: String = {
    def prop(name: String) = sys.props.getOrElse(name, "Unknown")
    val buildDetails = Option(getClass.getResource("/build-details.txt"))
      .fold("No build details /build-details.txt doesn't exist in the classpath") {
      detailsStream => fromInputStream(detailsStream.openStream()).mkString
    }
    s"""$buildDetails
         |Running as: ${prop("user.name")}@${java.net.InetAddress.getLocalHost.getHostName}
         |Runtime OS: ${prop("os.name")}-${prop("os.version")}
         |Runtime Java: ${prop("java.version")} ${prop("java.vendor")}
       """.stripMargin
  }
}
