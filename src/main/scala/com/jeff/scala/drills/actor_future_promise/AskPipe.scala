package com.jeff.scala.drills.actor_future_promise

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._


object AskPipe extends App {

    implicit val timeout = Timeout(5000.seconds)

    class GreetingsActor extends Actor {
        val messageActor = context.actorOf(Props[GreetingsChildActor])

        override def receive = {
            case name =>
                val f: Future[String] = (messageActor ? name).mapTo[String]
                f pipeTo sender
        }
    }

    class GreetingsChildActor extends Actor {
        override def receive = {
            case name =>
                val now = System.currentTimeMillis
                Thread.sleep(5000)
                if (now % 2 == 0)
                    sender ! "Hey " + name
                else
                    sender ! "Hello " + name
        }
    }

    val actorSystem = ActorSystem("askPipeSystem")

    val actor = actorSystem.actorOf(Props[GreetingsActor])

    val response: Future[String] = (actor ? "Nilanjan").mapTo[String]
    response.onComplete { e =>
        println(e)
        actorSystem.terminate()
    }

}