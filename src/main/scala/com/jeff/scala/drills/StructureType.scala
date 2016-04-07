package com.jeff.scala.drills

import java.util.Date

import org.slf4j.LoggerFactory


object StructureType extends App {
    val logger = LoggerFactory.getLogger(StructureType.getClass)

    trait SalariedWorker {
        def salary: BigDecimal
    }

    trait Worker extends SalariedWorker {
        def bonusPercentage: Double
    }

    trait HourlyWorker extends SalariedWorker {
        def hours: Int
    }

    case class FullTimeWorker(salary: BigDecimal, bonusPercentage: Double) extends Worker

    case class PartTimeWorker(hours: Int, salary: BigDecimal) extends HourlyWorker

    case class StudentWorker(hours: Int, salary: BigDecimal) extends HourlyWorker

    def amountPaidAsSalary(workers: Vector[SalariedWorker]) = {
        logger.debug("workers: {}", workers)
    }

    def amountPaidAsSalary2(workers: Vector[ {def salary: BigDecimal}]) = {
        logger.debug("workers: {}", workers)
        def getProfile(profile: Profile): Profile = {
            logger.debug("profile= {}", profile.name)
            profile
        }
        case class MyProfile(name: String, dob: Date)
        logger.debug("profile = {}", getProfile(MyProfile("jeff", new Date)))
    }

    //can not new, can only used as type reference.
    type Profile = {
        def name: String
        def dob: Date
    }

    def run() {
        amountPaidAsSalary(Vector(PartTimeWorker(5, 4000.50), StudentWorker(2, 100.20)))
        amountPaidAsSalary2(Vector(PartTimeWorker(5, 4000.50), StudentWorker(2, 100.20)))
    }

    run()
}
