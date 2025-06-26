package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.types.*
import it.unibo.skalamon.model.types.TypesCollection.*

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
    case "Gengar " =>
      Pokemon(
        name = "Gengar",
        types = List(Ghost, Poison),
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 65,
            Stat.Defense -> 60,
            Stat.SpecialAttack -> 130,
            Stat.SpecialDefense -> 75,
            Stat.Speed -> 110
          )
        ),
        ability = Ability("Cursed body", Map.empty),
        weightKg = 40.5,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Machamp " =>
      Pokemon(
        name = "Machamp ",
        types = Fighting,
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 130,
            Stat.Defense -> 80,
            Stat.SpecialAttack -> 65,
            Stat.SpecialDefense -> 85,
            Stat.Speed -> 55
          )
        ),
        ability = Ability("Guts", Map.empty),
        weightKg = 130.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Alakazam" =>
      Pokemon(
        name = "Alakazam",
        types = Psychic,
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 50,
            Stat.Defense -> 45,
            Stat.SpecialAttack -> 135,
            Stat.SpecialDefense -> 95,
            Stat.Speed -> 120
          )
        ),
        ability = Ability("Synchronize", Map.empty),
        weightKg = 48.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Tyranitar" =>
      Pokemon(
        name = "Tyranitar",
        types = List(Rock, Dark),
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 134,
            Stat.Defense -> 110,
            Stat.SpecialAttack -> 95,
            Stat.SpecialDefense -> 100,
            Stat.Speed -> 61
          )
        ),
        ability = Ability("Sand Stream", Map.empty),
        weightKg = 202.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
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
    case "Dragonite" =>
      Pokemon(
        name = "Dragonite",
        types = List(Dragon, Flying),
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 134,
            Stat.Defense -> 95,
            Stat.SpecialAttack -> 100,
            Stat.SpecialDefense -> 100,
            Stat.Speed -> 80
          )
        ),
        ability = Ability("Inner Focus", Map.empty),
        weightKg = 210.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Scizor" =>
      Pokemon(
        name = "Scizor",
        types = List(Bug, Steel),
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 130,
            Stat.Defense -> 100,
            Stat.SpecialAttack -> 55,
            Stat.SpecialDefense -> 80,
            Stat.Speed -> 65
          )
        ),
        ability = Ability("Swarm", Map.empty),
        weightKg = 118.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Gardevoir" =>
      Pokemon(
        name = "Gardevoir",
        types = List(Psychic, Fairy),
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 65,
            Stat.Defense -> 65,
            Stat.SpecialAttack -> 125,
            Stat.SpecialDefense -> 115,
            Stat.Speed -> 80
          )
        ),
        ability = Ability("Synchronize", Map.empty),
        weightKg = 48.4,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Gyarados" =>
      Pokemon(
        name = "Gyarados",
        types = List(Water, Flying),
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 125,
            Stat.Defense -> 79,
            Stat.SpecialAttack -> 60,
            Stat.SpecialDefense -> 100,
            Stat.Speed -> 81
          )
        ),
        ability = Ability("Intimidate", Map.empty),
        weightKg = 235.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Lucario" =>
      Pokemon(
        name = "Lucario",
        types = List(Flying, Steel),
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 110,
            Stat.Defense -> 70,
            Stat.SpecialAttack -> 115,
            Stat.SpecialDefense -> 70,
            Stat.Speed -> 90
          )
        ),
        ability = Ability("Steadfast", Map.empty),
        weightKg = 54.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Snorlax" =>
      Pokemon(
        name = "Snorlax",
        types = Normal,
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 110,
            Stat.Defense -> 65,
            Stat.SpecialAttack -> 65,
            Stat.SpecialDefense -> 110,
            Stat.Speed -> 30
          )
        ),
        ability = Ability("Immunity", Map.empty),
        weightKg = 460.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Greninja" =>
      Pokemon(
        name = "Greninja",
        types = List(Water, Dark),
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 95,
            Stat.Defense -> 67,
            Stat.SpecialAttack -> 103,
            Stat.SpecialDefense -> 71,
            Stat.Speed -> 122
          )
        ),
        ability = Ability("Torrent", Map.empty),
        weightKg = 40.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Aegislash" =>
      Pokemon(
        name = "Aegislash",
        types = List(Steel, Dark),
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 50,
            Stat.Defense -> 140,
            Stat.SpecialAttack -> 50,
            Stat.SpecialDefense -> 140,
            Stat.Speed -> 60
          )
        ),
        ability = Ability("Stance Change", Map.empty),
        weightKg = 53.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Mimikyu" =>
      Pokemon(
        name = "Mimikyu",
        types = List(Ghost, Fairy),
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 90,
            Stat.Defense -> 80,
            Stat.SpecialAttack -> 50,
            Stat.SpecialDefense -> 105,
            Stat.Speed -> 96
          )
        ),
        ability = Ability("Disguise", Map.empty),
        weightKg = 0.7,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case "Dragapult" =>
      Pokemon(
        name = "Dragapult",
        types = List(Dragon, Ghost),
        baseStats = Stats(
          base = Map(
            Stat.Attack -> 120,
            Stat.Defense -> 75,
            Stat.SpecialAttack -> 100,
            Stat.SpecialDefense -> 75,
            Stat.Speed -> 142
          )
        ),
        ability = Ability("Clear Body", Map.empty),
        weightKg = 50.0,
        possibleMoves = List(
          Move("Thunder Shock"),
          Move("Electric")
        )
      )
    case _ =>
      throw new IllegalArgumentException(s"Unknown Pokémon name: $name")
