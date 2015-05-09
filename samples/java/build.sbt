name := """sitemapper-sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  // Add your project dependencies here,
  javaCore,
  javaJdbc,
  javaEbean,
  "com.edulify" %% "sitemapper" % "1.1.8"
)

resolvers ++= Seq(
  Resolver.url("Edulify Repository", url("https://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
)
