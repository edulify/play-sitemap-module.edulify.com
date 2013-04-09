import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "sitemapper-sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "com.edulify" % "sitemapper_2.10" % "1.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += Resolver.url("sitemapper repository", url("http://blabluble.github.com/modules/releases/"))(Resolver.ivyStylePatterns)
  )

}
