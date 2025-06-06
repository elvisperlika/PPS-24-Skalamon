package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.move.MoveContext

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

  override def apply(move: MoveContext): MoveContext =
    move.copy(hits = move.hits ++ (0 until hits).map { hitIndex =>
      MoveContext.Hit(power(hitIndex))
    })
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

private case class FixedMultiHitBehavior(
    hits: Int,
    private val power: Int
) extends MultiHitBehavior {
  override def power(hitIndex: Int): Int = this.power
}

private case class VariableMultiHitBehavior(
    hits: Int,
    private val variablePower: (hitIndex: Int) => Int
) extends MultiHitBehavior {
  override def power(hitIndex: Int): Int = variablePower(hitIndex)
}
