package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.behavior.kind.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[BattlePokemon]] and its implementations.
  */
class BattlePokemonTest extends AnyFlatSpec with should.Matchers:
  "BattlePokemon" should "takeDamage" in:
    val pokemon = PokemonTestUtils.simplePokemon1
    val damage1 = 10
    val damage2 = 30

    pokemon
      .takeDamage(damage1)
      .currentHP shouldEqual (pokemon.currentHP - damage1)
    pokemon
      .takeDamage(damage1)
      .takeDamage(damage2)
      .currentHP shouldEqual (pokemon.currentHP - damage1 - damage2)

  "BattlePokemon" should "apply stat changes and update effective stat value" in:
    val pokemon = PokemonTestUtils.simplePokemon1
    val stat = Stat.Attack
    val baseValue = pokemon.base.baseStats.base(stat)

    pokemon.actualStats.effective(stat) shouldEqual baseValue

    val changed = pokemon.applyStatChange(StatChange(stat, 2))
    changed.actualStats.effective(stat) shouldEqual baseValue * 2.0

    val changedAgain = changed.applyStatChange(StatChange(stat, -4))
    changedAgain.actualStats.effective(stat) shouldEqual baseValue * 0.5

  "BattlePokemon" should "die" in:
    val pokemon = PokemonTestUtils.simplePokemon1
    pokemon.takeDamage(pokemon.currentHP).isAlive should be(false)
