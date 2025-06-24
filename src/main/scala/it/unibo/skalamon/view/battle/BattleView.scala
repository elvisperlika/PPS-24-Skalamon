package it.unibo.skalamon.view.battle

import it.unibo.skalamon.controller.battle.action.*
import it.unibo.skalamon.model.battle.BattleState
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
  def apply(screen: BattleScreen, controllerProxy: ActionBuffer): BattleView =
    new BattleViewImpl(screen, controllerProxy)

  /** Create a new BattleView.
    * @param screen
    *   The screen to be used for the battle view.
    * @param controllerProxy
    *   The action buffer to be used for the battle view.
    * @return
    *   A new instance of BattleView.
    */
  private class BattleViewImpl(
      screen: BattleScreen,
      _controllerProxy: ActionBuffer
  ) extends BattleView:
    private val controllerProxy = _controllerProxy

    /** Update the whole battle view.
      * @param battleState
      *   The current state of the battle.
      */
    override def update(battleState: BattleState): Unit =
      val trainers = battleState.trainers
      if (trainers.size != BattleScreen.playerNumber) {
        throw new IllegalArgumentException(
          s"Expected ${BattleScreen.playerNumber} trainers, but got ${trainers.size}."
        )
      }

      val player = trainers(0)
      val opponent = trainers(1)

      screen.setPlayersName(player.name, opponent.name)
      screen.setBattlePokemon(player.inField, opponent.inField)
      screen.setPokemonPool(player.team, opponent.team)
