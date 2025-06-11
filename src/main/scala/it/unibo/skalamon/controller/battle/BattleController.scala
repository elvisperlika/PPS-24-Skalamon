package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.turn.TurnControllerProxy
import it.unibo.skalamon.model.{Battle, BattleImpl}
import it.unibo.skalamon.util.Monad
import it.unibo.skalamon.view.battle.BattleView

/* start Temporary classes */
case class MutablePokemon(name: String, isKO: Boolean = false)

trait Action
case class Move() extends Action
case class Switch() extends Action

case class Trainer(name: String, team: List[MutablePokemon])
/* end Temporary classes */

/** Battle controller manage battle status and end game logic.
  */
trait BattleController:
  def trainers: List[Trainer]
  def isOver: Boolean
  def isDraw: Boolean
  def getWinner: Option[Trainer]
  def start: Unit
  def turnIndex: Int

object BattleController:

  def apply(trainers: List[Trainer]): BattleController =
    new BasicBattleControllerImpl(trainers)

  private class BasicBattleControllerImpl(_trainers: List[Trainer])
      extends BattleController():

    var _turnIndex: Int = 0
    val battleModel: Battle = BattleImpl()
    val controllerProxy: TurnControllerProxy = TurnControllerProxy(
      _trainers.size
    )
    val battleView: BattleView = BattleView(controllerProxy)

    override def trainers: List[Trainer] = _trainers
    override def isOver: Boolean = trainers.count(_.team.exists(!_.isKO)) <= 1
    override def isDraw: Boolean = trainers.forall(_.team.forall(_.isKO))
    override def getWinner: Option[Trainer] =
      if isOver && !isDraw then trainers.find(_.team.exists(!_.isKO))
      else None

    // TODO: use for-yield for this
    override def start: Unit =
      import scala.util.{Success, Failure}
      import scala.concurrent.ExecutionContext.Implicits.global
      if !isOver then
        _turnIndex += 1
        battleView.updateTurn(i = turnIndex)
        // battleModel.pokemonGetInField
        battleView.showActions(map = ???)
        controllerProxy.getChosenActions.onComplete {
          case Success(actions) =>
            // battleModel.updateWithActions(actions)
            battleView.updatePokemon(???)
            start
          case Failure(ex) =>
            println(s"Failure: ${ex.getMessage}")
        }

    override def turnIndex: Int = _turnIndex
