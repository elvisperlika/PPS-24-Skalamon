ThisBuild / version := "0.1.0-SNAPSHOT"

// Aggiungi il resolver di JitPack
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
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.19" % "test",
      "junit" % "junit" % "4.13.2" % Test,
      "com.github.trystan" % "AsciiPanel" % "master-SNAPSHOT"
    )
    coverageEnabled := true
  )
