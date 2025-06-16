package it.unibo.skalamon.view.battle

import it.unibo.skalamon.controller.battle.turn.TurnControllerProxy
import it.unibo.skalamon.controller.battle.{Action, MutablePokemon, Trainer}

trait BattleView:

  /** Update turn index.
    * @param i
    *   Is the Turn's index.
    */
  def updateTurn(i: Int): Unit

  /** Show users available actions.
    * @param map
    *   Map: Trainer -> Pokémon's in Field actions
    */
  def showActions(map: Map[Trainer, List[Action]]): Unit

  /** Update Pokémon in field.
    * @param map
    *   Map: Trainer -> MutablePokémon
    */
  def updatePokemon(map: Map[Trainer, MutablePokemon]): Unit

object BattleView {
  def apply(controllerProxy: TurnControllerProxy): BattleView =
    new BattleViewImpl(controllerProxy)

  private class BattleViewImpl(_controllerProxy: TurnControllerProxy)
      extends BattleView {
    private val controllerProxy = _controllerProxy

    override def updateTurn(i: Int): Unit = ???

    override def showActions(map: Map[Trainer, List[Action]]): Unit = ???

    override def updatePokemon(map: Map[Trainer, MutablePokemon]): Unit = ???
  }
}
