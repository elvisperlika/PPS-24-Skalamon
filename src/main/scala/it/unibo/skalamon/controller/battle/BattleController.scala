package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.turn.{
  TurnController,
  TurnControllerImpl
}

trait BattleController:

  var currentTurn: TurnController = TurnControllerImpl()

  /**
   * 
   * @return
   */
  def trainers: List[Trainer]

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
