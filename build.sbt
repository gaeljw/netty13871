ThisBuild / scalaVersion := "2.13.12"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "netty13871",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.21",
      "dev.zio" %% "zio-http" % "0.0.5",
      "io.opentelemetry" % "opentelemetry-sdk" % "1.35.0",
      "io.opentelemetry" % "opentelemetry-exporter-prometheus" % s"1.35.0-alpha",
      "io.opentelemetry.semconv" % "opentelemetry-semconv" % "1.23.1-alpha"
    )
  )
