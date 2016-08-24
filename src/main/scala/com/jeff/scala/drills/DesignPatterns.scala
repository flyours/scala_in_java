package com.jeff.scala.drills

import org.slf4j.LoggerFactory

object DesignPatterns {
    val logger = LoggerFactory.getLogger(DesignPatterns.getClass)

    //Stratgey pattern
    def calculatePrice(product: String, taxingStrategy: String => Double) = {
        taxingStrategy(product)
    }

    //DI in functional style
    trait TaxStrategy {
        def taxIt(product: String): Double
    }

    class ATaxStrategy extends TaxStrategy {
        def taxIt(product: String): Double = {
            logger.debug(product)
            10.0
        }
    }

    class BTaxStrategy extends TaxStrategy {
        def taxIt(product: String): Double = {
            logger.debug(product)
            20.0
        }
    }

    def taxIt: TaxStrategy => String => Double = s => p => s.taxIt(p)

    def taxIt_a = taxIt(new ATaxStrategy)

    def taxIt_b = taxIt(new BTaxStrategy)


    def main(args: Array[String]): Unit = {
        logger.debug("tax a={}", taxIt_a("taxIt_a"))
        logger.debug("tax a={}", taxIt_b("taxIt_b"))


        val taxIt_aa = taxIt(new ATaxStrategy)
        logger.debug("tax a={}", taxIt_aa("taxIt_aa"))
        logger.debug("tax a={}", taxIt_aa("taxIt_aaaa"))

        //no difference type for def and val, only def will evaluate every time, while val at the first time.
        logger.debug("tax a class={}", taxIt_a.getClass)
        logger.debug("tax a class={}", taxIt_aa.getClass)


        logger.debug("strategy ={}", calculatePrice("test", _.length))
        logger.debug("strategy ={}", calculatePrice("test", taxIt_a))

    }

}