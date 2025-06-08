package it.unibo.skalamon.controller

import it.unibo.skalamon.controller.battle.BattleController
import it.unibo.skalamon.model.Battle
import it.unibo.skalamon.view.View

class ControllerImpl(model: Battle) extends Controller {

  override def run(): Unit = ???

  override def attachView(view: View): Unit = ???

  override def createBattle(): BattleController = ???
}
