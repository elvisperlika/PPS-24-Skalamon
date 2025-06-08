package it.unibo.skalamon

import it.unibo.skalamon.controller.{Controller, ControllerImpl}
import it.unibo.skalamon.model.{Battle, BattleImpl}
import it.unibo.skalamon.view.{View, ViewImpl}


@main
def main(): Unit =
  val model: Battle = BattleImpl()
  val view: View = ViewImpl()
  val controller: Controller = ControllerImpl(model)
  controller.attachView(view)
  controller.run()
