package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.turn.{
  TurnController,
  TurnControllerImpl
}
import it.unibo.skalamon.model.{Battle, BattleImpl}
import it.unibo.skalamon.view.battle.{BattleView, BattleViewImpl}

/* start Temporary classes */
case class MutablePokemon(name: String, isKO: Boolean = false)

trait Action
case class Move() extends Action
case class Switch() extends Action

case class Trainer(name: String, team: List[MutablePokemon])
/* end Temporary classes */

class BattleControllerImpl(_trainers: List[Trainer]) extends BattleController {
  val battleView: BattleView = BattleViewImpl(this)
  val battle: Battle = BattleImpl()
  val turnController: TurnController = TurnControllerImpl()

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
    turnController.actions.size == _trainers.size

  /** Define turn as finished and create new turn.
    */
  private def endTurn(): Unit = ???

  override def isOver: Boolean =
    trainers.count(_.team.forall(_.isKO)) == (trainers.size - 1)

  override def isDraw: Boolean =
    trainers.forall(_.team.forall(_.isKO))

  override def getWinner: Option[Trainer] =
    if isOver then trainers.find(_.team.exists(!_.isKO))
    else None

  override def trainers: List[Trainer] = _trainers
}
