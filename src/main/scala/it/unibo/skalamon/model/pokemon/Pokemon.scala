package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.types.*
import it.unibo.skalamon.model.types.TypesCollection.Electric

sealed trait Gender
case object Male extends Gender
case object Female extends Gender
case object Genderless extends Gender

/** Represents the base attributes of a Pokémon.
  * @param name
  *   The name of the Pokémon.
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
    types: PokemonType,
    baseStats: Stats,
    ability: Ability,
    weightKg: Double,
    possibleMoves: List[Move]
)

/** Represents the whole Pokémon.
  * @param base
  *   The base attributes of the Pokémon.
  * @param gender
  *   The gender of the Pokémon.
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
    * @return
    *   A `Pokemon` instance representing the Pokémon.
    */
