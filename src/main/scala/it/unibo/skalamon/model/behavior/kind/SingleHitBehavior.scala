package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.visitor.BehaviorVisitor

/** A [[HitBehavior]] that represents a single-hit move.
  */
trait SingleHitBehavior extends HitBehavior:

  /** @return
    *   The power of the hit.
    */
  def power: Int

  override def accept[T](visitor: BehaviorVisitor[T]): T = visitor.visit(this)

object SingleHitBehavior:

  /** @param power
    *   The fixed power of the hit.
    * @return
    *   A [[SingleHitBehavior]] with the specified power.
    */
  def apply(power: Int): SingleHitBehavior = SimpleSingleHitBehavior(power)

/** A simple implementation of [[SingleHitBehavior]] that uses a fixed power.
  *
  * @param power
  *   The fixed power of the hit.
  */
case class SimpleSingleHitBehavior(power: Int) extends SingleHitBehavior
