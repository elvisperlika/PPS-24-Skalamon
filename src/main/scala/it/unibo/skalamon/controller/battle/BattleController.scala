package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.turn.{
  TurnController,
  TurnControllerImpl
}
import it.unibo.skalamon.model.trainer.Trainer

trait BattleController:

  var currentTurn: TurnController = TurnControllerImpl()

  /** Get trainers on battle.
    * @return
    *   tuple of trainers.
    */
  def trainers: (Trainer, Trainer)

  /** Check if battle is finished.
    *
    * @return
    *   true if battle completed, false otherwise
    */
  def isOver: Boolean

  /** Get winner if battle is over.
    * @return
    *   optionally the Winner trainer
    */
  def getWinner: Option[Trainer]
