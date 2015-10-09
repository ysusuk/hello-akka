package check

import check.Prop._
import fp._

/**
 * Created by iurii.susuk on 09.10.2015.
 */
case class Gen[+A](sample: State[RNG, A], exhaustive: Stream1[A])

object Gen {
  def unit[A](a: => A): Gen[A] = new Gen(State.unit(a), Stream1.empty)

  def boolean: Gen[Boolean] = Gen(State(RNG.boolean), Stream1.empty[Boolean])

  def choose(start: Int, stopExclusive: Int): Gen[Int] = Gen(State(RNG.nonNegativeInt), Stream1.empty[Int])

  /** Between 0 and 1, not including 1. */
  def uniform: Gen[Double] = ???

  /** Between `i` and `j`, not including `j`. */
  def choose(i: Double, j: Double): Gen[Double] = ???
}

object Prop {
  type FailedCase = String
  type SuccessCount = Int

  def listOf[A](a: Gen[A]): Gen[List[A]] = ???

  def forAll[A](a: Gen[A])(f: A => Boolean): Prop = ???

  def listOfN[A](n: Int, a: Gen[A]): Gen[List[A]] = ???
}

trait Prop {
  def check: Either[FailedCase, SuccessCount]

  def &&(p: Prop): Prop = new Prop {
    def check = Prop.this.check match {
      case Left(s) => Left(s)
      case Right(leftN) => p.check match {
        case Left(s) => Left(s)
        case Right(rightN) => Right(leftN + rightN)
      }
    }
  }

}

