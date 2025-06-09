package it.unibo.skalamon.controller.battle.turn

import it.unibo.skalamon.controller.battle.{Action, Trainer}

class TurnControllerImpl extends TurnController {

  override def addAction(t: Trainer, a: Action): Unit =
    actions = actions + (t -> a)
}
