package com.jeff.scala.drills


object Financial {
    def main(args: Array[String]) {
        val sum=args.map(_.toFloat).sum
        println(args.mkString("+"))
        println("total Number: " + args.length)
        println("         sum: " + sum)
    }
}
