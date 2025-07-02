package it.unibo.skalamon.model.behavior.damage

import it.unibo.skalamon.model.battle.{BattleState, Trainer}
import it.unibo.skalamon.model.behavior.damage.DamageCalculatorGen1.calculate
import it.unibo.skalamon.model.behavior.kind.SimpleSingleHitBehavior
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.dsl.*
import it.unibo.skalamon.model.field.field
import it.unibo.skalamon.model.field.room.TrickRoom
import it.unibo.skalamon.model.field.terrain.Electrified
import it.unibo.skalamon.model.field.weather.Sunny
import it.unibo.skalamon.model.move.MoveModel.Accuracy.Of
import it.unibo.skalamon.model.move.MoveModel.Category.{Physical, Special}
import it.unibo.skalamon.model.move.{BattleMove, Move}
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.*
import it.unibo.skalamon.model.pokemon.Stat.{
  Attack,
  Defense,
  SpecialAttack,
  SpecialDefense
}
import it.unibo.skalamon.model.types.TypesCollection.{Electric, Flying}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class DamageCalculatorGen1Test extends AnyFlatSpec with should.Matchers:

  val move: Move = Move(
    name = "Electric",
    priority = 5,
    moveType = Electric,
    category = Physical,
    accuracy = Of(100.percent),
    pp = 10,
    success = SimpleSingleHitBehavior(10)
  )
  val electricMove: BattleMove = move.battling

  val alice: Trainer =
    Trainer("Alice", List(simplePokemon1), Some(simplePokemon1))
  val bob: Trainer =
    Trainer("Alice", List(simplePokemon2), Some(simplePokemon2))

  val trainers: List[Trainer] = alice :: bob :: Nil
  val battleState: BattleState = BattleState(
    trainers,
    field(trainers) { b =>
      /** Electricfied:
        *   - Electric = 1.5
        */
      b.setTerrain(Electrified(2))

      /** Sunny:
        *   - WaterModifier = 1.5
        *   - FireModifier: = 1.5
        */
      b.setWeather(Sunny(3))

      /** No type modifiers
        */
      b.setRoom(TrickRoom(1))
    }
  )

  /** Calculate damage to an Electric Pokémon that attack with Electric move a
    * Grass-Poison Pokémon with Electrified Sunny and TrickRoom field effects
    */
  "Damage Calculator" should "calculate damage (1)" in:
    val movePower = 10
    val damage =
      calculate(
        electricMove,
        alice.inField.get,
        bob.inField.get,
        movePower,
        battleState
      )
    val baseDamage = movePower * (alice.inField.get.base.stats.base(
      Attack
    ) / bob.inField.get.base.stats.base(Defense)) / 50 + 2
    val multiplier = 1.5 * 0.5 * 1.5
    damage shouldEqual (baseDamage * multiplier).toInt

  /** Calculate damage with same conditions of test above with no field effects.
    */
  it should "calculate damage (2)" in:
    val battleState: BattleState = BattleState(trainers, field(trainers)())
    val movePower = 5
    val damage =
      calculate(
        electricMove,
        alice.inField.get,
        bob.inField.get,
        5,
        battleState
      )
    val baseDamage = movePower * (alice.inField.get.base.stats.base(
      Attack
    ) / bob.inField.get.base.stats.base(Defense)) / 50 + 2
    val multiplier = 1.5 * 0.5 * 1.0
    damage shouldEqual (baseDamage * multiplier).toInt

  /** Calculate damage to Grassy-Poison Pokémon that attack with Flying Special
    * attack without field effects
    */
  it should "calculate damage (3)" in:
    val move: Move = Move(
      name = "flyingAttack",
      priority = 5,
      moveType = Flying,
      category = Special,
      pp = 10,
      accuracy = Of(100.percent),
      success = SimpleSingleHitBehavior(10)
    )
    val specialFlyingMove: BattleMove = move.battling
    
    val alice: Trainer =
      Trainer("Alice", List(simplePokemon2), Some(simplePokemon2))
    val bob: Trainer =
      Trainer("Alice", List(simplePokemon1), Some(simplePokemon1))
    val trainers: List[Trainer] = alice :: bob :: Nil
    val battleState: BattleState = BattleState(trainers, field(trainers)())
    val movePower = 5
    val damage = calculate(
      specialFlyingMove,
      alice.inField.get,
      bob.inField.get,
      movePower,
      battleState
    )
    val baseDamage = movePower * (alice.inField.get.base.stats.base(
      SpecialAttack
    ) / bob.inField.get.base.stats.base(SpecialDefense)) / 50 + 2
    val multiplier = 1.0 * 0.5 * 1.0
    damage shouldEqual (baseDamage * multiplier).toInt
