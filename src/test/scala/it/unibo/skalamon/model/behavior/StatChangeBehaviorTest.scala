package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.pokemon.*
import it.unibo.skalamon.model.behavior.BehaviorTestUtils.*
import it.unibo.skalamon.model.behavior.kind.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[StatChangeBehavior]].
  */
class StatChangeBehaviorTest extends AnyFlatSpec with should.Matchers:
  private val STAGE = 2

  "StatChangeBehavior" should "bring stat change" in:
    val behavior = StatChangeBehavior(Stat.Attack + STAGE)
    val result = behavior.apply(context)
    getPlainBehaviors(result) shouldEqual List(
      StatChangeBehavior(StatChange(Stat.Attack, STAGE))
    )
