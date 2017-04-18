package com.jeff.scala.drills

import scala.language.higherKinds

/**
  * Created by jeff on 18/04/2017.
  *
  * Scala can abstract over “higher kinded” types. For example,
  * suppose that you needed to use several types of containers for several types of data.
  * You might define a Container interface that might be implemented
  * by means of several container types: an Option, a List, etc.
  * You want to define an interface for using values in these containers
  * without nailing down the values’ type.
  *
  */
trait Container[M[_]] {
    def put[A](x: A): M[A]

    def get[A](m: M[A]): A
}

object AdhocHigherKinded extends App {
    val container = new Container[List] {
        def put[A](x: A) = List(x)

        def get[A](m: List[A]): A = m.head
    }

    // Note that Container is polymorphic in a parameterized type (“container type”).
    println(container.put("hey"))
    println(container.put(123))

    /**
      * If we combine using containers with implicits,
      * we get “ad-hoc” polymorphism: the ability to write generic functions over containers.
      */

    implicit val listContainer = new Container[List] {
        def put[A](x: A) = List(x)

        def get[A](m: List[A]): A = m.head
    }
    implicit val optionContainer = new Container[Some] {
        def put[A](x: A) = Some(x)

        def get[A](m: Some[A]): A = m.get
    }

    def tupleize[M[_] : Container, A, B](fst: M[A], snd: M[B]): M[(A, B)] = {
        val c = implicitly[Container[M]]
        c.put(c.get(fst), c.get(snd))
    }

    println(tupleize(Some(1), Some(2)))
    println(tupleize(List(1), List(2)))
    println(tupleize(List("abc"), List(2)))
    println(tupleize(Some("abc"), Some(2)))

    //multiple args will be viewed as tuple
    def tupleTest[A](a:A): Unit ={
        println(a)
    }

    tupleTest(1)
    tupleTest("adbc")
    tupleTest(1,"abc")
}
