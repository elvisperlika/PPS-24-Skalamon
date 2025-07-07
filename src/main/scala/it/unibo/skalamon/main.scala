package it.unibo.skalamon

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.controller.battle.{BattleController, GameState}
import it.unibo.skalamon.model.battle.{Battle, Trainer}
import it.unibo.skalamon.model.event.BattleStateEvents
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.view.MainView
import it.unibo.skalamon.view.battle.{BattleInput, BattleView, PlayerSide}
import it.unibo.skalamon.view.gameOver.GameOverView

@main
def main(): Unit =
  val sleepDurationMillis = 200

  val (trainer1, trainer2) =
    (PokemonTestUtils.trainerAlice, PokemonTestUtils.trainerBob)

  val battle = Battle(List(trainer1, trainer2))
  val controller = BattleController(battle)

  val mainView: MainView = MainView()
  mainView.setupView()

  val battleView = BattleView(mainView.getPlayScreen)
  val gameOverView = GameOverView(mainView.getGameOverScreen)

  battle.eventManager.watch(BattleStateEvents.Changed): (_, state) =>
    if battle.gameState == GameState.InProgress then
      mainView.repaint()
      battleView.update(state, controller.battle.turnIndex)

  battle.eventManager.watch(BattleStateEvents.Finished): winner =>
    mainView.repaint()
    gameOverView.update(winner)

  controller.start()
  controller.battle.currentTurn.foreach(turn =>
    battleView.update(turn.state.snapshot, controller.battle.turnIndex)
  )
  mainView.setKeyPressedHandler { input =>
    val trainers = battle.currentTurn.get.state.snapshot.trainers
    val player = trainers.head
    val opponent = trainers.last

    def handleMove(side: PlayerSide, index: Int): Unit = side match
      case PlayerSide.Player   => handleTrainerMove(player, opponent, index)
      case PlayerSide.Opponent => handleTrainerMove(opponent, player, index)

    def handleSwitch(side: PlayerSide, index: Int): Unit = side match
      case PlayerSide.Player   => handlePokemonSwitch(player, index)
      case PlayerSide.Opponent => handlePokemonSwitch(opponent, index)

    input match
      case i
          if BattleInput.playerMove1.ordinal to BattleInput.playerMove4.ordinal contains i.ordinal =>
        handleMove(
          PlayerSide.Player,
          i.ordinal - BattleInput.playerMove1.ordinal
        )

      case i
          if BattleInput.opponentMove1.ordinal to BattleInput.opponentMove4.ordinal contains i.ordinal =>
        handleMove(
          PlayerSide.Opponent,
          i.ordinal - BattleInput.opponentMove1.ordinal
        )

      case i
          if BattleInput.playerPokemon1.ordinal to BattleInput.playerPokemon5.ordinal contains i.ordinal =>
        handleSwitch(
          PlayerSide.Player,
          i.ordinal - BattleInput.playerPokemon1.ordinal
        )

      case i
          if BattleInput.opponentPokemon1.ordinal to BattleInput.opponentPokemon5.ordinal contains i.ordinal =>
        handleSwitch(
          PlayerSide.Opponent,
          i.ordinal - BattleInput.opponentPokemon1.ordinal
        )

      case _ => ()
  }

  while battle.gameState == GameState.InProgress do
    Thread.sleep(sleepDurationMillis)
    if !controller.isWaitingForActions then
      controller.update()

  def handleTrainerMove(
      source: Trainer,
      target: Trainer,
      moveIndex: Int
  ): Unit =
    for
      pokemon <- source.inField
      move <- pokemon.moves.lift(moveIndex)
    do
      if controller.isWaitingForActions then
        val action = MoveAction(move, source, target)
        println(s"${source.name} selected move '${move.move.name}'")
        controller.registerAction(source, action)

  def handlePokemonSwitch(trainer: Trainer, pokemonIndex: Int): Unit =
    if (controller.isWaitingForActions) then
      val availablePokemon = trainer.teamWithoutInField
      availablePokemon.lift(pokemonIndex) match
        case Some(pokemon) if pokemon.currentHP > 0 =>
          val teamNames = availablePokemon.map(_.base.name).mkString(", ")
          val currentInField =
            trainer.inField.map(_.base.name).getOrElse("None")
          println(s"team: $teamNames - inField: $currentInField")

          if (!trainer.inField.contains(pokemon)) {
            println(s"${trainer.name} switched to ${pokemon.base.name}")
            controller.registerAction(trainer, SwitchAction(pokemon))
          } else
            println(s"${trainer.name} cannot switch to that Pokémon")

        case _ =>
          println("Cannot switch to that Pokémon, it is not available")
