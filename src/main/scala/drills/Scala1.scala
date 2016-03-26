package drills

import scala.io.Source
import scala.language.dynamics


object Scala1 {

  /**
    *
    * @param cond , this define will evaluate the value every time, otherwise , only first time.
    * @param body
    */
  def loopTill(cond: => Boolean)(body: => Unit): Unit = {
    if (cond) {
      body
      loopTill(cond)(body)
    }
  }

  class Person(var name: String, var salary: Int) {
    override def toString = {
      name + ":" + salary
    }
  }

  //miss method feature
  class MyMap extends Dynamic {
    private val map = Map("foo" -> "1a", "bar" -> 2)

    def selectDynamic(field: String) = {
      map.get(field)
    }

  }

  def main(args: Array[String]): Unit = {
    var i = 10
    loopTill(i > 0) {
      println(i)
      i -= 1
    }

    var person = new Person("jeff", 10000)
    println(person)

    var count = Source.fromFile("/Users/twer/play.log").getLines().map(_ => 1).sum
    println(count)

    val someMap = new MyMap
    println(someMap.foo.get)
    println(someMap.bar.get)


  }
}
