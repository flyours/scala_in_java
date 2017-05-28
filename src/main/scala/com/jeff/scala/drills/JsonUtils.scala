package com.jeff.scala.drills


import com.fasterxml.jackson.databind.JsonNode
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}
import org.json4s.{DefaultFormats, Formats, JValue}

import scala.reflect.Manifest


object JsonUtils {
    implicit lazy val formats = DefaultFormats

    def parseToJValue(data: String): JValue = fromJsonNode(mapper.readTree(data))

    def parseToJsonNode(data: String): JsonNode = mapper.readTree(data)

    def toJsonNode(data: Any): JsonNode = mapper.valueToTree(data)

    def jsonRead[A](data: String)(implicit formats: Formats, mf: Manifest[A]): A = read(data)

    def jsonWrite[A <: AnyRef](a: A)(implicit formats: Formats):String = write(a)

    def getJsonOption(data: JsonNode): String = Option(data).map(_.asText).filter(_ != "null").orNull

    def getJsonOption(data: JValue): String = data.extractOpt[String].filter(_ != "null").orNull
}
