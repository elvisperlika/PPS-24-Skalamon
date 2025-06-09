package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.turn.{
  TurnController,
  TurnControllerImpl
}
import it.unibo.skalamon.model.move.Action
import it.unibo.skalamon.model.{Battle, BattleImpl}
import it.unibo.skalamon.model.trainer.Trainer
import it.unibo.skalamon.view.battle.{BattleView, BattleViewImpl}

class BattleControllerImpl(t1: Trainer, t2: Trainer) extends BattleController {
  val battleView: BattleView = BattleViewImpl(this)
  val battle: Battle = BattleImpl()
  val turnController: TurnController = TurnControllerImpl()

  override def trainers: (Trainer, Trainer) = (t1, t2)
  
  /** Pick the action chosen from a trainer.
   * @param t
   *   is the Trainer
   * @param a
   *   is the Action chosen
   */
  def pickAction(t: Trainer, a: Action): Unit =
    turnController.addAction(t, a)

  /** Check if both trainers have picked own actions.
   * @return
   */
  private def haveAllTrainersPickedAction: Boolean =
    turnController.actions.size == 2

  /** Define turn as finished and create new turn.
   */
  private def endTurn(): Unit = ???

  override def isOver: Boolean = t1.team.forall(_.isKO) | t2.team.forall(_.isKO)

  override def getWinner: Option[Trainer] = ???
    
}
