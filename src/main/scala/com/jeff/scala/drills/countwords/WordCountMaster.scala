package com.jeff.scala.drills.countwords


import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.jeff.drills.countwords.WordCountWorker
import org.slf4j.LoggerFactory


class WordCountMaster extends Actor with ActorLogging {

    val logger = LoggerFactory.getLogger(this.getClass)
    private[this] var urlCount: Int = _
    private[this] var sortedCount: Seq[(String, Int)] = Nil

    def receive = {
        case StartCounting(urls, numActors) =>
            val workers = createWorkers(numActors)
            urlCount = urls.size
            beginSorting(urls, workers)
            //this is sync log
            logger.debug("WordCountMaster send to worker done", new Throwable("StartCounting"))
            //this is async log, not support stacktrace on the fly.
            log.debug("WordCountMaster send to worker done\n\t" + new Throwable("StartCounting").getStackTrace.mkString("\n\t"))

        case WordCount(url, count) =>
            log.debug(s" ${url} -> ${count}")
            sortedCount = sortedCount :+(url, count)
            sortedCount = sortedCount.sortWith(_._2 < _._2)
            if (sortedCount.size == urlCount) {
                log.debug("final result:" + sortedCount, new Throwable("WordCount"))
                finishSorting()
            }
    }

    override def postStop(): Unit = {
        log.debug(s"Master actor is stopped: ${self}")
    }

    private def createWorkers(numActors: Int) = {
        for (i <- 0 until numActors) yield context.actorOf(Props[WordCountWorker], name = s"worker-${i}")
    }

    private[this] def beginSorting(fileNames: Seq[String], workers: Seq[ActorRef]) {
        fileNames.zipWithIndex.foreach(e => {
            workers(e._2 % workers.size) ! FileToCount(e._1)
        })
    }

    private[this] def finishSorting() {
        context.system.terminate()
    }
}