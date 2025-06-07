package it.unibo.skalamon.model.data

import it.unibo.skalamon.model.data.Percentage.{MaxBound, MinBound}

/** A percentage value that can be used to calculate a portion of a given value.
  *
  * @param value
  *   The percentage value, between 0 and 100.
  */
case class Percentage(private val value: Int):
  require(value >= MinBound && value <= MaxBound)

  /** Calculates the percentage of a given value.
    * @param value
    *   The value to calculate the percentage of.
    * @return
    *   The calculated percentage of the value.
    */
  def of(value: Int): Int =
    (value * value) / MaxBound

  /** Generates a random boolean based on the percentage. If the percentage is
    * 0, it always returns false. If the percentage is 100, it always returns
    * true.
    *
    * @return
    *   A random boolean value based on the percentage.
    */
  def randomBoolean(using generator: RandomGenerator = RandomGenerator()): Boolean = {
    generator.nextInt(MinBound, MaxBound) < value
  }

  override def toString: String = s"$value%"

object Percentage:
  /** The minimum value accepted by a percentage. */
  val MinBound: Int = 0

  /** The maximum value accepted by a percentage. */
  val MaxBound: Int = 100

extension (i: Int)
  /** @return
    *   A [[Percentage]] of the given value.
    */
  def percent: Percentage = Percentage(i)
