package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.Behavior

/** A [[HitBehavior]] that represents a multi-hit move, where the move can hit
  * multiple times. Each hit can have a different power, which can be fixed or
  * variable.
  */
trait MultiHitBehavior extends HitBehavior {

  /** @return
    *   The number of hits.
    */
  def hits: Int

  /** @param hitIndex
    *   The index of the hit (0-based).
    * @return
    *   The power of the N-th hit.
    */
  def power(hitIndex: Int): Int

  /**
   * Delegates the behavior of an N-hit move to N single-hit behaviors.
   * @return a list of [[SingleHitBehavior]] of size [[hits]].
   */
  override protected def delegates: List[Behavior] =
    (0 until hits)
      .map(power)
      .map(SingleHitBehavior.apply)
      .toList
}

object MultiHitBehavior {

  /** @param hits
    *   The number of hits.
    * @param power
    *   The fixed power of each hit.
    * @return
    *   A [[MultiHitBehavior]] with the specified number of hits and fixed power
    *   for each hit.
    */
  def apply(hits: Int, power: Int): MultiHitBehavior =
    FixedMultiHitBehavior(hits, power)

  /** @param hits
    *   The number of hits.
    * @param power
    *   A function that takes the hit index and returns the power for that hit.
    * @return
    *   A [[MultiHitBehavior]] with the specified number of hits and variable
    *   power for each hit.
    */
  def apply(hits: Int, power: Int => Int): MultiHitBehavior =
    VariableMultiHitBehavior(hits, power)
}

/** A simple implementation of [[MultiHitBehavior]] that uses a fixed power for
  * each hit.
  *
  * @param hits
  *   The number of hits.
  * @param power
  *   The fixed power of each hit.
  */
case class FixedMultiHitBehavior(
    hits: Int,
    private val power: Int
) extends MultiHitBehavior {
  override def power(hitIndex: Int): Int = this.power
}

/** A simple implementation of [[MultiHitBehavior]] that uses a variable power
  * for each hit.
  *
  * @param hits
  *   The number of hits.
  * @param variablePower
  *   A function that takes the hit index and returns the power for that hit.
  */
case class VariableMultiHitBehavior(
    hits: Int,
    private val variablePower: (hitIndex: Int) => Int
) extends MultiHitBehavior {
  override def power(hitIndex: Int): Int = variablePower(hitIndex)
}
