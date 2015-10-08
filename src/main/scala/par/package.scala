package object par {

  def sum(as: IndexedSeq[Int]): Int =
    if (as.size <= 1) as.headOption getOrElse 0
    else {
      val (l, r) = as.splitAt(as.length / 2)
      sum(l) + sum(r)
    }

  def sum1(as: IndexedSeq[Int]): Int =
    if (as.size <= 1) as.headOption getOrElse 0
    else {
      val (l, r) = as.splitAt(as.length / 2)
      val leftPar: Par[Int] = Par.unit(sum1(l))
      val rightPar: Par[Int] = Par.unit(sum1(r))
      Par.run(leftPar) + Par.run(rightPar)
    }

  def sum2(as: IndexedSeq[Int]): Par[Int] =
    if (as.isEmpty) Par.unit(0)
    else {
      val (l, r) = as.splitAt(as.length / 2)
      Par.map2(Par.fork(sum2(l)), Par.fork(sum2(r)))(_ + _)
    }
}