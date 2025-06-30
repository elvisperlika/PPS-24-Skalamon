package it.unibo.skalamon.model.behavior.damage

import it.unibo.skalamon.model.battle.{BattleState, Trainer}
import it.unibo.skalamon.model.behavior.damage.DamageCalculatorGen1.calculate
import it.unibo.skalamon.model.behavior.kind.SimpleSingleHitBehavior
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.field.field
import it.unibo.skalamon.model.field.room.TrickRoom
import it.unibo.skalamon.model.field.terrain.Electrified
import it.unibo.skalamon.model.field.weather.Sunny
import it.unibo.skalamon.model.move.MoveModel.Accuracy.Of
import it.unibo.skalamon.model.move.MoveModel.Category.Physical
import it.unibo.skalamon.model.move.{BattleMove, Move}
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.*
import it.unibo.skalamon.model.types.TypesCollection.Electric
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class DamageCalculatorGen1Test extends AnyFlatSpec with should.Matchers:

  val move: Move = Move(
    name = "Electric",
    priority = 5,
    moveType = Electric,
    category = Physical,
    accuracy = Of(100.percent),
    success = SimpleSingleHitBehavior(10)
  )
  val battleMoveElectric: BattleMove = BattleMove(
    move = move,
    pp = 10
  )

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

  "Damage Calculator" should "test 1" in:
    val damage =
      calculate(
        battleMoveElectric,
        alice.inField.get,
        bob.inField.get,
        10,
        battleState
      )
    println(damage)
    damage shouldEqual 12
