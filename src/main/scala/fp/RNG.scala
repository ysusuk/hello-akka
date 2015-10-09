package fp

/**
 * Created by iurii.susuk on 07.10.2015.
 */
trait RNG {
  def nextInt: (Int, RNG)
}

object RNG {
  def unit[A](a: A): Rand[A] =
    rng => (a, rng)

  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  def map[A, B](s: Rand[A])(f: A => B): Rand[B] =
    rng => {
      val (a, rng2) = s(rng)
      (f(a), rng2)
    }

  def simple(seed: Long): RNG = new RNG {
    def nextInt = {
      val seed2 = (seed * 0x5DEECE66DL + 0xBL) &
        ((1L << 48) - 1)
      ((seed2 >>> 16).asInstanceOf[Int],
        simple(seed2))
    }
  }

  def positiveInt(rng: RNG): (Int, RNG) = {
    val (i, rng1) = rng.nextInt
    (i.abs, rng1)
  }

  def double(rng: RNG): (Double, RNG) = {
    val (i, rng1) = rng.nextInt
    (i.toDouble, rng1)
  }

  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (i1, rng1) = rng.nextInt
    val (i2, rng2) = double(rng1)
    ((i1, i2), rng2)
  }

  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val (i1, rng1) = double(rng)
    val (i2, rng2) = rng.nextInt
    ((i1, i2), rng2)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (i1, rng1) = double(rng)
    val (i2, rng2) = double(rng1)
    val (i3, rng3) = double(rng2)

    ((i1, i2, i3), rng3)
  }

  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    def build(list: List[Int], rng: RNG): (List[Int], RNG) = {
      if (list.size == count)
        (list, rng)
      else {
        val (i, rng1) = rng.nextInt
        build(i :: list, rng1)
      }
    }

    build(List.empty, rng)
  }

  def boolean(rng: RNG): (Boolean, RNG) =
    rng.nextInt match {
      case (i, rng2) => (i % 2 == 0, rng2)
    }

  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (i, r) = rng.nextInt
    (if (i < 0) -(i + 1) else i, r)
  }

}

