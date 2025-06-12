package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.GameState.{GameOver, InProgress}
import it.unibo.skalamon.controller.battle.turn.TurnControllerProxy
import it.unibo.skalamon.model.Battle
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

  /** Update view and model's data.
    */
  def update(): Unit

object BattleController:

  def apply(trainers: List[Trainer]): BattleController =
    new BasicBattleControllerImpl(trainers)

  private class BasicBattleControllerImpl(trainers: List[Trainer])
      extends BattleController():

    private val battleModel: Battle = Battle()
    private val controllerProxy: TurnControllerProxy = TurnControllerProxy(
      trainers.size
    )
    private val battleView: BattleView = BattleView(controllerProxy)
    private var state: GameState = InProgress

    def checkGameOver: GameState =
      if trainers.count(_.team.exists(!_.isKO)) == 1 then
        GameOver(trainers.find(_.team.exists(!_.isKO)).get)
      else InProgress

    override def update(): Unit =
      import scala.concurrent.ExecutionContext.Implicits.global
      import scala.util.{Failure, Success}
      while !state.isGameOver do
        battleView.updateTurn(i = battleModel.getTurnIndex)
        // battleModel.pokemonGetInField
        battleView.updatePokemon(???)
        battleView.showActions(map = ???)
        controllerProxy.getChosenActions.onComplete:
          case Success(actions) =>
            // battleModel.updateAfterActions(actions)
            battleView.updatePokemon(???)
          case Failure(ex) =>
            println(s"Failure: ${ex.getMessage}")
        state = checkGameOver
  //  battleView.showWinner(state.getWinner)
