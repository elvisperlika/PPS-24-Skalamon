package it.unibo.skalamon.types

import it.unibo.skalamon.model.types.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

type PokemonType = (Type, Type) | Type
type MoveType = Type

case class MutablePokemon(var _hp: Int, _type: PokemonType)
case class Move(_attack: Int, _type: MoveType)

// extension (pokemon: MutablePokemon)
  // def attack(move: Move)(other: MutablePokemon): Unit = other._hp -= move._attack

/** Test for types.
  */
class TypesTest extends AnyFlatSpec with should.Matchers {

  "Pokémon" should "have one type" in {
    val pikachu: MutablePokemon = MutablePokemon(100, Electric)
    pikachu._type shouldBe Electric
  }

  it should "can have two types" in {
    val squirrel: MutablePokemon = MutablePokemon(120, (Electric, Water))
    squirrel._type match {
      case (t1: Type, t2: Type) =>
        t1 shouldBe Electric
        t2 shouldBe Water
      case t: Type => t.isInstanceOf[Type]
    }
  }

}
