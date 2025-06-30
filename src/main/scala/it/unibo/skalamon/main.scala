package it.unibo.skalamon

import it.unibo.skalamon.controller.battle.action.MoveAction
import it.unibo.skalamon.controller.battle.{BattleController, GameState}
import it.unibo.skalamon.model.battle.{Battle, BattleState}
import it.unibo.skalamon.model.event.BattleStateEvents
import it.unibo.skalamon.model.move.{BattleMove, createContext}
import it.unibo.skalamon.model.pokemon.BattlePokemon

// TEMPORARY

@main
def main(): Unit =
  var (trainerAlice, trainerBob) =
    (PokemonTestUtils.trainerAlice, PokemonTestUtils.trainerBob)
  trainerAlice = trainerAlice.copy(inField = Some(trainerAlice.team.head))
  trainerBob = trainerBob.copy(inField = Some(trainerBob.team.head))

  val battle = Battle(List(trainerAlice, trainerBob))
  val controller = BattleController(battle)

  battle.eventManager.watch(BattleStateEvents.Changed): (_, state) =>
    printView(state)

  controller.start()

  while battle.gameState == GameState.InProgress do
    Thread.sleep(2000)

    println(s"Current turn: ${battle.turnIndex}, stage: ${battle.currentTurn.map(_.state.stage).mkString}")

    if controller.isWaitingForActions then
      println("Waiting for actions...")
      // Simulated action registration
      val aliceAction = moveToAction(
        move = trainerAlice.inField.get.moves.head,
        source = trainerAlice.inField.get,
        target = trainerBob.inField.get
      )
      val bobAction = moveToAction(
        move = trainerBob.inField.get.moves.head,
        source = trainerBob.inField.get,
        target = trainerAlice.inField.get
      )
      controller.registerAction(PokemonTestUtils.trainerAlice, aliceAction)
      controller.registerAction(PokemonTestUtils.trainerBob, bobAction)

    controller.update()

def moveToAction(
    move: BattleMove,
    source: BattlePokemon,
    target: BattlePokemon
): MoveAction =
  val context = move.createContext(_.success, target, source)
  MoveAction(context)

def printView(state: BattleState): Unit =
  state.trainers.foreach { trainer =>
    println(s"Trainer: ${trainer.name}")
    trainer.team.foreach { pokemon =>
      println(s"  Pokemon: ${pokemon.base.name}")
      println(s"    HP: ${pokemon.currentHP}")
      println(s"    Moves: ${pokemon.moves.map(_.move.name).mkString(", ")}")
      println(s"    Status: ${pokemon.nonVolatileStatus.mkString(", ")}")
    }
  }
  println("\n\n\n")
