package com.jeff.scala.drills

import java.io.File
import java.util.Date

import org.slf4j.LoggerFactory

import scala.annotation.tailrec
import scala.io.{Source, StdIn}
import scala.language.dynamics
import scala.util.{Failure, Success, Try}

object ScalaDrills {
    val logger = LoggerFactory.getLogger(ScalaDrills.getClass)

    def main(args: Array[String]): Unit = {

        var count = Source.fromFile("/Users/twer/play.log").getLines().map(_ => 1).sum
        logger.debug("count={}}", count)

        paramDefine()
        dynamicTest()
        lazyTest()
        xmlTest()
        functionLiteralAndClosure()
        infixOperator()
        classConstructor()
        scriptInvoke()
        factoryPattern()
        importAndObject()
        caseClass()
        forComprehension()
        modifierAndScope()
        valueClass()
        traitStack()
        implicitFunction()
        implicitClass()
        covariantAndContravariant()
        higherOrderFunction()

        funcCurried()


        //if define don't have (),then the invoke can't have ();
        // if define have (),then invoke may or not have (); better have means having side effect
        //
        scalaTry
    }

    def paramDefine(): Unit = {
        /**
          *
          * @param cond , this define will evaluate the value every time(Call By Name), otherwise , only first time.
          * @param body , this define is one closure, only support {} or ({})
          */
        def loopTill(cond: => Boolean)(body: => Unit): Unit = {
            if (cond) {
                body
                loopTill(cond)(body)
            }
        }
        var i = 10
        loopTill(i > 0) {
            logger.debug("i={}", i)
            i -= 1
        }
    }

    def dynamicTest(): Unit = {
        //miss method feature
        class MyMap extends Dynamic {
            private val map = Map("foo" -> "1a", "bar" -> "2")

            def selectDynamic(field: String) = {
                map.get(field)
            }

        }
        val someMap = new MyMap
        logger.debug("value={}", someMap.foo.get)
        logger.debug("value={}", someMap.bar.get)
    }

    def lazyTest(): Unit = {
        var name = "jeff"
        lazy val n2 = s"my name is $name"
        name = "donna"
        logger.debug(n2)
    }

    def xmlTest(): Unit = {
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
    }

    def functionLiteralAndClosure(): Unit = {
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

        //closure should use {} or ({})
        breakable {
            val env = System.getenv("ABC")
            if (env == null) break
            println("found abc")
        }

    }

    def infixOperator(): Unit = {
        case class MyCase(name: String, age: Int) {
            def mylog(m: String): Unit = {
                logger.debug(m)
            }
        }

        val mycase = MyCase("jeff", 90)
        logger.debug(mycase.toString)
        logger.debug("mycase.name={},mycase.age={}", mycase.name, mycase.age)
        mycase match {
            case MyCase(n, a) => logger.debug("pattern match mycase.name={},mycase.age={}", n, a)
            case _ =>
        }
        //using object infix operator, ordinary function can not using this way, infix is replacing . operator
        mycase mylog "jeff zhang again"
    }

    def classConstructor(): Unit = {
        //if no var or val exists, args will be private , can not access outside the class define
        class Client(val host: String, val port: Int) {
            def this() = this("localhost", 9999)

            override def toString: String = {
                "host=" + host + ";port=" + port
            }
        }

        val client = new Client("local", 9090)
        logger.debug(client.toString)
        val client2 = new Client
        logger.debug(client2.toString)
    }

    def scriptInvoke(): Unit = {
        //will invoke script code as in default constructor
        class MyScript(host: String) {
            require(host != null, "Have to provide host name")
            if (host == "127.0.0.1") logger.debug("host = localhost") else logger.debug("host = " + host)
        }

        //    val script=new MyScript(null)
        val script2 = new MyScript("jeff")
    }

    def factoryPattern(): Unit = {
        //factory pattern in scala
        trait Role {
            def canAccess(page: String): Boolean
        }
        class Root extends Role {
            override def canAccess(page: String) = true
        }
        class SuperAnalyst extends Role {
            override def canAccess(page: String) = page != "Admin"
        }
        class Analyst extends Role {
            override def canAccess(page: String) = false
        }
        object Role {
            def apply(roleName: String) = roleName match {
                case "root" => new Root
                case "superAnalyst" => new SuperAnalyst
                case "analyst" => new Analyst
            }
        }

        val root = Role("root")
        val analyst = Role("analyst")
        logger.debug("root class:{}", root.getClass)
        logger.debug("analyst class:{}", analyst.getClass)
    }

