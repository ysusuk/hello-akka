package par

import java.util.concurrent._

// execute shall be
object Par {
  implicit val exec: ExecutorService = Executors.newFixedThreadPool(2)

  type Par[A] = ExecutorService => Future[A]

  def run[A](s: ExecutorService)(par: Par[A]): Future[A] = par(s)

  def unit[A](a: A): Par[A] = (es: ExecutorService) => es.submit(new Callable[A] {
    override def call(): A = a
  })

  def map[A, B](s: ExecutorService)(fa: Par[A])(f: A => B): Par[B] =
    flatMap(fa)((a: A) => Par.unit(f(a)))

  def map2[A, B, C](s: ExecutorService)(par1: Par[A], par2: Par[B])(f: (A, B) => C): Par[C] =
    Par.unit(f(Par.run(s)(par1) get, Par.run(s)(par2) get))

  def fork[A](es: ExecutorService)(a: => Par[A]): Par[A] = es => a(es)

  // derived combinator fork + unit
  def async[A](a: => A): Par[A] = fork(exec)(unit(a))

  def sortPar(s: ExecutorService)(l: Par[List[Int]]): Par[List[Int]] =
    map(s)(l)(_.sorted)

  def product[A, B](s: ExecutorService)(fa: Par[A], fb: Par[B]): Par[(A, B)] = Par.unit(Par.run(s)(fa) get, Par.run(s)(fb) get)

  def choiceMap[K, V](key: Par[K])(choices: Map[K, Par[V]]): Par[V] =
    flatMap(key)(choices)

  def choiceN[A](n: Par[Int])(choices: List[Par[A]]): Par[A] =
    flatMap(n)(choices)

  def chooser[A, B](a: Par[A])(choices: A => Par[B]): Par[B] =
    flatMap(a)(choices)

  // or bind
  def flatMap[A, B](a: Par[A])(f: A => Par[B]): Par[B] =
    es => {
      val ind = run(es)(a).get // Full source files
      run(es)(f(ind))
    }
}