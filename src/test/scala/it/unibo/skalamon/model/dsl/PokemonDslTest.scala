package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.pokemon.Stat
import it.unibo.skalamon.model.types.TypesCollection.{Electric, Fire, Flying}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for building Pokémon via DSL. */
class PokemonDslTest extends AnyFlatSpec with should.Matchers:

  "Pokemon DSL" should "create with one type" in:
    val pikachu = pokemon("Pikachu"):
      _ typed Electric weighing 6.0.kg

    pikachu.name shouldBe "Pikachu"
    pikachu.types shouldBe Electric

  it should "allow combining types" in:
    val charizard = pokemon("Charizard"):
      _ typed Fire + Flying weighing 90.5.kg

    charizard.types shouldBe Fire :: Flying :: Nil

  it should "throw an exception if types are not defined" in:
    an[IllegalArgumentException] should be thrownBy:
      pokemon("Unknown")(identity)

  it should "allow setting stats" in:
    val bulbasaur = pokemon("Pikachu"):
      _ typed Electric weighing 6.0.kg stat Stat.Attack -> 20 stat Stat.Defense -> 10

    bulbasaur.baseStats.base shouldBe Map(
      Stat.Attack -> 20,
      Stat.Defense -> 10
    )
