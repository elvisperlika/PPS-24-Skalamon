package it.unibo.skalamon

import it.unibo.skalamon.controller.MainController
import it.unibo.skalamon.controller.teambuilder.TeamBuilderController
import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.view.teambuilder.TeamBuilderView

private val TrainersCount = 2

@main
def main(args: String*): Unit =
  if (args.headOption.contains("skip")) {
    startBattle(PokemonTestUtils.trainerAlice, PokemonTestUtils.trainerBob)
    return
  }

  startTeamBuilder: trainers =>
    trainers.size match
      case TrainersCount =>
        println("Starting the battle.")
        startBattle(trainers.head, trainers.tail.head)
      case _ =>
        println(s"Expected $TrainersCount trainers, but got ${trainers.size}. Restarting.")
        main(args*)

/** Starts the team builder.
  * @param onComplete
  *   A callback that is called when the team building is complete, with the
  *   list of trainers created.
  */
def startTeamBuilder(onComplete: List[Trainer] => Unit): Unit =
  val controller = TeamBuilderController()
  val view = TeamBuilderView(controller)
  view.start(onComplete)

/** Starts a battle between two trainers.
  */
def startBattle(trainer1: Trainer, trainer2: Trainer): Unit =
  val controller = MainController(trainer1, trainer2)
  controller.start()
