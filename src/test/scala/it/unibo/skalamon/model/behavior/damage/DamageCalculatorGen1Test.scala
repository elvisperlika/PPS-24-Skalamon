package it.unibo.skalamon.model.behavior.damage

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import it.unibo.skalamon.model.behavior.damage.DamageCalculatorGen1
import it.unibo.skalamon.model.behavior.damage.DamageCalculatorGen1.calculate
import it.unibo.skalamon.model.behavior.kind.{
  SimpleSingleHitBehavior,
  SingleHitBehavior
}
import it.unibo.skalamon.model.move.MoveModel.Accuracy.Of
import it.unibo.skalamon.model.move.MoveModel.Category.Physical
import it.unibo.skalamon.model.move.{BattleMove, Move}
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.*
import it.unibo.skalamon.model.types.TypesCollection.Electric
import it.unibo.skalamon.model.data.percent

class DamageCalculatorGen1Test extends AnyFlatSpec with should.Matchers:

  val move = Move(
    name = "Electric",
    priority = 5,
    moveType = Electric,
    category = Physical,
    accuracy = Of(100.percent),
    success = SimpleSingleHitBehavior(10)
  )

  val battleMoveElectric = BattleMove(
    move = move,
    pp = 10
  )
  "Damage Calculator" should "test 1" in:
    val damage =
      calculate(battleMoveElectric, simplePokemon1, simplePokemon2, 10)
    damage shouldEqual 8
