package it.unibo.skalamon.util

import it.unibo.skalamon.model.ability.Ability
import it.unibo.skalamon.model.behavior.kind.Stats
import it.unibo.skalamon.model.move.Move
import it.unibo.skalamon.model.pokemon.{Pokemon, Stat}
import it.unibo.skalamon.model.types.TypesCollection.*

/** Represents the ID of a Pokémon. This enum is used to identify different
  * Pokémon by their unique IDs.
  */
enum PokemonId:
  case Pikachu

//TODO: integrate Ability and Moves when available

/** Factory object to create Pokémon instances. This object provides a method to
  * create a Pokémon based on its ID.
  */
object Pokedex:
  /** Creates a Pokémon based on the provided ID.
    * @param id
    *   The ID of the Pokémon to create.
    * @return
    *   A `Pokemon` instance representing the Pokémon.
    */
  def create(id: PokemonId): Pokemon = id match
    case PokemonId.Pikachu =>
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
