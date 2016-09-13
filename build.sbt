
name := "vehicles-services-common"
organization := "dvla"
version := "0.15"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
scalaVersion := "2.10.6"
crossScalaVersions := Seq("2.10.6", "2.11.8")
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

test in assembly := {}

libraryDependencies ++= {
  val akkaV = "2.3.15"
  val sprayV = "1.3.2"
  Seq(
    "com.github.nscala-time" %% "nscala-time" % "2.12.0",
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "io.spray" %% "spray-caching" % sprayV,
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-json" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    //test
    "com.github.tomakehurst" % "wiremock" % "1.58" % "test",
    "io.spray" %% "spray-testkit" % sprayV % "test",
    "org.mockito" % "mockito-all" % "1.10.19" % "test",
    "org.scalatest" %% "scalatest" % "2.2.6" % "test"
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
bintrayPublishSettings

BintrayCredentials.bintrayCredentialsTask

