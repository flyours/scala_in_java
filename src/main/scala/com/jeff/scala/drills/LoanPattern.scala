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

        //use anonymous function for second param
        func(MyResource("resource")) {
            r => logger.debug("in processing {}", r)
        }

        logger.debug("="*60)
        //above equals to below, which is function literal
        func(MyResource("resource")) {
            logger.debug("in processing {}", _)
        }

        logger.debug("="*60)
        //() is same with {}
        func(MyResource("resource")) (
            r => logger.debug("in processing {}", r)
        )

        val mylist=List((2,4),("6","test"))
        logger.debug("last value="+mylist(1)+" class="+mylist.getClass)
    }
}