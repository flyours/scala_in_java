package com.jeff.scala

import org.slf4j.LoggerFactory

trait Persistable[T] {
  def log(m: String) = {
    new DualLogger().log(m)
  }

  def getEntity: T

  def save(): T = {
    persistToDb(getEntity)
    log("save done")
    getEntity
  }

  private def persistToDb(t: T) = {}
}


trait RemoteLogger extends java.rmi.Remote {
  @throws(classOf[java.rmi.RemoteException])
  def log(m: String)
}

class PersistableImpl extends Persistable[PersistableImpl] {
  override def getEntity = this
}

object Main {
  val logger = LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
    new PersistableImpl().save()
    logger.debug("save done")
  }
}