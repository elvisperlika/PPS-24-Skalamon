package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.behavior.EmptyBehavior
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for building moves via DSL. */
class MoveDslTest extends AnyFlatSpec with should.Matchers:

  "Move DSL" should "create empty moves" in:
    val tackle = move("Tackle")(identity)

    tackle.name shouldBe "Tackle"
    tackle.priority shouldBe 0
    tackle.success shouldBe EmptyBehavior
    tackle.fail shouldBe EmptyBehavior

  it should "allow setting priority" in:
    val quickAttack = move("Quick Attack"):
      _ priority 1

    quickAttack.priority shouldBe 1