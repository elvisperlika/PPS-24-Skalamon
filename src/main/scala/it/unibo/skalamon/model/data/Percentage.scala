package it.unibo.skalamon.model.data

/** A percentage value that can be used to calculate a portion of a given value.
  * @param percentage
  *   The percentage value, between 0 and 100.
  */
case class Percentage(percentage: Int):
  require(percentage >= 0 && percentage <= 100)

  /** Calculates the percentage of a given value.
    * @param value
    *   The value to calculate the percentage of.
    * @return
    *   The calculated percentage of the value.
    */
  def of(value: Int): Int =
    (value * percentage) / 100

  override def toString: String = s"$percentage%"

extension (i: Int)
  /** @return
    *   A [[Percentage]] of the given value.
    */
  def percent: Percentage = Percentage(i)
