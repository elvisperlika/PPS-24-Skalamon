ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.11"

resolvers += "jitpack" at "https://jitpack.io"

inThisBuild(
  List(
    scalaVersion := "3.3.6",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions ++= Seq("-Wunused:all", "-Wunused:imports")
  )
)

lazy val root = (project in file("."))
  .settings(
    name := "PPS-24-Skalamon",
    libraryDependencies += "junit" % "junit" % "4.13.2" % Test,
    libraryDependencies += "com.github.trystan" % "AsciiPanel" % "master-SNAPSHOT",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test",
    coverageEnabled := true,
    assembly / mainClass := Some("it.unibo.skalamon.main")
  )
