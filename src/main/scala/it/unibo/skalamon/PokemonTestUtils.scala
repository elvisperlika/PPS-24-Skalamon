package it.unibo.skalamon

import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.dsl.*
import it.unibo.skalamon.model.pokemon.Male
import it.unibo.skalamon.model.pokemon.Pokemon.*

// TEMPORARY

object PokemonTestUtils:
  def trainerAlice: Trainer =
    Trainer("Alice", List(pikachu.battling(Male), gyarados.battling(Male)))

  def trainerBob: Trainer =
    Trainer("Bob", List(bulbasaur.battling(Male)))
