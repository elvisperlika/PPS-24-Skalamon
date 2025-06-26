package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.behavior.kind.{StatChange, Stats}
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.status.{
  AssignedStatus,
  NonVolatileStatus,
  VolatileStatus
}

import java.util.UUID

/** Represents the whole Pokémon.
  *
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
    id: UUID = UUID.randomUUID()
):
  /** Return true if the Pokémon is still alive.
    * @return
    *   true if the Pokémon is still alive.
    */
  def isAlive: Boolean = currentHP > 0

  /** Deals damage to the Pokémon.
    *
    * @param damage
    *   The damage to be inflicted on the Pokémon.
    * @return
    *   the copy of the damaged Pokémon.
    */
  def takeDamage(damage: Int): BattlePokemon =
    this.copy(currentHP =
      math.max(0, currentHP - damage)
    ) // TODO: this method is just temporary, to be removed when the battle engine is implemented

  def applyStatChange(change: StatChange): BattlePokemon =
    this.copy(base = base.copy(baseStats = base.baseStats.applyChange(change)))
