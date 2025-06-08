package it.unibo.skalamon.types

import it.unibo.skalamon.model.types.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

case class TestMutablePokemon(var _hp: Int, _type: PokemonType)
case class TestMove(_attack: Int, _type: MoveType)

extension (pokemon: TestMutablePokemon)
  def attack(move: TestMove)(other: TestMutablePokemon): Unit =
    other._hp = other._hp - (move._attack * move._type.computeEffectiveness(
      other._type
    )).toInt

/** Test for types.
  */
class TypesTest extends AnyFlatSpec with should.Matchers {

  "Pokémon" should "have one type" in {
    val pikachu: TestMutablePokemon = TestMutablePokemon(100, Electric)
    pikachu._type shouldBe Electric
  }

  it should "can have two types" in {
    val squirrel: TestMutablePokemon =
      TestMutablePokemon(120, (Electric, Water))
    squirrel._type match {
      case (t1: Type, t2: Type) =>
        t1 shouldBe Electric
        t2 shouldBe Water
      case t: Type => t.isInstanceOf[Type]
    }
  }

  "Moves" should "have one type" in {
    val light: TestMove = TestMove(10, Electric)
    light._type shouldBe Electric
  }

  "Electric move" should "be resisted on Electric Pokémon" in {
    val pikachu: TestMutablePokemon = TestMutablePokemon(100, Electric)
    val pikachu2: TestMutablePokemon = TestMutablePokemon(100, Electric)
    val light: TestMove = TestMove(10, Electric)
    pikachu.attack(light)(pikachu2)
    pikachu2._hp shouldBe (95)
  }

}
