package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.types.TypesCollection.{Electric, Fire, Flying}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for building Pokémon via DSL. */
class PokemonDslTest extends AnyFlatSpec with should.Matchers:

  "Pokemon DSL" should "create with one type" in {
    val pikachu = pokemon("Pikachu"):
      _ typed Electric

    pikachu.name shouldBe "Pikachu"
    pikachu.types shouldBe Electric
  }

  it should "allow combining types" in {
    val charizard = pokemon("Charizard"):
      _ typed (Fire and Flying)

    charizard.types shouldBe Fire :: Flying :: Nil
  }

  it should "throw an exception if types are not defined" in {
    an[IllegalArgumentException] should be thrownBy {
      pokemon("Unknown")(identity)
    }
  }
