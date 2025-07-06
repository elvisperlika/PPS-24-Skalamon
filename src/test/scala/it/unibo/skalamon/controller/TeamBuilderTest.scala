package it.unibo.skalamon.controller

import it.unibo.skalamon.controller.teambuilder.TeamBuilderController
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for the team builder.
  */
class TeamBuilderTest extends AnyFlatSpec with should.Matchers:
  private val controller = TeamBuilderController()

  "TeamBuilderController" should "return all Pokémon" in:
    val allPokemon = controller.allPokemon
    allPokemon should not be empty
    allPokemon.map(_.name) should contain allOf (
      "Pikachu",
      "Rattata",
      "Dragonite",
      "Pelipper"
    )

  it should "return a dictionary of Pokémon" in:
    val all = controller.allPokemon
    val dictionary = controller.pokemonDictionary

    dictionary('A').name shouldBe all.head.name
    dictionary('B').name shouldBe all(1).name

  it should "build a trainer with a valid sequence" in:
    val all = controller.allPokemon
    val trainer = controller.buildTrainer("Ash", "ABCDH")
    trainer.name shouldBe "Ash"
    trainer.team.size shouldBe 5
    trainer.team.map(_.base.name) shouldBe List(
      all.head.name,
      all(1).name,
      all(2).name,
      all(3).name,
      all(7).name
    )
