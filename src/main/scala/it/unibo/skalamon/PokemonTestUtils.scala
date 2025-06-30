package it.unibo.skalamon

import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.event.TurnStageEvents
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Male, Pokemon, Stat}
import it.unibo.skalamon.model.types.*
import it.unibo.skalamon.model.types.TypesCollection.{
  Electric,
  Fire,
  Grass,
  Poison
}

// TEMPORARY

object PokemonTestUtils:
  private val moveThunderShock = Move("Thunder Shock", priority = 0, success = DamageBehavior(10))
  private val moveElectric = Move("Electric", priority = 0, success = DamageBehavior(5))

  private val genericAbility = Ability("Static", hooks = Map(
    TurnStageEvents.Started -> DamageBehavior(1) // 1HP damage at the start of the turn
  ))
  private val blazeAbility = Ability("Blaze", Map.empty)

  private val startingHP: Int = 70
  private val powerPoint: Int = 4
  private val levelPokemon1: Int = 100
  private val levelPokemon2: Int = 86

  private val basePokemon1 = Pokemon(
    "Pikachu",
    Male,
    List(Electric),
    baseStats = Stats(
      base = Map(
        Stat.Attack -> 55,
        Stat.Defense -> 40,
        Stat.SpecialAttack -> 50,
        Stat.SpecialDefense -> 50,
        Stat.Speed -> 90
      )
    ),
    ability = blazeAbility,
    weightKg = 6.0,
    possibleMoves = List(moveThunderShock, moveElectric)
  )
  val simplePokemon1: BattlePokemon = BattlePokemon(
    basePokemon1,
    levelPokemon1,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    None,
    Set.empty
  )

  private val basePokemon2 = Pokemon(
    "Bulbasaur",
    Male,
    List(Grass, Poison),
    baseStats = Stats(
      base = Map(
        Stat.Attack -> 55,
        Stat.Defense -> 40,
        Stat.SpecialAttack -> 50,
        Stat.SpecialDefense -> 50,
        Stat.Speed -> 90
      )
    ),
    ability = genericAbility,
    weightKg = 6.0,
    possibleMoves = List(moveThunderShock, moveElectric)
  )
  val simplePokemon2: BattlePokemon = BattlePokemon(
    basePokemon2,
    levelPokemon2,
    startingHP,
    List(BattleMove(moveElectric, powerPoint)),
    Option.empty,
    Set.empty
  )

  private val basePokemon3 = Pokemon(
    "Charmander",
    Male,
    Fire,
    baseStats = Stats(
      base = Map(
        Stat.Attack -> 52,
        Stat.Defense -> 43,
        Stat.SpecialAttack -> 60,
        Stat.SpecialDefense -> 50,
        Stat.Speed -> 65
      )
    ),
    ability = blazeAbility,
    weightKg = 8.5,
    possibleMoves = List(moveThunderShock, moveElectric)
  )

  private val simplePokemon3: BattlePokemon = BattlePokemon(
    basePokemon3,
    levelPokemon1,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    None,
    Set.empty
  )

  def trainerAlice: Trainer =
    Trainer("Alice", List(simplePokemon1))

  def trainerBob: Trainer =
    Trainer("Bob", List(simplePokemon2))

  def trainerGio: Trainer =
    Trainer("Gio", List(simplePokemon3))
