
name := "andreas_cluster_features"

version := "0.1"
scalaVersion := "2.12.8"
organization := "org.swissbib.ml"


ThisBuild / resolvers ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  "marcxml-fields" at "https://gitlab.com/api/v4/projects/12974592/packages/maven",
  Resolver.mavenLocal
)

val dependencies = Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "org.swissbib.slsp" % "marcxml-fields" % "0.5.2",
  "com.typesafe.play" %% "play-json" % "2.7.3",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
   "com.softwaremill.sttp.client" %% "core" % "2.0.1",
  "org.apache.httpcomponents" % "httpclient" % "4.5.11"


  //"org.scalamock" %% "scalamock" % "4.2.0" % Test)
)

lazy val root = (project in file("."))
  .settings(
    libraryDependencies ++= dependencies
  )

//assembly / mainClass := Some("org.swissbib.slsp.Job")


