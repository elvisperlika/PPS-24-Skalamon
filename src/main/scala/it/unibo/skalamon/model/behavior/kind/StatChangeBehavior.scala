package it.unibo.skalamon.model.behavior.kind

/** Behavior that changes a statistic of a Pokémon.
  * @param change
  *   The stat change to apply
  */
case class StatChangeBehavior(change: StatChange) extends HitBehavior

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

extension (stat: Stat)
  /** Creates a [[StatChange]] with the given stage.
    * @param stage
    *   The stage of the change.
    */
  def +(stage: Int): StatChange =
    StatChange(stat, stage)

// temp

enum Stat:
  case Attack, Defense, SpecialAttack, SpecialDefense, Speed