    def importAndObject(): Unit = {
        import java.util._

        import scala.collection.convert.Wrappers._
        //import can export static(java) members and sub package, different with java import
        import java.util.Arrays._
        //private location!!!
        class DB private(val underlying: String) {

            override def toString = underlying

            def collecionNames = for (name <- new JSetWrapper[String](new TreeSet(asList("abc", "efg")))) yield name
        }
        object DB {
            def apply(underlying: String) = new DB(underlying)
        }

        var db = DB("ok ,new db")
        logger.debug(db.toString)
        logger.debug(db.collecionNames.getClass.toString)

        for (name <- db.collecionNames) logger.debug(name)

        object MyObject {
            val name = "jeff"
            val age = 90

            def apply(): Unit = {
                logger.debug(name + age)
            }

            def apply(m: String) = {
                name + " ; " + age
            }
        }
        MyObject()
        logger.debug(MyObject("donna"))
    }

    def caseClass(): Unit = {
        case class DBObject()

        sealed trait QueryOption
        case object NoOption extends QueryOption
        case class Sort(sorting: DBObject, anotherOption: QueryOption) extends QueryOption
        case class Skip(number: Int, anotherOption: QueryOption) extends QueryOption
        case class Limit(limit: Int, anotherOption: QueryOption) extends QueryOption


        case class Query(q: DBObject, option: QueryOption = NoOption) {
            def sort(sorting: DBObject) = Query(q, Sort(sorting, option))

            def skip(skip: Int) = Query(q, Skip(skip, option))

            def limit(limit: Int) = Query(q, Limit(limit, option))
        }

        var richQuery = Query(DBObject()).skip(20).limit(10)

        case class DBCursor() {
            def skip(op: Int) = new DBCursor

            def sort(op: DBObject) = new DBCursor

            def limit(op: Int) = new DBCursor

        }

        def applyOptions(cursor: DBCursor, option: QueryOption): DBCursor = {
            logger.debug("applyOptions : " + option)
            option match {
                case Skip(skip, next) => applyOptions(cursor.skip(skip), next)
                case Sort(sorting, next) => applyOptions(cursor.sort(sorting), next)
                case Limit(limit, next) => applyOptions(cursor.limit(limit), next)
                case NoOption => cursor
            }
        }

        //named arguments and copy method to create modified version
        val skipOption = Skip(10, NoOption)
        val skipWithLimit = skipOption.copy(anotherOption = Limit(10, NoOption))

        if (Skip(10, NoOption) == Skip(10, NoOption).copy()) {
            logger.debug("In Scala, invoking the == method is the same as calling the equals method")
        }



        applyOptions(new DBCursor, richQuery.option)
    }

    def forComprehension(): Unit = {
        val forVal = for (i <- 1 to 10) yield i

        logger.debug("for comprehension forVal type={}, size={}", forVal.getClass, forVal.size)

        def listFile: Unit = {
            val files = new File(".").listFiles
            for {
                file <- files
                fileName = file.getName
                if (fileName.endsWith(".md"))
            } logger.debug(fileName)
        }

        listFile
    }


    def modifierAndScope(): Unit = {
        /**
          * 1. default public -- no public keyword
          * 2. private/protected can be qualified by package/class/this(means this instance), default class if no qualify
          * 3. sealed, classes marked sealed can be overridden as long as the subclasses belong to the same source file.
          */
        class Outer {

            class Inner {
                private[Outer] def f() = "This is f"

                private[jeff] def g() = "This is g"

                private[drills] def h() = "This is h"
            }

        }
    }

    def valueClass(): Unit = {
        //value class
        //Value classes are a new mechanism to avoid runtime allocation of the objects.
        val w = ValueClass("Hey")
        w.p()
        logger.debug(w.up())
    }

    def traitStack(): Unit = {
        trait MyTest {
            def one: String
        }

        trait MyTest2 extends MyTest {
            abstract override def one: String = super.one + " MyTest2"
        }

        class MyTestImpl(name: String) extends MyTest {
            override def one: String = "MyTestImpl:" + name
        }

        val test = new MyTestImpl("jeff")
        logger.debug(test.one)

        val test2 = new MyTestImpl("jeff") with MyTest2
        logger.debug(test2.one)
    }

