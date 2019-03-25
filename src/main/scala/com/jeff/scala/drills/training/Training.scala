package com.jeff.scala.drills.training


import scala.language.{implicitConversions, postfixOps}

object Training {


  case class Person(name: String, age: Int) {
    override def toString = s"$name ($age)"

    def getAlias = "jeff"

    def alias = () => "jeff"
  }

  def underagePeopleNames(persons: List[Person]) = for (person <- persons; if person.age < 18) yield person.name

  def createRandomPeople() = {
    import scala.util._
    val names = List("Alice", "Bob", "Carol", "Dave", "Eve", "Frank")
    for (name <- names) yield {
      val age = (Random.nextGaussian() * 8 + 20).toInt
      Person(name, age)
    }
  }

  var name = "jeff"

  def getAlias = s"jeff$name"


  def implicitFunction(): Unit = {
    //below not defined
    //val oneTo10 = 1 --> 10
    class RangeMaker(left: Int) {
      def -->(right: Int) = left to right
    }

    implicit def int2RangeMaker(left: Int): RangeMaker = new RangeMaker(left)

    val oneTo10 = 1 --> 10
  }

  def stringLengthPlusNum(s: String)(f: String => Int => Unit) = f(s)

  def plusNum(s: String): Int => Unit = {
    def plus(n: Int): Unit = {
      println(s"$s length= ${s.length} and n=$n")
    }

    plus
  }

  stringLengthPlusNum("sayHello")(plusNum)(8)

  def sum(n1: Int, n2: Int) = n1 + n2

  val l=1 to 10

  val ll=l.flatMap(one => l.map(one+_))

  for(one <- l; two <- l) yield one+two





  def main(args: Array[String]): Unit = {
    val p = Person("jeff", 11)
    val p2 = Person("zhang", 19)

    val persons = createRandomPeople()
    println(persons)

    val strings = underagePeopleNames(persons)
    println(strings)

  }
}
