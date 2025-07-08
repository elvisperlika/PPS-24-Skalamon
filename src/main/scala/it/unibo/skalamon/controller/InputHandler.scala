package it.unibo.skalamon.controller

import it.unibo.skalamon.controller.battle.BattleController
import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}

/** Handles user input for the battle controller.
  */
object InputHandler:

  /** Handles the trainer's move selection.
    * @param controller
    *   The battle controller.
    * @param source
    *   The trainer who is selecting the move.
    * @param target
    *   The trainer who is the target of the move.
    * @param moveIndex
    *   The index of the move selected by the trainer.
    */
  def handleTrainerMove(
      controller: BattleController,
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

  /** Handles the trainer's Pokémon switch.
    * @param controller
    *   The battle controller.
    * @param trainer
    *   The trainer who is switching Pokémon.
    * @param pokemonIndex
    *   The index of the Pokémon to switch to.
    */
  def handlePokemonSwitch(
      controller: BattleController,
      trainer: Trainer,
      pokemonIndex: Int
  ): Unit =
    if controller.isWaitingForActions then
      val availablePokemon = trainer.teamWithoutInField
      availablePokemon.lift(pokemonIndex) match
        case Some(pokemon) if pokemon.currentHP > 0 =>
          val teamNames = availablePokemon.map(_.base.name).mkString(", ")
          val currentInField =
            trainer.inField.map(_.base.name).getOrElse("None")
          println(s"team: $teamNames - inField: $currentInField")

          if !trainer.inField.contains(pokemon) then
            println(s"${trainer.name} switched to ${pokemon.base.name}")
            controller.registerAction(trainer, SwitchAction(pokemon))
          else
            println(s"${trainer.name} cannot switch to that Pokémon")

        case _ =>
          println("Cannot switch to that Pokémon, it is not available")
