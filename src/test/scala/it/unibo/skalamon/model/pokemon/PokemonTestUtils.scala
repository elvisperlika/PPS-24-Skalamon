package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.pokemon.*
import it.unibo.skalamon.model.move.*

/** Test utilities for testing Pokémon.
  */
object PokemonTestUtils:
  private val moveThunderShock = Move("Thunder Shock")
  private val moveElectric = Move("Electric")

  private val startingHP: Int = 70
  private val powerPoint: Int = 4
  private val levelPokemon1: Int = 100
  private val levelPokemon2: Int = 86

  private val basePokemon1 = Pokemon(
    "Pikachu",
    Male,
    List(Type("Electric")),
    baseStats = Stats(
      base = Map(
        Stat.Attack -> 55,
        Stat.Defense -> 40,
        Stat.SpecialAttack -> 50,
        Stat.SpecialDefense -> 50,
        Stat.Speed -> 90
      )
    ),
    ability = Ability("Static"),
    weightKg = 6.0,
    possibleMoves = List(moveThunderShock, moveElectric)
  )
  val simplePokemon1: BattlePokemon = BattlePokemon(
    basePokemon1,
    levelPokemon1,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option.empty,
    List()
  )

  private val basePokemon2 = Pokemon(
    "Bulbasaur",
    Male,
    List(Type("Electric")),
    baseStats = Stats(
      base = Map(
        Stat.Attack -> 55,
        Stat.Defense -> 40,
        Stat.SpecialAttack -> 50,
        Stat.SpecialDefense -> 50,
        Stat.Speed -> 90
      )
    ),
    ability = Ability("Static"),
    weightKg = 6.0,
    possibleMoves = List(moveThunderShock, moveElectric)
  )
  val simplePokemon2: BattlePokemon = BattlePokemon(
    basePokemon2,
    levelPokemon2,
    startingHP,
    List(BattleMove(moveElectric, powerPoint)),
    Option.empty,
    List()
  )
