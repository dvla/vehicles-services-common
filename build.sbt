import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "vehicles-services-common"

organization := "dvla"

version := "0.9"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.10.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

jacoco.settings

libraryDependencies ++= {
  val akkaV = "2.3.3"
  val sprayV = "1.3.1"
  Seq(
    "io.spray" % "spray-can" % sprayV,
    "io.spray" % "spray-caching" % sprayV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "io.spray" % "spray-routing" % sprayV,
    "io.spray" % "spray-testkit" % sprayV,
    "io.spray" %% "spray-json" % "1.2.5",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "ch.qos.logback" % "logback-classic" % "1.1.0",
    "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
    "org.mockito" % "mockito-all" % "1.9.5" % "test",
    "com.github.nscala-time" %% "nscala-time" % "0.8.0",
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

