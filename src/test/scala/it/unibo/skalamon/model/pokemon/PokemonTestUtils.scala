package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.pokemon.*
import it.unibo.skalamon.model.move.*

/** Test utilities for testing pokemon.
 */
object PokemonTestUtils:
  private val moveThunderShock = Move("Thunder Shock")
  private val moveElectric = Move("Electric")
  
  private val basePokemon = Pokemon("pikachu",
    Male,
    List(Type("Electric")),
    baseStats = Stats(35, 55, 40, 50, 50, 90),
    ability = Ability("Static"),
    weightKg = 6.0,
    possibleMoves = List(moveThunderShock, moveElectric)
  )
  private val startingHP: Int = 70
  private val powerPoint: Int = 4
  val simplePokemon1: BattlePokemon = BattlePokemon(basePokemon, 100, startingHP, List(BattleMove(moveThunderShock, powerPoint)))
  val simplePokemon2: BattlePokemon = BattlePokemon(basePokemon, 86, startingHP, List(BattleMove(moveElectric, powerPoint)))
