package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.behavior.kind.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class BattlePokemonTest extends AnyFlatSpec with should.Matchers:
  private val Damage1 = 10
  private val Damage2 = 30
  private val StatStageUp = 2
  private val StatStageDown = -4
  private val StatStageDownMultiplier = 0.33
  private val StatStageUpMultiplier = 2.0

  "BattlePokemon" should "takeDamage" in:
    val pokemon = PokemonTestUtils.simplePokemon1

    pokemon
      .takeDamage(Damage1)
      .currentHP shouldEqual (pokemon.currentHP - Damage1)
    pokemon
      .takeDamage(Damage1)
      .takeDamage(Damage2)
      .currentHP shouldEqual (pokemon.currentHP - Damage1 - Damage2)

  "BattlePokemon" should "apply stat changes and update effective stat value" in:
    val pokemon = PokemonTestUtils.simplePokemon1
    val stat = Stat.Attack
    val baseValue = pokemon.base.baseStats.base(stat)

    pokemon.actualStats.base.getOrElse(stat, 0) shouldEqual baseValue

    val changed = pokemon.applyStatChange(StatChange(stat, StatStageUp))
    changed.actualStats.base.getOrElse(stat, 0)  shouldEqual (baseValue * StatStageUpMultiplier).toInt

    val changedAgain = pokemon.applyStatChange(StatChange(stat, StatStageDown))
    changedAgain.actualStats.base.getOrElse(stat, 0) shouldEqual (baseValue * StatStageDownMultiplier).toInt

  "BattlePokemon" should "die" in:
    val pokemon = PokemonTestUtils.simplePokemon1
    pokemon.takeDamage(pokemon.currentHP).isAlive should be(false)