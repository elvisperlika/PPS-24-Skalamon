package it.unibo.skalamon.controller.battle.turn

import it.unibo.skalamon.controller.battle.{Action, Trainer}

trait TurnController {

  /** Actions map.
    */
  var actions: Map[Trainer, Action] = Map()

  /** Add new Action in 'actions' map.
    * @param t
    *   is the Trainer
    * @param a
    *   is the Action
    */
  def addAction(t: Trainer, a: Action): Unit

}
