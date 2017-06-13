package com.jeff.scala.drills

import com.jeff.scala.drills.JsonUtils._
import org.json4s.JsonAST.{JNull, JString}
import org.json4s.{CustomSerializer, DefaultFormats}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}

import scala.util.{Failure, Success, Try}


/**
  * Created by jeff on 26/05/2017.
  */
class JsonUtilsTest extends FunSuite with ScalaFutures with Matchers {

  case object TrySerializer extends CustomSerializer[Try[String]](format => ( {
    case JString(s) => Success(s)
    case JNull => Failure(new Exception("null"))
  }, {
    case Success(s: String) => JString(s)
    case Failure(e) => JNull
  })
  )

  implicit lazy val formats = DefaultFormats + TrySerializer

  case class MyTest(o: Option[String], t: Try[String])

  test("json string serialize") {
    jsonSerail
  }

  private def jsonSerail = {
    val t = MyTest(Some("jeff"), Try("ok"))
    assert(t.o.contains("jeff"))

    val jsonString = jsonWrite(t)
    jsonString should be("{\"o\":\"jeff\",\"t\":\"ok\"}")

    val tt = jsonRead[MyTest](jsonString)

    println(t)
    println(tt)
    assert(t != tt)
  }

  test("json case class converter") {
    jsonCaseClassConverter
  }

  private def jsonCaseClassConverter = {
    import org.json4s.Extraction._

    val t = MyTest(Some("jeff"), Try("ok"))

    val jt = decompose(t)

    val tString = (jt \ "t").extract[String]
    tString should be("ok")

    val oString = (jt \ "o").extractOrElse[String]("option")
    oString should be("jeff")

    val tt=jt.extract[MyTest]
    assert(t!=tt)
  }

  test("json dsl test") {
    jsonDSLTest
  }

  private def jsonDSLTest = {
    import org.json4s.JsonDSL._
    import org.json4s._
    import org.json4s.jackson.JsonMethods._

    val json =
      ("person" ->
        ("name" -> "Joe") ~
          ("age" -> 35) ~
          ("age" -> 11) ~
          ("spouse" ->
            ("person" ->
              ("name" -> "Marilyn") ~
                ("age" -> 33)
              )
            )
        )

    println(pretty(render(json)))
    println(compact(render(json)))

    // render(json) can be used to paring single tuple to JValue
    val jValue = render(json)
    assert(jValue.isInstanceOf[JObject])

    val jObject= jValue.asInstanceOf[JObject]

    // \\ will return JObject, \ will return JArray or JValue
    assert(((jObject\\"age")\"age").extract[List[Int]] == List(35,11,33))
    assert(((jObject\"person")\"age").extract[List[Int]] == List(35,11))
    assert(((jObject\"person")\"name").extract[String] == "Joe")
  }

}
