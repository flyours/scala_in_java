package com.jeff.scala.drills

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import com.fasterxml.jackson.databind.JsonNode
import com.jeff.scala.drills.UserProtocol.LogOut


object UserProtocol {

    case class LogIn(json: JsonNode)

    case class LogOut(mmeToken: String)

    case class IsSessionTimeout(mmeToken: String)

    case class Register(json: JsonNode)

    case class GetTwoStepToggle()

    case class ResetPassword(json: JsonNode)

    case class ResetPasswordWithOld(json: JsonNode, mmeToken: String)

    case class VerifyMobile(json: JsonNode)

    case class CreateCaptcha(action: String, json: JsonNode)

    case class ActorResult(code: Int, json: JsonNode = null, mmeToken: String = null)

}

object SerialMain extends App{
    val out=new ObjectOutputStream(new FileOutputStream("logout.bin"))
    out.writeObject(LogOut("abcd1234"))
    out.flush()

    val oi=new ObjectInputStream(new FileInputStream("logout.bin2"))
    val lo=oi.readObject().asInstanceOf[LogOut]
    println(lo)
}