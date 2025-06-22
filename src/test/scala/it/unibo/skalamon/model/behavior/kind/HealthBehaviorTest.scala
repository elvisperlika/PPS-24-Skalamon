package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.*
import it.unibo.skalamon.model.behavior.BehaviorTestUtils.*
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.move.MoveContext
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[HealthBehavior]] and its implementations.
  */
class HealthBehaviorTest extends AnyFlatSpec with should.Matchers {
  private val DELTA = 30
  private val HP = 100

  private def assertHealthEquals(
      context: MoveContext,
      expectedHealth: Int
  ) =
    getPlainBehaviors(context) match {
      case List(b: HealthBehavior) => b.newHealth(HP) shouldEqual expectedHealth
      case _                       => fail()
    }

  "HealBehavior" should "heal" in:
    val behavior = HealBehavior(DELTA)
    val result = behavior(context)
    assertHealthEquals(result, HP + DELTA)

  "DamageBehavior" should "damage" in:
    val behavior = DamageBehavior(DELTA)
    val result = behavior(context)
    assertHealthEquals(result, HP - DELTA)
}
