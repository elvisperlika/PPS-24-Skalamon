package it.unibo.skalamon.view.teambuilder

import it.unibo.skalamon.controller.teambuilder.TeamBuilderController
import it.unibo.skalamon.model.battle.Trainer

import scala.io.StdIn.readLine

/** A view for building a team of Pokémon for battle.
  */
class TeamBuilderView(private val controller: TeamBuilderController):
  private val NumTrainers = 2

  /** Starts the team builder view.
    */
  def start(onComplete: List[Trainer] => Unit): Unit =
    println("Welcome to the Skalamon team builder.\n")

    val all = controller.allPokemon
    val labelWidth = 5
    val nameWidth = all.map(_.name.length).max + 1
    val typeWidth = 20
    val abilityWidth = all.map(_.ability.name.length).max + 1
    val movesWidth = 30
    val fmt =
      s"%-${labelWidth}s %-${nameWidth}s %-${typeWidth}s %-${abilityWidth}s %-${movesWidth}s"

    println(fmt.format(
      "Char",
      "Name",
      "Types",
      "Ability",
      "Moves"
    ))
    println("-" * (labelWidth + nameWidth + typeWidth + abilityWidth + movesWidth))

    controller.pokemonDictionary.toSeq
      .sorted(Ordering.by(_._1))
      .foreach { case (char, pokemon) =>
        println(
          fmt.format(
            char,
            pokemon.name,
            pokemon.types.mkString(" + "),
            pokemon.ability.name,
            pokemon.moves.map(_.name).mkString(", ")
          )
        )
      }

    val trainers = (1 to NumTrainers).map(buildTrainer).toList
    onComplete(trainers)

  private def buildTrainer(index: Int): Trainer =
    println()
    val trainerName = readLine(s"Enter the name of Trainer $index: ")
    val teamString =
      readLine(s"Enter the team for Trainer $index (each char is a Pokémon): ")
    val trainer = controller.buildTrainer(trainerName, teamString)
    println(
      s"$trainerName's team: ${trainer.team.map(_.base.name).mkString(", ")}"
    )
    trainer
