package check

import check.Prop._
import fp._

/**
 * Created by iurii.susuk on 09.10.2015.
 */
case class Gen[+A](sample: State[RNG, A], exhaustive: Stream1[Option[A]])

object Gen {
  def unit[A](a: => A): Gen[A] = new Gen(State.unit(a), Stream1.empty)

  def boolean: Gen[Boolean] = Gen(State(RNG.boolean), Stream1.empty[Boolean])

  def choose(start: Int, stopExclusive: Int): Gen[Int] = Gen(State(RNG.nonNegativeInt), Stream1.empty[Int])

  /** Between 0 and 1, not including 1. */
  val uniform: Gen[Double] = Gen(State(RNG.double), unbounded)

  /** Between `i` and `j`, not including `j`. */
  def choose(i: Double, j: Double): Gen[Double] = ???

  type Domain[+A] = Stream[Option[A]]

  def bounded[A](a: Stream[A]): Domain[A] = a map (Some(_))

  def unbounded: Domain[Nothing] = Stream(None)
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

