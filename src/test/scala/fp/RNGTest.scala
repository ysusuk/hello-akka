package fp

import org.scalatest.Matchers._
import org.scalatest._

/**
 * Created by iurii.susuk on 07.10.2015.
 */
class RNGTest extends FlatSpec {

  "RNG" should "generate the same number" in {
    val rng = RNG.simple(1)
    val next = rng.nextInt._1
    val nextNext = rng.nextInt._1

    nextNext should be (next)
  }

  it should "generate int double" in {
    val rng = RNG.simple(1)

    println(RNG.intDouble(rng))
  }

  it should "generate double int" in {
    val rng = RNG.simple(1)

    println(RNG.doubleInt(rng))
  }

  it should "generate triple double" in {
    val rng = RNG.simple(1)

    println(RNG.double3(rng))
  }

  it should "generate list int of size 5" in {
    val rng = RNG.simple(1)

    println(RNG.ints(5)(rng))
  }

  it should "generate int" in {
    val rng = RNG.simple(1)

    println(RNG.int(rng))
  }

  it should "unit" in {
    val rng = RNG.simple(1)

    println(RNG.unit(1))
  }

  it should "map" in {
    val rng = RNG.simple(1)

    println(RNG.map(RNG.unit(1))((x: Int) => x.toString))
  }
}
