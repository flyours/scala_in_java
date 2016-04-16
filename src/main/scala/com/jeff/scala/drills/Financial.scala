package com.jeff.scala.drills

object Financial {
    def main(args: Array[String]) {
        val results = for (line <- args; oneNum = Integer.parseInt(line)) yield oneNum
        val sum = results.sum
        println(args.mkString("+"))
        println("=" + sum)
    }
}
