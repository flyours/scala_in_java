package drills

import java.io.File
import java.util.Date

import org.slf4j.LoggerFactory

import scala.io.Source
import scala.language.dynamics


object Scala1 {
  val logger = LoggerFactory.getLogger(Scala1.getClass)

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
      logger.debug("i={}", i)
      i -= 1
    }

    var person = new Person("jeff", 10000)
    logger.debug("person={}", person)

    var count = Source.fromFile("/Users/twer/play.log").getLines().map(_ => 1).sum
    logger.debug("count={}}", count)

    val someMap = new MyMap
    //    logger.debug("value={}", someMap.foo.get)
    //    logger.debug("value={}", someMap.bar.get)

    var name = "jeff"
    lazy val n2 = s"my name is $name"
    name = "donna"
    logger.debug(n2)

    val book = <book>
      <title>
        scala in action
      </title>
      <author>
        jeff zhang
      </author>
      <date>
        {new Date}
      </date>
    </book>

    logger.debug(book.toString)

    val evenNumbers = List(2, 4, 6, 8, 10)
    logger.debug("sum={}", evenNumbers.sum)

    //function literal, both () and {} are ok
    logger.debug("sum2={}", evenNumbers.foldLeft(0) {
      _ + _
    })


    val breakException = new RuntimeException("test")

    def breakable(op: => Unit): Unit = {
      try {
        op
      } catch {
        case e: Exception => logger.error("catch {}", e)
      }
    }
    def break = throw breakException

    //closure should use {}
    breakable {
      val env = System.getenv("ABC")
      if (env == null) break
      println("found abc")
    }

    def listFile: Unit = {
      val files = new File(".").listFiles
      for {
        file <- files
        fileName = file.getName
        if (fileName.endsWith(".md"))
      } println(fileName)
    }

    listFile


  }
}
