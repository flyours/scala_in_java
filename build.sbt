organization := "Scala in Action"

name := "scala_in_java"

version := "0.1"

scalaVersion := "2.11.7"

val akkaVersion = "2.4.14"

libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "2.9.2",
    "ch.qos.logback" % "logback-classic" % "1.1.6",
    "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % "2.8.2",
    "org.json4s" %% "json4s-jackson" % "3.3.0",
    "org.json4s" %% "json4s-ext" % "3.3.0",
    //don't use %% for libs writed using java; only libs using scala need use %%
    "org.hibernate" % "hibernate-core" % "4.3.11.Final",

    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)