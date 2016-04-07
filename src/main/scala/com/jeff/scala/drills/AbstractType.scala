package com.jeff.scala.drills

trait Calculator {
    type S

    def initialize: S

    def close(s: S): Unit

    def calculate(productId: String): Double = {
        val s = initialize
        val price = calculate(s, productId)
        close(s)
        price
    }

    def calculate(s: S, productId: String): Double
}

object CostPlusCalculator extends Calculator {
    type S = MongoClient

    def initialize = new MongoClient

    def close(dao: MongoClient) = dao.close

    def calculate(source: MongoClient, productId: String) = {
        0.0
    }
}

class MongoClient {
    def close = {}
}

object Main3 extends App {
    val costPlusCalculator = CostPlusCalculator
    val res = costPlusCalculator.calculate("jeff zhang")
    println(res)
}