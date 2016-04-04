package com.jeff.scala.drills

import org.slf4j.LoggerFactory


trait Resource {
    def dispose(): Unit
}

class UseResource {
    def use[A, B <: Resource](r: B)(f: B => A): A = {
        try {
            f(r)
        } finally {
            r.dispose()
        }
    }
}


object LoanPattern {
    val logger = LoggerFactory.getLogger(LoanPattern.getClass)

    def main(args: Array[String]): Unit = {
        case class MyResource(name: String) extends Resource {
            override def dispose(): Unit = logger.debug("disposed:{}", name)
        }
        //using eta expansion
        val func = new UseResource().use[Unit, MyResource] _
        func(MyResource("resource")) {
            r => logger.debug("in processing {}", r)
        }
    }
}