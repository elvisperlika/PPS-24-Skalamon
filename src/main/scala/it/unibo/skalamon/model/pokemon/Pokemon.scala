package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.move.Move.*
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

import it.unibo.skalamon.model.ability.Ability.*
import it.unibo.skalamon.model.pokemon.Stat.*

/** Factory object to create Pokémon instances. This object contains predefined
  * Pokémon instances that can be used in the game.
  */
object Pokemon:
  import it.unibo.skalamon.model.dsl.*

  def alakazam: Pokemon =
    pokemon("Alakazam"):
      _ typed Psychic hp 55 weighing 48.0.kg ability synchronize stat Attack -> 50 stat Defense -> 45 stat SpecialAttack -> 135 stat SpecialDefense -> 95 stat Speed -> 120

  def metagross: Pokemon =
    pokemon("Metagross"):
      _ typed Steel + Psychic hp 80 weighing 550.0.kg ability clearBody stat Attack -> 135 stat Defense -> 130 stat SpecialAttack -> 95 stat SpecialDefense -> 90 stat Speed -> 70 moves (
        bulletPunch,
        zenHeadbutt,
        psychic,
        earthquake
      )

  def tyranitar: Pokemon =
    pokemon("Tyranitar"):
      _ typed Rock + Dark hp 100 weighing 202.0.kg ability sandStream stat Attack -> 134 stat Defense -> 110 stat SpecialAttack -> 95 stat SpecialDefense -> 100 stat Speed -> 61 moves (
        bite,
        earthquake,
        dragonDance,
        fissure
      )

  def pikachu: Pokemon =
    pokemon("Pikachu"):
      _ typed Electric hp 35 weighing 6.0.kg ability static stat Attack -> 55 stat Defense -> 40 stat SpecialAttack -> 50 stat SpecialDefense -> 50 stat Speed -> 90 moves (
        tackle,
        quickAttack,
        thunderbolt,
        thunderWave
      )

  def dragonite: Pokemon =
    pokemon("Dragonite"):
      _ typed Dragon + Flying hp 91 weighing 210.0.kg ability none stat Attack -> 134 stat Defense -> 95 stat SpecialAttack -> 100 stat SpecialDefense -> 100 stat Speed -> 80 moves (
        dragonRage,
        dragonClaw,
        swift,
        dragonDance
      )

  def gardevoir: Pokemon =
    pokemon("Gardevoir"):
      _ typed Psychic + Fairy hp 68 weighing 48.4.kg ability synchronize stat Attack -> 65 stat Defense -> 65 stat SpecialAttack -> 125 stat SpecialDefense -> 115 stat Speed -> 80

  def malamar: Pokemon =
    pokemon("Malamar"):
      _ typed Dark + Psychic hp 86 weighing 47.0.kg ability contrary stat Attack -> 92 stat Defense -> 88 stat SpecialAttack -> 68 stat SpecialDefense -> 75 stat Speed -> 73 moves (
        hypnosis,
        psychic,
        nightSlash,
        trickRoom,
      )

  def gyarados: Pokemon =
    pokemon("Gyarados"):
      _ typed Water + Flying hp 95 weighing 235.0.kg ability intimidate stat Attack -> 125 stat Defense -> 79 stat SpecialAttack -> 60 stat SpecialDefense -> 100 stat Speed -> 81 moves (
        surf,
        earthquake,
        grassKnot,
        dragonDance,
      )

  def pelipper: Pokemon =
    pokemon("Pelipper"):
      _ typed Water + Flying hp 60 weighing 28.0.kg ability drizzle stat Attack -> 50 stat Defense -> 100 stat SpecialAttack -> 85 stat SpecialDefense -> 70 stat Speed -> 65 moves (
        swift,
        roost,
        aquaJet,
        rainDance
      )

  def lucario: Pokemon =
    pokemon("Lucario"):
      _ typed Fighting + Steel hp 70 weighing 54.0.kg ability intimidate stat Attack -> 110 stat Defense -> 70 stat SpecialAttack -> 115 stat SpecialDefense -> 70 stat Speed -> 90 moves (
        quickAttack,
        swordDance
      )

  def snorlax: Pokemon =
    pokemon("Snorlax"):
      _ typed Normal hp 160 weighing 460.0.kg ability none stat Attack -> 110 stat Defense -> 65 stat SpecialAttack -> 65 stat SpecialDefense -> 110 stat Speed -> 30 moves (
        tackle,
        rest,
        earthquake,
      )

  def bulbasaur: Pokemon =
    pokemon("Bulbasaur"):
      _ typed Grass + Poison hp 45 weighing 6.9.kg ability naturalCure stat Attack -> 49 stat Defense -> 49 stat SpecialAttack -> 65 stat SpecialDefense -> 65 stat Speed -> 45 moves(
        tackle,
        grassKnot,
        razorLeaf,
        bulletSeed
      )

  val yanmega: Pokemon =
    pokemon("Yanmega"):
      _ typed Bug + Flying hp 86 weighing 51.5.kg ability speedBoost stat Attack -> 76 stat Defense -> 86 stat SpecialAttack -> 116 stat SpecialDefense -> 56 stat Speed -> 95

  def charmander: Pokemon =
    pokemon("Charmander"):
      _ typed Fire hp 39 weighing 8.5.kg ability none stat Attack -> 52 stat Defense -> 43 stat SpecialAttack -> 60 stat SpecialDefense -> 50 stat Speed -> 65 moves (
        slash,
        flamethrower,
        willOWisp,
        sunnyDay
      )

  def squirtle: Pokemon =
    pokemon("Squirtle"):
      _ typed Water hp 44 weighing 9.0.kg ability swiftSwim stat Attack -> 48 stat Defense -> 65 stat SpecialAttack -> 50 stat SpecialDefense -> 64 stat Speed -> 43 moves (
        tackle,
        aquaJet,
        surf
      )

  def rattata: Pokemon =
    pokemon("Rattata"):
      _ typed Normal hp 30 weighing 3.5.kg ability none stat Attack -> 56 stat Defense -> 35 stat SpecialAttack -> 25 stat SpecialDefense -> 35 stat Speed -> 72 moves (
        tackle,
        quickAttack,
        growl,
        superFang
      )
