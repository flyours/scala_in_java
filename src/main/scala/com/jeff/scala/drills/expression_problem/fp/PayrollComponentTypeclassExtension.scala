package com.jeff.scala.drills.expression_problem.fp


object PayrollSystemWithTypeclassExtension {

    import PayrollSystemWithTypeclass._

    case class JapanPayroll[A](payees: Vector[A])(implicit processor: PayrollProcessor[JapanPayroll, A]) {
        def processPayroll = processor.processPayroll(payees)
    }

    case class Contractor(name: String)

}

object PayrollProcessorsExtension {

    import PayrollSystemWithTypeclass._
    import PayrollSystemWithTypeclassExtension._

    implicit object JapanPayrollProcessor extends PayrollProcessor[JapanPayroll, Employee] {
        def processPayroll(payees: Seq[Employee]) = Left("japan employees are processed")
    }

    implicit object USContractorPayrollProcessor
            extends PayrollProcessor[USPayroll, Contractor] {
        def processPayroll(payees: Seq[Contractor]) = Left("us contractors are processed")
    }

    implicit object CanadaContractorPayrollProcessor
            extends PayrollProcessor[CanadaPayroll, Contractor] {
        def processPayroll(payees: Seq[Contractor]) = Left("canada contractors are processed")
    }

    implicit object JapanContractorPayrollProcessor
            extends PayrollProcessor[JapanPayroll, Contractor] {
        def processPayroll(payees: Seq[Contractor]) = Left("japan contractors are processed")
    }

}


object RunNewPayroll {

    import PayrollProcessorsExtension._
    import PayrollSystemWithTypeclass._
    import PayrollSystemWithTypeclassExtension._

    def main(args: Array[String]): Unit = run

    def run = {
        val r1 = JapanPayroll(Vector(Employee("a", 1))).processPayroll
        val r2 = JapanPayroll(Vector(Contractor("a"))).processPayroll
        println(r1)
        println(r2)
    }
}