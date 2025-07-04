package it.unibo.skalamon

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.controller.battle.{BattleController, GameState}
import it.unibo.skalamon.model.battle.{Battle, Trainer}
import it.unibo.skalamon.model.event.{BattleStateEvents, TurnStageEvents}
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.view.MainView
import it.unibo.skalamon.view.battle.{BattleInput, BattleView, PlayerSide}

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
  battle.eventManager.watch(TurnStageEvents.Started): turn =>
    mainView.repaint()
    battleView.update(turn.state.snapshot)

  battle.eventManager.watch(BattleStateEvents.Changed): (_, state) =>
    mainView.repaint()
    battleView.update(state)

  controller.start()
  mainView.setKeyPressedHandler { input =>
    val trainers = battle.currentTurn.head.state.snapshot.trainers
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
        handleMove(PlayerSide.Player, i.ordinal - BattleInput.playerMove1.ordinal)

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
    Thread.sleep(200) // TODO: is it necessary?
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

  def handlePokemonSwitch(
      trainer: Trainer,
      pokemonIndex: Int
  ): Unit =
    if controller.isWaitingForActions then
      println("team: " + trainer.teamWithoutInField.map(p =>
        p.base.name
      ).mkString(
        ", "
      ) + s" - inField: ${trainer.inField.map(_.base.name).getOrElse(None)}")
      trainer.teamWithoutInField.lift(pokemonIndex) match
        case Some(p) if p != trainer.inField =>
          println(s"${trainer.name} switched to ${p.base.name}")
          controller.registerAction(trainer, SwitchAction(p))
        case _ =>
          println(s"${trainer.name} cannot switch to that Pokémon")
