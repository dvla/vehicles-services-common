logLevel := Level.Debug

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += "Maven 2" at "http://repo2.maven.org/maven2"

resolvers += "Sonatype Repository" at "https://oss.sonatype.org/content/groups/public"

// Allows you to run "sbt gen-idea"
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.1")

addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.1.4")

resolvers += Resolver.url(
  "bintray-sbt-plugin-releases",
  url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(
    Resolver.ivyStylePatterns)

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.2")
