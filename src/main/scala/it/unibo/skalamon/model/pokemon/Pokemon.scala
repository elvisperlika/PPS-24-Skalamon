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

/** Factory object to create Pokémon instances. This object provides a method to
  * create a Pokémon based on its ID.
  */
object Pokemon:
  /** Creates a Pokémon based on the provided ID.
    * @param name
    *   The name of the Pokémon to create.
    * @return
    *   A `Pokemon` instance representing the Pokémon.
    */
  def apply(name: String): Pokemon = name match
    case "Pikachu" =>
      Pokemon(
        name = "Pikachu",
        types = Electric,
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 55,
            Stat.Defense -> 40,
            Stat.SpecialAttack -> 50,
            Stat.SpecialDefense -> 50,
            Stat.Speed -> 90
          )
        ),
        ability = Ability("Static", Map.empty),
        weightKg = 6.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
