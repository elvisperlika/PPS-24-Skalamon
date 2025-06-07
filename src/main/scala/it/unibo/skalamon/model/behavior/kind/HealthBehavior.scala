package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.Behavior

/** A [[Behavior]] that affects the health of a target.
  */
trait HealthBehavior extends Behavior:
  /** Computes the new health of the target.
    *
    * @param currentHealth
    *   The current health of the target.
    * @return
    *   The new health after applying this behavior.
    */
  def newHealth(currentHealth: Int): Int

/** A [[HealthBehavior]] that modifies health by a relative amount, compared to
  * the target's current health.
  *
  * @param delta
  *   The change in health, positive for healing and negative for damage.
  */
private trait RelativeHealthBehavior(delta: Int) extends HealthBehavior:
  override def newHealth(currentHealth: Int): Int =
    currentHealth + delta

/** A [[HealthBehavior]] that heals the target by a fixed amount.
  *
  * @param delta
  *   The amount of health to add.
  */
case class HealBehavior(delta: Int) extends RelativeHealthBehavior(delta.abs)

/** A [[HealthBehavior]] that damages the target by a fixed amount.
  *
  * @param delta
  *   The amount of health to subtract.
  */
case class DamageBehavior(delta: Int) extends RelativeHealthBehavior(-delta.abs)
