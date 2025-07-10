package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.*
import it.unibo.skalamon.model.behavior.visitor.BehaviorVisitor
import it.unibo.skalamon.model.pokemon.*

/** A change in a Pokémon's stats.
  * @param stat
  *   the statistic to change
  * @param stage
  *   The stage of the change, where 0 means *1, 1 means *1.5, -1 means *0.5,
  *   etc.
  */
case class StatChange(
    stat: Stat,
    stage: Int
)

/** Extension method to create a StatChange from a Stat + stage syntax.
  */
extension (stat: Stat)
  /** Creates a [[StatChange]] with the given stage.
    * @param stage
    *   The stage of the change.
    */
  def +(stage: Int): StatChange = StatChange(stat, stage)

  /** Creates a [[StatChange]] with the given stage.
    * @param stage
    *   The stage of the change.
    */
  def -(stage: Int): StatChange = StatChange(stat, -stage)

/** Utility object for stat stage clamping and conversion.
  */
object StatStage:
  private val MinStage = -6
  private val MaxStage = 6

  /** Clamp the stage between allowed bounds (-6 and +6). */
  def clamp(stage: Int): Int = stage.max(MinStage).min(MaxStage)

  /** Convert a stage into a multiplier:
    *   - 0 → 1.0
    *   - +1 → 1.5
    *   - -1 → 0.66...
    */
  def multiplier(stage: Int): Double =
    if stage >= 0 then (2.0 + stage) / 2.0
    else 2.0 / (2.0 - stage)

/** Behavior that changes a statistic of a Pokémon.
  * @param change
  *   The stat change to apply
  */
case class StatChangeBehavior(change: StatChange) extends HitBehavior:
  override def accept[T](visitor: BehaviorVisitor[T]): T =
    visitor.visit(this)

/** Represents the base stats and current stage-modifiers of a Pokémon.
  */
case class Stats(base: Map[Stat, Int])
