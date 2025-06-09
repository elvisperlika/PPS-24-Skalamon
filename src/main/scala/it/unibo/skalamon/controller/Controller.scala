package it.unibo.skalamon.controller

import it.unibo.skalamon.controller.battle.BattleController

/** Controller
  */
trait Controller {

  /** Create a battle with 2 trainers, both of them with a team of Pokémon.
    *
    * @return
    *   BattleController to manage the battle
    */
  def createBattle(): BattleController

}
