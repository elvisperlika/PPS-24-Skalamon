package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.move.MoveContext

/** A [[HitBehavior]] that represents a single-hit move.
  */
trait SingleHitBehavior extends HitBehavior {

  /** @return
    *   The power of the hit.
    */
  def power: Int

  override def apply(move: MoveContext): MoveContext =
    move.copy(hits = move.hits :+ MoveContext.Hit(power))
}

object SingleHitBehavior {

  /** @param power
    *   The fixed power of the hit.
    * @return
    *   A [[SingleHitBehavior]] with the specified power.
    */
  def apply(power: Int): SingleHitBehavior = SimpleSingleHitBehavior(power)
}

private case class SimpleSingleHitBehavior(power: Int) extends SingleHitBehavior
