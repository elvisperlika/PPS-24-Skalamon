package it.unibo.skalamon.types

import it.unibo.skalamon.model.types.TypesRegister
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

//case class MutablePokemon(var _hp: Int, _types: TypePair)
//case class Move(_attack: Int, _types: PokemonType)

//extension (pokemon: MutablePokemon)
//  def attack(move: Move)(other: MutablePokemon): Unit = other._hp -= move._attack

/**
 * Test for types.
 */
class TypesTest extends AnyFlatSpec with should.Matchers {

//  val electric =
//  val earth = PokemonType("Earth")
//  val light: Move = Move(10, electric)
//  var pikachu1: MutablePokemon = MutablePokemon(100, (electric, earth))
//  var pikachu2: MutablePokemon = MutablePokemon(100, (electric, earth))

  "Type register" should "be empty" in {
    TypesRegister.size should equal(0)
  }

  it should "let add new Types" in {
    TypesRegister.createType("Fire")
    TypesRegister.size should equal(1)
  }

  it should "be empty on reset" in {
    TypesRegister.createType("Fire")
    TypesRegister.reset()
    TypesRegister.size should equal(0)
  }

  it should "throw IllegalArgumentException if want to create existing type" in {
    TypesRegister.reset()
    TypesRegister.createType("Fire")
    a [IllegalArgumentException] should be thrownBy TypesRegister.createType("Fire")
  }

}
