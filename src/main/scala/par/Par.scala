package par

import java.util.concurrent.{Callable, ForkJoinPool, ExecutorService, Future}

object Par {
  implicit val exec: ExecutorService = new ForkJoinPool()

  type Par[A] = ExecutorService => Future[A]

  def run[A](s: ExecutorService)(par: Par[A]): Future[A] = par(s)

  def unit[A](a: A): Par[A] = (s: ExecutorService) => s.submit(new Callable[A] {
    override def call(): A = a
  })

  def map[A,B](s: ExecutorService)(fa: Par[A])(f: A => B): Par[B] =
    map2(s)(fa, unit(()))((a,_) => f(a))

  def map2[A, B, C](s: ExecutorService)(par1: Par[A], par2: Par[B])(f: (A, B) => C): Par[C] =
    Par.unit(f(Par.run(s)(par1) get, Par.run(s)(par2) get))

  def fork[A](a: => Par[A]): Par[A] = a

  // derived combinator fork + unit
  def async[A](a: => A): Par[A] = fork(unit(a))

  def sortPar(s: ExecutorService)(l: Par[List[Int]]): Par[List[Int]] =
    map(s)(l)(_.sorted)

  def product[A,B](s: ExecutorService)(fa: Par[A], fb: Par[B]): Par[(A,B)] = Par.unit(Par.run(s)(fa) get, Par.run(s)(fb) get)
}