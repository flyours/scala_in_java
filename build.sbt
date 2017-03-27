organization := "Scala in Action"

name := "scala_in_java"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.6",
    "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",
    "com.typesafe.akka" %% "akka-actor" % "2.4.11",
    "com.typesafe.akka" %% "akka-remote" % "2.4.11",
    "com.typesafe.akka" %% "akka-slf4j" % "2.4.11",
    //don't use %% for libs writed using java; only libs using scala need use %%
    "org.hibernate" % "hibernate-core" % "4.3.11.Final"
)