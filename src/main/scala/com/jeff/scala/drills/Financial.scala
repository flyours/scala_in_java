package com.jeff.scala.drills

object Financial {
    def main(args: Array[String]) {
        val results = for (line <- args; oneNum = java.lang.Float.parseFloat(line)) yield oneNum
        val sum = results.sum
        println(args.mkString("+"))
        println("total Number: "+results.length)
        println("         sum: " + sum)
    }
}
