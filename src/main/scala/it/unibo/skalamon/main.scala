package it.unibo.skalamon

import it.unibo.skalamon.controller.battle.action.MoveAction
import it.unibo.skalamon.controller.battle.{BattleController, GameState}
import it.unibo.skalamon.model.battle.{Battle, Trainer}
import it.unibo.skalamon.model.event.{BattleStateEvents, TurnStageEvents}
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.view.MainView
import it.unibo.skalamon.view.battle.{BattleInput, BattleView}

@main
def main(): Unit =
  var (trainer1, trainer2) =
    (PokemonTestUtils.trainerAlice, PokemonTestUtils.trainerBob)
  trainer1 = trainer1.copy(_inField = Some(trainer1.team.head))
  trainer2 = trainer2.copy(_inField = Some(trainer2.team.head))

  val battle = Battle(List(trainer1, trainer2))
  val controller = BattleController(battle)

  val mainView: MainView = MainView()
  mainView.setupView()

  val battleView = BattleView(mainView.getPlayScreen)

  // TODO: vedere se lasciarlo. Lo ho messo perche il primo turno a volte non viene visualizzata l'intera view
  battle.eventManager.watch(TurnStageEvents.Started): (turn) =>
    mainView.repaint()
    battleView.update(turn.state.snapshot)

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
    Thread.sleep(200) // TODO: is it necessary?
    if !controller.isWaitingForActions then
      controller.update()

  def handleTrainerAction(
      trainer: Trainer,
      opponent: Trainer,
      moveIndex: Int
  ): Unit =
    for
      pokemon <- trainer.inField
      target <- opponent.inField
      move <- pokemon.moves.lift(moveIndex)
    do
      if controller.isWaitingForActions then
        val action = MoveAction(move, pokemon, target)
        println(s"${trainer.name} selected move '${move.move.name}'")
        controller.registerAction(trainer, action)
