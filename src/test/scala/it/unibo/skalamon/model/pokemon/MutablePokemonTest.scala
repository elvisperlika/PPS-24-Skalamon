package it.unibo.skalamon.model.pokemon

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/**
 * Tests for [[MutablePokemon]] and its implementations.
 */
class MutablePokemonTest extends AnyFlatSpec with should.Matchers {
  "MutablePokemon" should "takeDamage" in:
    val pokemon = PokemonTestUtils.simplePokemon
    val damage1 = 10
    val damage2 = 30
    
    pokemon.takeDamage(damage1).currentHP shouldEqual (pokemon.currentHP - damage1)
    pokemon.takeDamage(damage1).takeDamage(damage2).currentHP shouldEqual (pokemon.currentHP - damage1 - damage2)
    
  "MutablePokemon" should "die" in:
    val pokemon = PokemonTestUtils.simplePokemon
    pokemon.takeDamage(pokemon.currentHP).isAlive should be (false)
}
