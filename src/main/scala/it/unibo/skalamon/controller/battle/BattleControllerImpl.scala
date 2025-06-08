package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.model.trainer.Trainer

class BattleControllerImpl(t1: Trainer, t2: Trainer) extends BattleController {

  override def trainers: (Trainer, Trainer) = (t1, t2)
}
