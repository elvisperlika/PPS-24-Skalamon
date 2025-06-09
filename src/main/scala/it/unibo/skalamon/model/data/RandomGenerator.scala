package it.unibo.skalamon.model.data

/** A generator of random values.
  */
trait RandomGenerator:
  /** @param min
    *   Minimum value (inclusive)
    * @param max
    *   Maximum value (inclusive)
    * @return
    *   A random integer between `min` and `max`.
    */
  def nextInt(min: Int, max: Int): Int

object RandomGenerator:
  /** @return
    *   An implementation of [[RandomGenerator]] that uses Scala's built-in
    *   random generator.
    */
  def apply(): RandomGenerator = BuiltInRandomGenerator

private object BuiltInRandomGenerator extends RandomGenerator:
  override def nextInt(min: Int, max: Int): Int =
    if (min > max) {
      throw new IllegalArgumentException(
        "min must be less than or equal to max"
      )
    }

    scala.util.Random.nextInt((max - min) + 1) + min
