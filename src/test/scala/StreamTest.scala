import org.scalatest.Matchers._
import org.scalatest._

/**
 * Created by iurii.susuk on 06.10.2015.
 */
class StreamTest extends FlatSpec {

  "stream" should "be empty" in {
    val emptyStream = Stream1.empty
    emptyStream.uncons should be(None)
  }

  it should "contain 1, 2, 3 elements and not 4" in {
    val nonEmptyStream = Stream1(1, 2, 3)
    nonEmptyStream.toList should contain only(1, 2, 3)
  }

  it should "map and filter" in {
    Stream(1, 2, 3, 4).map(_ + 10).filter(_ % 2 == 0).toList should contain only(12, 14)
  }

  it should "" in {
    val ones: Stream[Int] = Stream.continually(1)

    ones.take(3).toList should contain only(1)
    ones.take(3).toList.size should be (3)

  }
}
