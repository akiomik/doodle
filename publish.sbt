ThisBuild / name := "doodle"
ThisBuild / organization := "org.creativescala"
ThisBuild / organizationName := "Noel Welsh"
ThisBuild / organizationHomepage := Some(url("http://creativescala.org/"))
ThisBuild / version := "0.11.2"
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / isSnapshot := false

ThisBuild / credentials += Credentials(
  Path.userHome / ".sbt" / "sonatype_credential"
)
ThisBuild / pgpSecretRing := pgpPublicRing.value

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/creativescala/doodle"),
    "scm:git@github.com:creativescala/doodle.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "noelwelsh",
    name = "Noel Welsh",
    email = "noel@noelwelsh.com",
    url = url("http://noelwelsh.com")
  )
)

ThisBuild / description := "Compositional graphics for Scala"
ThisBuild / licenses := List(
  "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
)
ThisBuild / homepage := Some(url("https://github.com/creativescala/doodle"))

// Remove all additional repository other than Maven Central from POM
// I don't know if this is needed or not
// ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true

ThisBuild / publishTo := sonatypePublishToBundle.value
