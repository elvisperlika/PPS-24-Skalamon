package it.unibo.skalamon

import it.unibo.skalamon.controller.MainController

@main
def main(): Unit =
  val trainer1 = PokemonTestUtils.trainerAlice
  val trainer2 = PokemonTestUtils.trainerBob

  val controller = MainController(trainer1, trainer2)
  controller.start()
