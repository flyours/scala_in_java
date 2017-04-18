package com.jeff.scala.drills.typesystem

/*
 * This is the Towers of Hanoi example from the prolog tutorial [1]
 * converted into Scala, using implicits to unfold the algorithm at
 * compile-time.
 *
 * [1] http://www.csupomona.edu/~jrfisher/www/prolog_tutorial/2_3.html
 *
 * Scala 的类型系统是图灵完备(Turing Complete)的，即利用类型系统本身就可以解决一些问题。
 * 利用scala的类型系统解决汉诺塔： https://gist.github.com/jrudolph/66925
 *
 */

object TowersOfHanoi {

    import scala.reflect.Manifest

    def simpleName(m: Manifest[_]): String = {
        val name = m.toString
        name.substring(name.lastIndexOf('$') + 1)
    }

    trait Nat

    final class _0 extends Nat

    final class Succ[Pre <: Nat] extends Nat

    type _1 = Succ[_0]
    type _2 = Succ[_1]
    type _3 = Succ[_2]
    type _4 = Succ[_3]

    case class Move[N <: Nat, A, B, C]()

    implicit def move0[A, B, C](implicit a: Manifest[A], b: Manifest[B]): Move[_0, A, B, C] = {
        println("Move from " + simpleName(a) + " to " + simpleName(b))
        null
    }

    implicit def moveN[P <: Nat, A, B, C](implicit m1: Move[P, A, C, B], m2: Move[_0, A, B, C], m3: Move[P, C, B, A]): Move[Succ[P], A, B, C] = null

    def run[N <: Nat, A, B, C](implicit m: Move[N, A, B, C]) = null

    case class Left()

    case class Center()

    case class Right()

    def main(args: Array[String]) {
        run[_2, Left, Right, Center]
    }
}