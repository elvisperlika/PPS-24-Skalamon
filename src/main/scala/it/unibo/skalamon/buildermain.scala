package it.unibo.skalamon

import it.unibo.skalamon.controller.teambuilder.TeamBuilderController
import it.unibo.skalamon.view.teambuilder.TeamBuilderView

// TEMPORARY

@main
def _main(): Unit =
  val controller = TeamBuilderController()
  val view = TeamBuilderView(controller)

  view.start: trainers =>
    println("Team building complete")