package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.turn.{
  TurnController,
  TurnControllerImpl
}

trait BattleController:

  var currentTurn: TurnController = TurnControllerImpl()

  /** @return
    *   trainers list
    */
  def trainers: List[Trainer]

  /** Check if battle is finished.
    * @return
    *   if all trainer's teams are KO or all of them except one.
    */
  def isOver: Boolean

  /** Check if battle is finished without a Winner.
    * @return
    *   true if all trainer's teams are KO
    */
  def isDraw: Boolean

  /** Get winner if battle is over.
    * @return
    *   optionally the Winner trainer
    */
  def getWinner: Option[Trainer]
