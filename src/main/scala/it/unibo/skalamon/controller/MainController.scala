package it.unibo.skalamon.controller

import it.unibo.skalamon.*
import it.unibo.skalamon.model.battle.{Battle, Trainer}
import it.unibo.skalamon.controller.battle.{BattleController, GameState}
import it.unibo.skalamon.model.event.BattleStateEvents
import it.unibo.skalamon.view.*
import it.unibo.skalamon.view.battle.BattleView
import it.unibo.skalamon.view.gameOver.GameOverView

/** The main controller for the game, responsible for managing the battle
  * between two trainers and updating the views accordingly.
  * @param trainer1
  *   The first trainer participating in the battle.
  * @param trainer2
  *   The second trainer participating in the battle.
  * @param sleepDurationMillis
  *   The duration in milliseconds to sleep between game loop iterations.
  */
class MainController(
    trainer1: Trainer,
    trainer2: Trainer,
    sleepDurationMillis: Long = 200
):
  private val battle = Battle(List(trainer1, trainer2))
  private val controller = BattleController(battle)

  private val mainView: MainView = MainView()
  private val battleView: BattleView = BattleView(mainView.getPlayScreen)
  private val gameOverView: GameOverView =
    GameOverView(mainView.getGameOverScreen)

    /** Starts the main controller, setting up the views and input handlers.
      */
  def start(): Unit =
    mainView.setupView()

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

    setupInputHandler()

    gameLoop()

  /** Sets up the input handler for the main view.
    *
    * This method listens for key presses and handles them according to the
    * current game state, allowing players to select moves or switch Pokémon.
    */
  private def setupInputHandler(): Unit =
    mainView.setKeyPressedHandler { input =>
      val trainers = battle.currentTurn.get.state.snapshot.trainers
      val player = trainers.head
      val opponent = trainers.last

      /** Handles the player's move selection.
        *
        * @param side
        *   The side of the player (Player or Opponent).
        * @param index
        *   The index of the move to be executed.
        */
      def handleMove(side: PlayerSide, index: Int): Unit =
        side match
          case PlayerSide.Player =>
            InputHandler.handleTrainerMove(controller, player, opponent, index)
          case PlayerSide.Opponent =>
            InputHandler.handleTrainerMove(controller, opponent, player, index)

      /** Handles the player's Pokémon switch.
        * @param side
        *   The side of the player (Player or Opponent).
        * @param index
        *   The index of the Pokémon to be switched in.
        */
      def handleSwitch(side: PlayerSide, index: Int): Unit =
        side match
          case PlayerSide.Player =>
            InputHandler.handlePokemonSwitch(controller, player, index)
          case PlayerSide.Opponent =>
            InputHandler.handlePokemonSwitch(controller, opponent, index)

      input match
        case i
            if InputKeyWords.playerMove1.ordinal to InputKeyWords.playerMove4.ordinal contains i.ordinal =>
          handleMove(
            PlayerSide.Player,
            i.ordinal - InputKeyWords.playerMove1.ordinal
          )

        case i
            if InputKeyWords.opponentMove1.ordinal to InputKeyWords.opponentMove4.ordinal contains i.ordinal =>
          handleMove(
            PlayerSide.Opponent,
            i.ordinal - InputKeyWords.opponentMove1.ordinal
          )

        case i
            if InputKeyWords.playerPokemon1.ordinal to InputKeyWords.playerPokemon5.ordinal contains i.ordinal =>
          handleSwitch(
            PlayerSide.Player,
            i.ordinal - InputKeyWords.playerPokemon1.ordinal
          )

        case i
            if InputKeyWords.opponentPokemon1.ordinal to InputKeyWords.opponentPokemon5.ordinal contains i.ordinal =>
          handleSwitch(
            PlayerSide.Opponent,
            i.ordinal - InputKeyWords.opponentPokemon1.ordinal
          )

        case _ => ()
    }

  /** The main game loop that runs until the battle is finished.
    *
    * This loop checks the game state and updates the controller if it is not
    * waiting for actions. It sleeps for a specified duration to control the
    * update frequency.
    */
  private def gameLoop(): Unit =
    while battle.gameState == GameState.InProgress do
      Thread.sleep(sleepDurationMillis)
      if !controller.isWaitingForActions then
        controller.update()
