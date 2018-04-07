// Comment to get more information during initialization
logLevel := Level.Warn

resolvers += "iBiblio Maven" at "http://mirrors.ibiblio.org/maven2/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % System.getProperty("play.version", "2.6.12"))

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")
