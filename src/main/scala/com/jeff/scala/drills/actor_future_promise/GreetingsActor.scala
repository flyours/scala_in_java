package com.jeff.scala.drills.actor_future_promise

object GreetingsActor extends App {

    import akka.actor.{Actor, ActorSystem, Props}

    case class Name(name: String)

    class GreetingsActor extends Actor {
        def receive = {
            case Name(n) => println("Hello " + n)
        }
    }

    val system = ActorSystem("greetings")
    val a = system.actorOf(Props[GreetingsActor], name = "greetings-actor")

    a ! Name("Nilanjan")

    Thread.sleep(50)
    system.terminate()
}