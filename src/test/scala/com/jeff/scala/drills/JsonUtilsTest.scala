package com.jeff.scala.drills

import com.jeff.scala.drills.JsonUtils._
import org.json4s.JsonAST.{JNull, JString}
import org.json4s.{CustomSerializer, DefaultFormats}
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.util.{Failure, Success, Try}



/**
  * Created by jeff on 26/05/2017.
  */
class JsonUtilsTest extends FunSuite with ScalaFutures with Matchers{

  case object TrySerializer extends CustomSerializer[Try[String]](format => (
    {
      case JString(s) => Success(s)
      case JNull => Failure(new Exception("null"))
    },
    {
      case Success(s:String) => JString(s)
      case Failure(e) => JNull
    }
  )
  )

  implicit lazy val formats = DefaultFormats + TrySerializer

  case class MyTest(o: Option[String], t: Try[String])

  test("json serialize") {
    val t = MyTest(Some("jeff"), Try("ok"))
    assert(t.o.contains("jeff"))

    val jsonString = jsonWrite(t)
    jsonString should be("{\"o\":\"jeff\",\"t\":\"ok\"}")

    val tt=jsonRead[MyTest](jsonString)

    println(t)
    println(tt)
    assert(tt.equals(t))
  }

}
