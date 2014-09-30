import java.io.File

import sbt.{IO, taskKey}

object BintrayCredentials {
  lazy val bintrayCredentials = taskKey[Unit]("Writes down the bintray credentials file")

  lazy val bintrayCredentialsTask = bintrayCredentials := {
    val bintrayUser = sys.props.get("BINTRAY_USER")
      .orElse(sys.env.get("BINTRAY_USER"))
      .orElse(throw new Exception("BINTRAY_USER system property or environment variable not set"))
      .get
    val bintrayApiKey = sys.props.get("BINTRAY_API_KEY")
      .orElse(sys.env.get("BINTRAY_API_KEY"))
      .orElse(throw new Exception("BINTRAY_API_KEY system property or environment variable not set"))
      .get
    val credentialsFile = new File(sys.props.get("user.home").get + "/.bintray/.credentials")
    println(s"Writing the bintray credentials file to ${credentialsFile.getAbsolutePath}")
    IO.write(
      credentialsFile,
      s"""realm=Bintray Public Repo
        |host=bintray.com
        |user=$bintrayUser
        |password=$bintrayApiKey
        |
        |""".stripMargin
    )
  }
}