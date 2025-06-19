package it.unibo.skalamon.model.types

import it.unibo.skalamon.model.types.*
import it.unibo.skalamon.model.types.TypesCollection.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

case class TestMutablePokemon(var _hp: Int, _type: PokemonType)
case class TestMove(_attack: Int, _type: MoveType)

extension (pokemon: TestMutablePokemon)
  def attack(move: TestMove)(other: TestMutablePokemon): Unit =
    println(other._hp)
    other._hp = other._hp - (move._attack * TypeUtility
      .calculateMultiplier(move._type.computeEffectiveness(other._type))).toInt

/** Test for types.
  */
class TypesTest extends AnyFlatSpec with should.Matchers:

  "Pokémon" should "have one type" in:
    val pikachu: TestMutablePokemon = TestMutablePokemon(100, Electric)
    pikachu._type shouldBe Electric

  it should "can have two types" in:
    val squirrel: TestMutablePokemon =
      TestMutablePokemon(120, List(Electric, Water))
    squirrel._type match
      case list: List[Type] =>
        list shouldBe List(Electric, Water)
      case t: Type => t.isInstanceOf[Type]

  "Type utility" should "calculate multiplier" in:
    val efficacyList: List[Efficacy] =
      Electric.computeEffectiveness(List(Water, Grass, Electric))
    TypeUtility.calculateMultiplier(efficacyList) shouldBe 0.5

  it should "calculate multiplier pt.2" in:
    val efficacyList: List[Efficacy] =
      Steel.computeEffectiveness(List(Fire, Water, Electric))
    TypeUtility.calculateMultiplier(efficacyList) shouldBe 0.125

  it should "calculate multiplier pt.3" in:
    val efficacyList: List[Efficacy] =
      Dragon.computeEffectiveness(List(Dragon, Fairy))
    TypeUtility.calculateMultiplier(efficacyList) shouldBe 0.0

  "Moves" should "have one type" in:
    val light: TestMove = TestMove(10, Electric)
    light._type shouldBe Electric

  "Electric move" should "be resisted on Electric Pokémon" in:
    val pikachu: TestMutablePokemon = TestMutablePokemon(100, Electric)
    val pikachu2: TestMutablePokemon = TestMutablePokemon(100, Electric)
    val light: TestMove = TestMove(10, Electric)
    pikachu.attack(light)(pikachu2)
    pikachu2._hp shouldBe 95

  "Electric move" should "be super effective on Water Pokémon" in:
    val pikachu: TestMutablePokemon = TestMutablePokemon(100, Electric)
    val squirrel: TestMutablePokemon = TestMutablePokemon(100, Water)
    val light: TestMove = TestMove(10, Electric)
    pikachu.attack(light)(squirrel)
    squirrel._hp shouldBe 80

  "Electric move" should "be ineffective on Ground Pokémon" in:
    val pikachu: TestMutablePokemon = TestMutablePokemon(100, Electric)
    val diglett: TestMutablePokemon = TestMutablePokemon(100, Ground)
    val light: TestMove = TestMove(10, Electric)
    pikachu.attack(light)(diglett)
    diglett._hp shouldBe 100

  "Electric move" should "be effective oxn Water-Grass Pokémon" in:
    val pikachu: TestMutablePokemon = TestMutablePokemon(100, Electric)
    val lotad: TestMutablePokemon = TestMutablePokemon(100, List(Grass, Water))
    val light: TestMove = TestMove(10, Electric)
    pikachu.attack(light)(lotad)
    lotad._hp shouldBe 90

  "Electric move" should "be 2x Super Effective on Water-Flying Pokémon" in:
    val pikachu: TestMutablePokemon = TestMutablePokemon(100, Electric)
    val gyarados: TestMutablePokemon =
      TestMutablePokemon(100, List(Water, Flying))
    val light: TestMove = TestMove(10, Electric)
    pikachu.attack(light)(gyarados)
    gyarados._hp shouldBe 60
