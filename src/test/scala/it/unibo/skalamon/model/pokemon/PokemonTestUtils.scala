package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.pokemon.*
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.types.TypesCollection.Electric

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
    Electric,
    baseStats = Stats(35, 55, 40, 50, 50, 90),
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
    Electric,
    baseStats = Stats(35, 55, 40, 50, 50, 90),
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
