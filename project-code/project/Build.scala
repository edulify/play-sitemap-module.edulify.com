import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "sitemapper"
  val appVersion      = "1.1.1"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore, javaJdbc, javaEbean,
    "com.google.code" % "sitemapgen4j" % "1.0.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    publishArtifact in(Compile, packageDoc) := false,
    organization := "com.edulify"
  )
}