package it.unibo.skalamon.utils

import it.unibo.skalamon.model.battle.{BattleState, Trainer}
import it.unibo.skalamon.model.pokemon.{BattlePokemon, PokemonTestUtils}

/** Test utility trait that provides mock trainers and their Pokémon. */
trait MockTrainers:
  protected val alice: Trainer = PokemonTestUtils.trainerAlice
  protected val bob: Trainer = PokemonTestUtils.trainerBob

  protected val source: BattlePokemon = alice.team.head
  protected val target: BattlePokemon = bob.team.head

  protected def getSource(state: BattleState): BattlePokemon =
    state.trainers.find(_.name == alice.name).get.team.head

  protected def getTarget(state: BattleState): BattlePokemon =
    state.trainers.find(_.name == bob.name).get.team.head
