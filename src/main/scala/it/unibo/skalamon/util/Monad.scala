package it.unibo.skalamon.util

/** Monad.
  * @tparam M
  *   Type constructor
  */
trait Monad[M[_]]:
  def unit[A](a: A): M[A]

  extension [A](m: M[A])
    def flatMap[B](f: A => M[B]): M[B]
    def map[B](f: A => B): M[B] =
      m.flatMap(a => unit(f(a)))
