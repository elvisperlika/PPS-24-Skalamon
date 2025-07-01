package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.types.*
import it.unibo.skalamon.model.types.TypesCollection.*

/** Represents the base attributes of a Pokémon.
  * @param name
  *   The name of the Pokémon.
  * @param types
  *   The list of types of the Pokémon.
  * @param hp
  *   The base HP (Health Points) of the Pokémon.
  * @param stats
  *   The base stats of the Pokémon.
  * @param ability
  *   The ability of the Pokémon.
  * @param weightKg
  *   The weight of the Pokémon in Kg.
  * @param moves
  *   The list of possible moves of the Pokémon.
  */
case class Pokemon(
    name: String,
    types: List[Type],
    hp: Int,
    stats: Stats,
    ability: Ability,
    weightKg: Double,
    moves: List[Move]
)

/** Factory object to create Pokémon instances. This object contains predefined
  * Pokémon instances that can be used in the game.
  */
object Pokemon:

  def gengar: Pokemon =
    Pokemon(
      name = "Gengar",
      types = Ghost :: Poison :: Nil,
      hp = 60,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def machamp: Pokemon =
    Pokemon(
      name = "Machamp ",
      types = Fighting :: Nil,
      hp = 90,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def alakazam: Pokemon =
    Pokemon(
      name = "Alakazam",
      types = Psychic :: Nil,
      hp = 55,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def tyranitar: Pokemon =
    Pokemon(
      name = "Tyranitar",
      types = Rock :: Dark :: Nil,
      hp = 100,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def pikachu: Pokemon =
    Pokemon(
      name = "Pikachu",
      types = Electric :: Nil,
      hp = 35,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def dragonite: Pokemon =
    Pokemon(
      name = "Dragonite",
      types = Dragon :: Flying :: Nil,
      hp = 91,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def scizor: Pokemon =
    Pokemon(
      name = "Scizor",
      types = Bug :: Steel :: Nil,
      hp = 70,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def gardevoir: Pokemon =
    Pokemon(
      name = "Gardevoir",
      types = Psychic :: Fairy :: Nil,
      hp = 68,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def gyarados: Pokemon =
    Pokemon(
      name = "Gyarados",
      types = Water :: Flying :: Nil,
      hp = 95,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def lucario: Pokemon =
    Pokemon(
      name = "Lucario",
      types = List(Flying, Steel),
      hp = 70,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def snorlax: Pokemon =
    Pokemon(
      name = "Snorlax",
      types = Normal :: Nil,
      hp = 160,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def greninja: Pokemon =
    Pokemon(
      name = "Greninja",
      types = List(Water, Dark),
      hp = 72,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def aegislash: Pokemon =
    Pokemon(
      name = "Aegislash",
      types = List(Steel, Dark),
      hp = 60,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def mimikyu: Pokemon =
    Pokemon(
      name = "Mimikyu",
      types = List(Ghost, Fairy),
      hp = 55,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def dragapult: Pokemon =
    Pokemon(
      name = "Dragapult",
      types = List(Dragon, Ghost),
      hp = 88,
      stats = Stats(
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
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def bulbasaur: Pokemon =
    Pokemon(
      name = "Bulbasaur",
      types = Grass :: Poison :: Nil,
      hp = 45,
      stats = Stats(
        base = Map(
          Stat.Attack -> 49,
          Stat.Defense -> 49,
          Stat.SpecialAttack -> 65,
          Stat.SpecialDefense -> 65,
          Stat.Speed -> 45
        )
      ),
      ability = Ability("Overgrow", Map.empty),
      weightKg = 6.9,
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
  def charmander: Pokemon =
    Pokemon(
      name = "Charmander",
      types = Fire :: Nil,
      hp = 39,
      stats = Stats(
        base = Map(
          Stat.Attack -> 52,
          Stat.Defense -> 43,
          Stat.SpecialAttack -> 60,
          Stat.SpecialDefense -> 50,
          Stat.Speed -> 65
        )
      ),
      ability = Ability("Blaze", Map.empty),
      weightKg = 8.5,
      moves = Nil
//      List(
//        Move("Thunder Shock"),
//        Move("Electric")
//      )
    )
