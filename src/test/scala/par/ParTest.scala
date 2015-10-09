package par

import org.scalatest.Matchers._
import org.scalatest._

import Par._

/**
 * Created by iurii.susuk on 08.10.2015.
 */
class ParTest extends FlatSpec {

  "sum" should "calc the sum" in {
    sum(IndexedSeq(1, 2, 3)) should be(6)
  }

  "sum1" should "calc the sum" in {
    sum1(IndexedSeq(1, 2, 3)) should be(6)
  }

  "map" should "combine calc" in {
    Par.run(exec)(Par.map2(exec)(Par.unit(1), Par.unit(2))(_ + _)).get should be(3)
  }

  "sum2" should "calc the sum" in {
    Par.run(exec)(sum2(IndexedSeq(1, 2, 3))).get should be(6)
  }

}
