package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.weather.kind.Sunny
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.scalatest.prop.TableDrivenPropertyChecks.forAll
import org.scalatest.prop.Tables.Table

class ExpirableTest extends AnyFlatSpec with should.Matchers:

  "Expirable weather" should "return true if more than 5 turns passed" in:
    val sunny: Sunny = Sunny(1)
    val testCases = Table(
      ("turn", "expected"),
      (2, false),
      (3, false),
      (4, false),
      (5, false),
      (6, true)
    )
    forAll(testCases) { (turn, expected) =>
      sunny.isExpired(turn) shouldBe expected
    }
