package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.status.*
import it.unibo.skalamon.model.types.*

import java.util.UUID

sealed trait Gender
case object Male extends Gender
case object Female extends Gender
case object Genderless extends Gender

/** Represents the base attributes of a Pokémon.
  * @param name
  *   The name of the Pokémon.
  * @param gender
  *   The gender of the Pokémon.
  * @param types
  *   The list of types of the Pokémon.
  * @param hp
  *   The base HP of the Pokémon.
  * @param baseStats
  *   The base stats of the Pokémon.
  * @param ability
  *   The ability of the Pokémon.
  * @param weightKg
  *   The weight of the Pokémon in Kg.
  * @param possibleMoves
  *   The list of possible moves of the Pokémon.
  */
case class Pokemon(
    name: String,
    gender: Gender,
    types: PokemonType,
    hp: Int,
    baseStats: Stats,
    ability: Ability,
    weightKg: Double,
    possibleMoves: List[Move]
)

/** Represents the whole Pokémon.
  * @param base
  *   The base attributes of the Pokémon.
  * @param level
  *   The level of the Pokémon.
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
    level: Int,
    currentHP: Int,
    moves: List[BattleMove],
    nonVolatileStatus: Option[AssignedStatus[NonVolatileStatus]],
    volatileStatus: Set[AssignedStatus[VolatileStatus]],
    id: UUID = UUID.randomUUID()
):

  /** Return the current stats of the Pokémon, updated to it's level.
    * @return
    *   the current stats of the Pokémon.
    */
  def actualStats: Stats =
    base.baseStats // TODO: for now, just return the base stats

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
