package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.behavior.kind.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class BattlePokemonTest extends AnyFlatSpec with should.Matchers:
  private val StatStageUp = 2
  private val StatStageDown = -4
  private val StatStageDownMultiplier = 0.33
  private val StatStageUpMultiplier = 2.0

  "BattlePokemon" should "apply stat changes and update effective stat value" in:
    val pokemon = PokemonTestUtils.simplePokemon1
    val stat = Stat.Attack
    val baseValue = pokemon.base.stats.base(stat)

    println("--- " + pokemon.actualStats.base.getOrElse(stat, 0))
    pokemon.actualStats.base.getOrElse(stat, 0) shouldEqual baseValue

    val changed = pokemon.applyStatChange(StatChange(stat, StatStageUp))
    changed.actualStats.base.getOrElse(
      stat,
      0
    ) shouldEqual (baseValue * StatStageUpMultiplier).toInt

    val changedAgain = pokemon.applyStatChange(StatChange(stat, StatStageDown))
    changedAgain.actualStats.base.getOrElse(
      stat,
      0
    ) shouldEqual (baseValue * StatStageDownMultiplier).toInt
