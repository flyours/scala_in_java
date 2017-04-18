package com.jeff.scala.drills

/**
  * Created by jeff on 18/04/2017.
  * F-bounded quantification or recursively bounded quantification, introduced in 1989,
  * allows for more precise typing of functions that are applied on recursive types.
  *
  * Often it’s necessary to access a concrete subclass in a (generic) trait.
  * For example, imagine you had some trait that is generic,
  * but can be compared to a particular subclass of that trait.
  */

trait Container2[A <: Container2[A]] extends Ordered[A]

case class MyContainer(value: Int, name: String) extends Container2[MyContainer] {
    def compare(that: MyContainer): Int = value - that.value
}

object FBoundedPolymorphism extends App {
    val values = List(MyContainer(2, "zhang"), MyContainer(1, "dong"), MyContainer(0, "yang"), MyContainer(0, "du"))
    println(values.sorted)
    println(values.min)

    //sort by combined Ordering
    println(values.sortBy(x => (x.value, x.name)))
    println(values.sortBy(_.name))
}
