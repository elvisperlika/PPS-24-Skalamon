package it.unibo.skalamon.view.battle

import it.unibo.skalamon.controller.battle.{
  Action,
  BattleController,
  MutablePokemon
}

class BattleViewImpl(bc: BattleController) extends BattleView {

  /** Update turn number on view.
    * @param n
    *   is the new turn value
    */
  private def updateTurn(n: Int): Unit = ???

  /** Update mutable Pokémon.
    * @param p
    *   is the new Pokémon status.
    */
  private def updateMutablePokemon(p: MutablePokemon): Unit = ???

  /** Show actions available with Pokémon in battle.
    * @param actions
    *   list
    */
  private def showActions(actions: List[Action]): Unit = ???

}
