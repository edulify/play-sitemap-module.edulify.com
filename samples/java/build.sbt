name := """sitemapper-sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  "org.easytesting" % "fest-assert"    % "1.4",
  "junit" % "junit"    % "4.11",
  "com.edulify"    %% "sitemap-module" % "2.0.0"
)

resolvers ++= Seq(
  Resolver.url("Edulify Repository", url("https://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-q")