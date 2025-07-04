package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.behavior.kind.{StatChange, StatStage, Stats}
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.status.{
  AssignedStatus,
  NonVolatileStatus,
  VolatileStatus
}

import java.util.UUID

sealed trait Gender
case object Male extends Gender
case object Female extends Gender
case object Genderless extends Gender

/** Represents the whole Pokémon.
  * @param base
  *   The base attributes of the Pokémon.
  * @param gender
  *   The gender of the Pokémon.
  * @param currentHP
  *   Current Health Points of the Pokémon.
  * @param moves
  *   List of moves of the Pokémon.
  * @param nonVolatileStatus
  *   The non-volatile status of the Pokémon.
  * @param volatileStatus
  *   Set of volatile status of the Pokémon.
  * @param id
  *   The unique identifier of the battle Pokémon. This is useful to track the
  *   Pokémon in a battle context: even if this instance is copied, the ID
  *   remains the same and can be tracked across different states.
  */
case class BattlePokemon(
    base: Pokemon,
    gender: Gender,
    currentHP: Int,
    moves: List[BattleMove],
    nonVolatileStatus: Option[AssignedStatus[NonVolatileStatus]],
    volatileStatus: Set[AssignedStatus[VolatileStatus]],
    statChanges: Map[Stat, Int] = Map.empty,
    id: UUID = UUID.randomUUID()
):
  /** @return
    *   Whether the Pokémon is the same as [[other]] based on its ID.
    */
  def is(other: BattlePokemon): Boolean =
    this.id == other.id

  /** Return the current stats of the Pokémon.
    *
    * @return
    *   the current stats of the Pokémon.
    */
  def actualStats: Stats =
    base.stats.copy(
      base = base.stats.base.map { case (stat, value) =>
        stat -> (StatStage.multiplier(statChanges.getOrElse(
          stat,
          0
        )) * value).toInt
      }
    )

  /** Return true if the Pokémon is still alive.
    *
    * @return
    *   true if the Pokémon is still alive.
    */
  def isAlive: Boolean = currentHP > 0

  /** Applies a stat change to the Pokémon.
    *
    * @param change
    *   The stat change to be applied.
    * @return
    *   the copy of the Pokémon with the applied stat change.
    */
  def applyStatChange(change: StatChange): BattlePokemon =
    val newStage =
      StatStage.clamp(statChanges.getOrElse(change.stat, 0) + change.stage)
    this.copy(statChanges = statChanges.updated(change.stat, newStage))
