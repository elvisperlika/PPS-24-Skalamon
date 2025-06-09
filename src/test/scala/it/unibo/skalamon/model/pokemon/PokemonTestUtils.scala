package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.pokemon.*

object PokemonTestUtils:
  
  private val basePokemon = Pokemon("pikachu",
    Male,
    List(Type("Electric")),
    baseStats = Stats(35, 55, 40, 50, 50, 90),
    ability = Ability("Static"),
    weightKg = 6.0,
    possibleMoves = List(Move("Thunder Shock"), Move("Electric"))
  )
  private val startingHP: Int = 70
  val simplePokemon: BattlePokemon = BattlePokemon(basePokemon, 100, startingHP, List(Move("Thunder Shock")))
