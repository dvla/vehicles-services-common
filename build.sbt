import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "vehicles-services-common"
organization := "dvla"
version := "0.12-SNAPSHOT"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
scalaVersion := "2.10.5"
crossScalaVersions := Seq("2.10.5", "2.11.7")
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
jacoco.settings

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.2"
  Seq(
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-caching" % sprayV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-json" % sprayV,
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.github.nscala-time" %% "nscala-time" % "2.0.0",
    "ch.qos.logback" % "logback-classic" % "1.1.0",
    "org.mockito" % "mockito-all" % "1.9.5" % "test",
    "io.spray" %% "spray-testkit" % sprayV % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "com.github.tomakehurst" % "wiremock" % "1.48" % "test"
  )
}

// ------ Start: settings to allow us to publish the binary to nexus internal repository
credentials += Credentials(Path.userHome / ".sbt/.credentials")

// Enable microservices project to use the test classes from the common
publishArtifact in (Test, packageBin) := true

publishTo <<= version { v: String =>
  val nexusHost = "http://rep002-01.skyscape.preview-dvla.co.uk:8081/nexus/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexusHost + "content/repositories/snapshots")
  else
    Some("releases" at nexusHost + "content/repositories/releases")
}
// ------ End: settings to allow us to publish the binary to nexus internal repository

// Uncomment next line when released and before publishing to github. NOTE: bintray plugin doesn't work with SNAPSHOTS
//bintrayPublishSettings

BintrayCredentials.bintrayCredentialsTask

