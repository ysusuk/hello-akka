package check

import org.scalatest.FlatSpec

/**
 * Created by iurii.susuk on 09.10.2015.
 */
class PropertyBasedCheckTest extends FlatSpec {

  "gen" should "put elements in option" in {
    println(Gen.bounded(Stream(1, 2, 3)))
  }

  "gen" should "put none" in {
    println(Gen.unbounded)
  }
}
