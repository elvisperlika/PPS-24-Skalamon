package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.model.battle.{Battle, BattleState, Trainer}
import it.unibo.skalamon.model.dsl.*
import it.unibo.skalamon.model.pokemon.Pokemon.*
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Male, Pokemon}

/** Test utilities to run simulated battles.
  */
trait BattleSimulationTest:
  val neutral: Pokemon = rattata

  extension (pokemon: Pokemon)
    def b: BattlePokemon = pokemon.battling(Male)

  extension (battle: Battle)
    def state: BattleState = battle.currentTurn.get.state.snapshot

    def trainersTuple: (Trainer, Trainer) =
      (battle.state.trainers.head, battle.state.trainers.tail.head)

  extension (state: BattleState)
    def inField: (BattlePokemon, BattlePokemon) =
      (state.trainers.head.inField.get, state.trainers.tail.head.inField.get)

  def newBattle(teamAlice: Pokemon*)(teamBob: Pokemon*): (Battle, BattleController, Trainer, Trainer) =
    val alice = Trainer("Alice", teamAlice.map(_.b).toList)
    val bob = Trainer("Bob", teamBob.map(_.b).toList)
    val battle = Battle(alice :: bob :: Nil)
    val controller = BattleController(battle)
    controller.start()
    (battle, controller, alice, bob)