package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.model.trainer.Trainer

trait BattleController:
  def trainers: (Trainer, Trainer)
