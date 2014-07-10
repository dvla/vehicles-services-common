import de.johoop.jacoco4sbt._
import JacocoPlugin._

organization := "dvla"

version := "0.3-SNAPSHOT"

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
    "com.github.nscala-time" %% "nscala-time" % "0.8.0"
  )
}

// ------ Start: settings to allow us to publish the binary to nexus internal repository
credentials += Credentials(Path.userHome / ".sbt/.credentials")

publishTo <<= version { v: String =>
  val nexusHost = "http://rep002-01.skyscape.preview-dvla.co.uk:8081/nexus/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexusHost + "content/repositories/snapshots")
  else
    Some("releases" at nexusHost + "content/repositories/releases")
}
// ------ End: settings to allow us to publish the binary to nexus internal repository


