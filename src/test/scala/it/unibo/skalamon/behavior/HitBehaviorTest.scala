package it.unibo.skalamon.behavior

import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.pokemon.MutablePokemon
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/**
 * Tests for [[HitBehavior]] and its implementations.
 */
class HitBehaviorTest extends AnyFlatSpec with should.Matchers {
  private val context = MoveContext(
    move = MutableMove(
      Move("TestMove"),
      pp = 10,
    ),
    target = MutablePokemon(hp = 100),
    source = MutablePokemon(hp = 100),
  )

  "SingleHitBehavior" should "apply a single hit" in {
    val behavior = SingleHitBehavior(30)
    val result = behavior.apply(context)
    result.hits shouldEqual List(MoveContext.Hit(30))
  }

  "MultiHitBehavior" should "apply multiple hits with the same power" in {
    val behavior = MultiHitBehavior(hits = 3, power = 20)
    val result = behavior.apply(context)
    result.hits shouldEqual List(MoveContext.Hit(20), MoveContext.Hit(20), MoveContext.Hit(20))
  }

  "MultiHitBehavior" should "apply multiple hits with the different power" in {
    val behavior = MultiHitBehavior(hits = 3, power = hitIndex => (hitIndex + 1) * 10)
    val result = behavior.apply(context)
    result.hits shouldEqual List(MoveContext.Hit(10), MoveContext.Hit(20), MoveContext.Hit(30))
  }
}
