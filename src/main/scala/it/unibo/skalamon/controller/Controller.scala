package it.unibo.skalamon.controller

import it.unibo.skalamon.controller.battle.BattleController
import it.unibo.skalamon.model.battle.Battle

/** The main controller of the game.
  */
trait Controller:

  /** Creates a battle with 2 trainers, both of them with a team of Pokémon.
    *
    * @return
    *   The new battle controller.
    */
  def createBattle(battle: Battle): BattleController = BattleController(battle)
