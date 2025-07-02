package it.unibo.skalamon

import it.unibo.skalamon.controller.battle.action.MoveAction
import it.unibo.skalamon.controller.battle.{BattleController, GameState}
import it.unibo.skalamon.model.battle.{Battle, Trainer}
import it.unibo.skalamon.model.event.BattleStateEvents
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.view.MainView
import it.unibo.skalamon.view.battle.{BattleInput, BattleView}

@main
def main(): Unit =
  var (trainer1, trainer2) =
    (PokemonTestUtils.trainerAlice, PokemonTestUtils.trainerBob)
  trainer1 = trainer1.copy(_inField = Some(trainer1.team.head))
  trainer2 = trainer2.copy(_inField = Some(trainer2.team.head))

  val inputQueue = scala.collection.mutable.Map(
    trainer1 -> Option.empty[MoveAction],
    trainer2 -> Option.empty[MoveAction]
  )

  val battle = Battle(List(trainer1, trainer2))
  val controller = BattleController(battle)

  val mainView: MainView = MainView()
  mainView.setupView()

  val battleView = BattleView(mainView.getPlayScreen)

  battle.eventManager.watch(BattleStateEvents.Changed): (_, state) =>
    mainView.repaint()
    battleView.update(state)

  controller.start()

  mainView.setKeyPressedHandler {
    case BattleInput.playerMove1 =>
      handleTrainerAction(trainer1, trainer2, 0)
    case BattleInput.playerMove2 =>
      handleTrainerAction(trainer1, trainer2, 1)
    case BattleInput.playerMove3 =>
      handleTrainerAction(trainer1, trainer2, 2)
    case BattleInput.playerMove4 =>
      handleTrainerAction(trainer1, trainer2, 3)

    case BattleInput.opponentMove1 =>
      handleTrainerAction(trainer2, trainer1, 0)
    case BattleInput.opponentMove2 =>
      handleTrainerAction(trainer2, trainer1, 1)
    case BattleInput.opponentMove3 =>
      handleTrainerAction(trainer2, trainer1, 2)
    case BattleInput.opponentMove4 =>
      handleTrainerAction(trainer2, trainer1, 3)
    case _ => None
  }

  while battle.gameState == GameState.InProgress do
    if inputQueue(trainer1).isDefined && inputQueue(trainer2).isDefined then
      controller.registerAction(trainer1, inputQueue(trainer1).get)
      controller.registerAction(trainer2, inputQueue(trainer2).get)
      inputQueue(trainer1) = None
      inputQueue(trainer2) = None
    else
      // TODO: cambiare con if controller.isWaitingForActions
      // println("Waiting for actions from trainers...")

      controller.update()

  // TODO: move è un option, se non c'è un pokemon in campo
  def handleTrainerAction(
      trainer: Trainer,
      opponent: Trainer,
      moveIndex: Int
  ): Unit =
    trainer.inField.flatMap(_.moves.lift(moveIndex)).foreach { move =>
      val action =
        MoveAction(move, trainer.inField.get, opponent.inField.get)
      println(
        s"${trainer.name} selected move '${move.move.name}'"
      )
      inputQueue(trainer) = Some(action)
    }
