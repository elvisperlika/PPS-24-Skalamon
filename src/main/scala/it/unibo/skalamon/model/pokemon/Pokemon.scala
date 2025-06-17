package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.status.*

sealed trait Gender
case object Male extends Gender
case object Female extends Gender
case object Genderless extends Gender

case class Type(name: String) //TODO: to be removed, it is temporary till merge

/** Represents the base attributes of a Pokémon.
  * @param name
  *   The name of the Pokémon.
  * @param gender
  *   The gender of the Pokémon.
  * @param types
  *   The list of types of the Pokémon.
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
    types: List[Type],
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
  *   List of volatile status of the Pokémon.
  */
case class BattlePokemon(
    base: Pokemon,
    level: Int,
    currentHP: Int,
    moves: List[BattleMove],
    nonVolatileStatus: Option[NonVolatileStatus],
    volatileStatus: List[VolatileStatus]
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
