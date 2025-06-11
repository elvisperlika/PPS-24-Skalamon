package it.unibo.skalamon.controller

import it.unibo.skalamon.controller.battle.{
  BattleController,
  MutablePokemon,
  Trainer
}

/** Controller
  */
trait Controller {

  /** Create a battle with 2 trainers, both of them with a team of Pokémon.
    *
    * @return
    *   BattleController to manage the battle
    */
  def createBattle(): Unit =
    val battleController: BattleController = BattleController(
      List(
        Trainer("Bob", List(MutablePokemon("Pikachu"))),
        Trainer("Alice", List(MutablePokemon("Squirrel")))
      )
    )
    // viewCoordinator show battleController.view
    battleController.start

}
