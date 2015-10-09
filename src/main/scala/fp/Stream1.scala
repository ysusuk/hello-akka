package fp

/**
 * Created by iurii.susuk on 09.10.2015.
 */
object Stream1 {

  def empty[A]: Stream1[A] =
    new Stream1[A] {
      def uncons = None

      def toList = List.empty
    }

  def cons[A](hd: => A, tl: => Stream1[A]): Stream1[A] =
    new Stream1[A] {
      lazy val uncons = Some((hd, tl))

      def toList = {
        def build(stream: Stream1[A]): List[A] = {
          if (stream.uncons == None)
            List.empty[A]
          else stream.uncons.get._1 :: build(stream.uncons.get._2)
        }

        build(this)
      }
    }

  def apply[A](as: A*): Stream1[A] =
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))
}

trait Stream1[+A] {
  def uncons: Option[(A, Stream1[A])]

  def isEmpty: Boolean = uncons.isEmpty

  def toList: List[A]

//  def take(n: Int): Stream1[A]

  // def takeWhile(p: A => Boolean): Stream[A]
}