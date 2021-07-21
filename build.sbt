
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
  // https://mvnrepository.com/artifact/org.scala-lang.modules/scala-xml
  "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
  "org.swissbib.slsp" % "marcxml-fields" % "0.5.2",
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
   "com.softwaremill.sttp.client" %% "core" % "2.0.1",
  "org.apache.httpcomponents" % "httpclient" % "4.5.11"


  //"org.scalamock" %% "scalamock" % "4.2.0" % Test)
)

lazy val root = (project in file("."))
  .settings(
    libraryDependencies ++= dependencies,

    assemblyMergeStrategy in assembly := {
      case "log4j.properties" => MergeStrategy.first
      case "log4j2.xml" => MergeStrategy.first
      case "module-info.class" => MergeStrategy.discard
      //https://stackoverflow.com/questions/54625572/sbt-assembly-errordeduplicate-different-file-contents-found-in-io-netty-versio
      //todo: study the mechanisms!
      case "META-INF/io.netty.versions.properties" => MergeStrategy.concat
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    mainClass in assembly := Some("ch.swisscollections.Main")

  )

//assembly / mainClass := Some("org.swissbib.slsp.Job")


