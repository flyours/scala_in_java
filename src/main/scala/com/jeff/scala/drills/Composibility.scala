package com.jeff.scala.drills

import org.slf4j.LoggerFactory

object Composibility {
    val logger = LoggerFactory.getLogger(Composibility.getClass)

    def even: Int => Boolean = _ % 2 == 0

    def not: Boolean => Boolean = !_

    def filter[A](criteria: A => Boolean)(col: Traversable[A]): Traversable[A] = col.filter(criteria)

    def map[A, B](f: A => B)(col: Traversable[A]) = col.map(f)

    //convert to partial function
    def evenFilter: (Traversable[Int]) => Traversable[Int] = filter(even) _

    def double: Int => Int = _ * 2

    def doubleAllEven = evenFilter andThen map(double)

    def odd: Int => Boolean = not compose even

    def oddFilter = filter(odd) _

    def doubleAllOdd = oddFilter andThen map(double)

    def main(args: Array[String]): Unit = {
        logger.debug(doubleAllEven(Vector(1, 2, 5, 6, 7, 14)).toString)
        logger.debug(doubleAllOdd(Vector(1, 2, 5, 6, 7, 14)).toString)

        logger.debug("all odd={}", filter(even.andThen(not))(Vector(1, 2, 5, 6, 7, 14)))
        //infix usage
        logger.debug("all odd={}", filter(even andThen not)(Vector(1, 2, 5, 6, 7, 14)))

    }
}