    def implicitFunction(): Unit = {
        //below not defined
        //val oneTo10 = 1 --> 10
        class RangeMaker(left: Int) {
            def -->(right: Int) = left to right
        }

        implicit def int2RangeMaker(left: Int): RangeMaker = new RangeMaker(left)
        val oneTo10 = 1 --> 10

        logger.debug(oneTo10.toString)

    }

    def implicitClass(): Unit = {
        implicit class RangeMaker(val left: Int) {
            def -->(right: Int) = left to right
        }
        val oneTo10 = 1 --> 10

        logger.debug(oneTo10.toString)

    }

    def covariantAndContravariant(): Unit = {
        //mutable has to be invariant

        def position[A](xs: List[A], value: A): Option[Int] = {
            val index = xs.indexOf(value)
            if (index != -1) Some(index) else None
        }

        logger.debug("position:{}", position(List(1, 2, 4, 6), 4))
        logger.debug("position:{}", position(List(1, 2, 4, 6), 5))


        sealed trait Maybe[+A] {
            def isEmpty: Boolean

            //contravariant can not used as return
            def get: A

            //covariant can not used as parameter
            //has to add type bound
            def getOrElse[B >: A](default: B): B = {
                if (isEmpty) default else get
            }
        }

        final case class Just[A](value: A) extends Maybe[A] {
            override def isEmpty: Boolean = false

            override def get: A = value
        }

        case object Nill extends Maybe[Nothing] {
            override def isEmpty: Boolean = true

            override def get: Nothing = throw new NoSuchElementException("Nill.get")
        }

        def position2[A](xs: List[A], value: A): Maybe[Int] = {
            val index = xs.indexOf(value)
            //Maybe[int] can be assigned to Maybe[Nothing] as Maybe is covariant.
            if (index != -1) Just(index) else Nill
        }

        logger.debug("position2:{}", position2(List(1, 2, 4, 6), 4))
        logger.debug("position2:{}", position2(List(1, 2, 4, 6), 5))

    }

    def higherOrderFunction(): Unit = {
        val forVal = for {i <- 1 to 10} yield i
        //using anonymous function
        logger.debug(forVal.map { x => x + 1 }.toString())
        //using function literal
        logger.debug(forVal.map {
            _ + 100
        }.toString())

        def addTen(num: Int) = {
            def ++(x: Int) = x + 10
            ++(num)
        }
        //using call by name
        logger.debug(forVal.map(addTen).toString())
    }

    @tailrec
    def scalaTry: Try[Int] = {
        val dividend = Try(StdIn.readLine("Enter an Int that you'd like to divide:\n").toInt)
        val divisor = Try(StdIn.readLine("Enter an Int that you'd like to divide by:\n").toInt)
        val problem = dividend.flatMap(x => divisor.map(y => x / y))
        problem match {
            case Success(v) =>
                logger.debug("Result of " + dividend.get + "/" + divisor.get + " is: " + v)
                Success(v)
            case Failure(e) =>
                logger.debug("You must've divided by zero or entered something that's not an Int. Try again!")
                logger.debug("Info from the exception: " + e.getMessage)
                scalaTry
        }
    }


    def funcCurried()={
        import scala.math._


        def callLog(func: Double=>Double,num: Int)=func(num)

        //turn method to function
        val oneLog=callLog _

        def twoLog=callLog _

        logger.debug("curried value={}",oneLog.curried(log10 _)(1000))
        logger.debug("curried value={}",twoLog.curried(log10 _) (10000))
        logger.debug("curried value={}",(callLog _).curried(log10 _) (10000))
    }

}

trait Printable extends Any {
    def p() = {
        val logger = LoggerFactory.getLogger("Printable")
        logger.debug("this:{}", this)
    }
}

/**
  * can not be defined locally!!!
  *
  *  The class must have exactly one val parameter (vars are not allowed).
  *  The parameter type may not be a value class.
  *  The class can not have any auxiliary constructors.
  *  The class can only have def members, no vals or vars.
  *  The class cannot extend any traits, only universal traits (extends from Any).
  */
case class ValueClass(name: String) extends AnyVal with Printable {
    def up() = name.toUpperCase
}


