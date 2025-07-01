package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.behavior.kind.SingleHitBehavior
import it.unibo.skalamon.model.event.TurnStageEvents
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for building abilities via DSL. */
class AbilityDslTest extends AnyFlatSpec with should.Matchers:

  "Ability DSL" should "create empty abilities" in :
    val static = ability("Static")(identity)

    static.name shouldBe "Static"
    static.hooks shouldBe empty

  it should "allow setting hooks" in :
    val static = ability("Static"):
      _.on(TurnStageEvents.Started)(SingleHitBehavior(10))

    static.name shouldBe "Static"
    static.hooks shouldBe Map(
      TurnStageEvents.Started -> SingleHitBehavior(10)
    )