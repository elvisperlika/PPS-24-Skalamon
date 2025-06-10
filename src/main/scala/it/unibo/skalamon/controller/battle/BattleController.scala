package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.turn.TurnControllerProxy
import it.unibo.skalamon.model.{Battle, BattleImpl}
import it.unibo.skalamon.view.battle.BattleView

/* start Temporary classes */
case class MutablePokemon(name: String, isKO: Boolean = false)

trait Action
case class Move() extends Action
case class Switch() extends Action

case class Trainer(name: String, team: List[MutablePokemon])
/* end Temporary classes */

trait BattleController:
  private val controllerProxy: TurnControllerProxy = TurnControllerProxy()
  val battleView: BattleView = BattleView(controllerProxy)
  def trainers: List[Trainer]
  def isOver: Boolean
  def isDraw: Boolean
  def getWinner: Option[Trainer]
  def update: Unit

object BattleController:

  def apply(trainers: List[Trainer]): BattleController =
    new BasicBattleControllerImpl(trainers)

  private class BasicBattleControllerImpl(_trainers: List[Trainer])
      extends BattleController:

    val battleModel: Battle = BattleImpl()
    var turnController: TurnControllerProxy = TurnControllerProxy()

    override def trainers: List[Trainer] = _trainers
    override def isOver: Boolean = trainers.count(_.team.exists(!_.isKO)) <= 1
    override def isDraw: Boolean = trainers.forall(_.team.forall(_.isKO))
    override def getWinner: Option[Trainer] =
      if isOver && !isDraw then trainers.find(_.team.exists(!_.isKO))
      else None
    // TODO: use for-yield for this
    override def update: Unit = ???
//        battleView.updateTurn(i = ???)
//        battleModel.firstUpdate
//        battleView.showActions(map = ???)
//        controllerProxy.getChoosenActions
//        battleModel.updateWithActions
//        battleView.updatePokemon(???)
