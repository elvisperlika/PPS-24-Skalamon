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
  /** Get list of trainers in Battle.
    * @return
    *   Trainers List
    */
  def trainers: List[Trainer]

  /** @return
    *   True if all trainers or all trainers except one have the team KO.
    */
  def isOver: Boolean

  /** @return
    *   True if all trainers have the team KO.
    */
  def isDraw: Boolean

  /** Winner getter.
    * @return
    *   Optionally the winner Trainer.
    */
  def getWinner: Option[Trainer]

  /** Update view and model's data.
    */
  def update(): Unit

  /** @return
    *   the turn index.
    */
  def getTurnIndex: Int

object BattleController:

  def apply(trainers: List[Trainer]): BattleController =
    new BasicBattleControllerImpl(trainers)

  private class BasicBattleControllerImpl(_trainers: List[Trainer])
      extends BattleController():

    var _turnIndex: Int = 0
    val _battleModel: Battle = BattleImpl()
    val _controllerProxy: TurnControllerProxy = TurnControllerProxy(
      _trainers.size
    )
    val _battleView: BattleView = BattleView(_controllerProxy)

    override def trainers: List[Trainer] = _trainers
    override def isOver: Boolean = trainers.count(_.team.exists(!_.isKO)) <= 1
    override def isDraw: Boolean = trainers.forall(_.team.forall(_.isKO))
    override def getWinner: Option[Trainer] =
      if isOver && !isDraw then trainers.find(_.team.exists(!_.isKO))
      else None

    // TODO: use for-yield for this
    override def update(): Unit =
      import scala.util.{Success, Failure}
      import scala.concurrent.ExecutionContext.Implicits.global
      while !isOver do
        _turnIndex += 1
        _battleView.updateTurn(i = getTurnIndex)
        // _battleModel.pokemonGetInField
        _battleView.updatePokemon(???)
        _battleView.showActions(map = ???)
        _controllerProxy.getChosenActions.onComplete:
          case Success(actions) =>
            // _battleModel.updateWithActions(actions)
            _battleView.updatePokemon(???)
          case Failure(ex) =>
            println(s"Failure: ${ex.getMessage}")

    override def getTurnIndex: Int = _turnIndex
