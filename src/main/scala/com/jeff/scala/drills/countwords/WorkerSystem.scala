package com.jeff.scala.drills.countwords

import java.lang.management.ManagementFactory

import akka.actor._
import com.typesafe.config.ConfigFactory

object WorkerSystem extends App {
    //this is the remote deamon which can be configured via application.conf or on the fly to run actor transparently.
    val workerSystem = ActorSystem("workersystem", ConfigFactory.load.getConfig("workersystem"))

    println("Started the WorkerSystem: pid=" + ManagementFactory.getRuntimeMXBean.getName)
}