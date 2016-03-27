package com.jeff.scala
import java.io._

import org.slf4j.LoggerFactory

trait Logger {
  def log(m: String)
}

trait ConsoleLogger extends Logger {
  val logger = LoggerFactory.getLogger("ConsoleLogger")

  def log(m: String) = logger.debug(m)
}

trait DecorateLogger extends Logger {
  abstract override def log(m: String) = {
    super.log("-" * 20)
    super.log(m)
    super.log("-" * 20)
  }
}

trait WireTapLogger extends Logger {
  def otherSource: OutputStream 
  abstract override def log(m: String) = {
    otherSource.write(m.getBytes)
    super.log(m)
  }
}

class DualLogger extends ConsoleLogger with DecorateLogger with WireTapLogger {
  def otherSource: OutputStream = new FileOutputStream(File.createTempFile("crazy-logger", "txt"))
}