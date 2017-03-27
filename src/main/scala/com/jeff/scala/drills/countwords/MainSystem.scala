package com.jeff.scala.drills.countwords


import java.lang.management.ManagementFactory

import akka.actor._
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

case class FileToCount(url: String) {
    def countWords = {
        //Source.fromURL(new URL(url)).getLines.foldRight(0)(_.split(" ").size + _)
        url.length
    }
}

case class WordCount(url: String, count: Int)

case class StartCounting(urls: Seq[String], numActors: Int)

object MainSystem {
    //this is sync log
    val logger = LoggerFactory.getLogger(MainSystem.getClass)

    class MainActor(accumulator: ActorRef) extends Actor with ActorLogging {
        def receive = {
            case "start" =>
                val urls = List("http://www.infoq.com/",
                    "http://www.qq.com/",
                    "http://www.sina.com/",
                    "http://www.baidu.com/",
                    "http://www.ifeng.com/",
                    "http://www.sohu.com/")
                accumulator ! StartCounting(urls, 2)
                log.debug("MainActor send urls done.", new Throwable("MainActor"))

        }
    }

    def main(args: Array[String]) = run

    private def run = {
        println("Started the MainSystem: pid=" + ManagementFactory.getRuntimeMXBean.getName)
        val mainSystem = ActorSystem("main", ConfigFactory.load.getConfig("mainsystem"))
        val accumulator = mainSystem.actorOf(Props[WordCountMaster], name = "wordCountMaster")
        val m = mainSystem.actorOf(Props(new MainActor(accumulator)))
        m ! "start"
    }
}