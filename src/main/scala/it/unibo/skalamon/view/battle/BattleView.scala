package it.unibo.skalamon.view.battle

import it.unibo.skalamon.controller.battle.action.*
import it.unibo.skalamon.model.battle.{BattleState, Trainer}
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.view.screen.BattleScreen

trait BattleView:

  /** Update the whole battle view.
    * @param battleState
    *   The current state of the battle.
    */
  def update(battleState: BattleState): Unit

/** Provides a factory method to create a new BattleView instance.
  */
object BattleView:
  def apply(screen: BattleScreen): BattleView =
    new BattleViewImpl(screen)

  /** Create a new BattleView.
    * @param screen
    *   The screen to be used for the battle view.
    * @param controllerProxy
    *   The action buffer to be used for the battle view.
    * @return
    *   A new instance of BattleView.
    */
  private class BattleViewImpl(
      screen: BattleScreen
  ) extends BattleView:
    /** Update the whole battle view.
      * @param battleState
      *   The current state of the battle.
      */
    override def update(battleState: BattleState): Unit =
      val trainers = battleState.trainers
      if (trainers.size != BattleScreen.playerNumber) then
        throw new IllegalArgumentException(
          s"Expected ${BattleScreen.playerNumber} trainers, but got ${trainers.size}."
        )

      val Seq(player, opponent) = trainers

      def teamWithoutInField(trainer: Trainer): List[BattlePokemon] =
        trainer.inField.map(p => trainer.team.filterNot(_.id == p.id))
          .getOrElse(trainer.team)

      val pTeam = teamWithoutInField(player)
      val oTeam = teamWithoutInField(opponent)

      screen.setPlayersName(player.name, opponent.name)
      screen.setBattlePokemon(player.inField, opponent.inField)
      screen.setPokemonTeam(pTeam, oTeam)

      val pMoves: List[BattleMove] =
        player.inField.map(_.moves).getOrElse(List.empty)

      val oMoves: List[BattleMove] =
        opponent.inField.map(_.moves).getOrElse(List.empty)

      screen.setMoves(pMoves, oMoves)
