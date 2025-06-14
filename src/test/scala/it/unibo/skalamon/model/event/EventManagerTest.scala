package it.unibo.skalamon.model.event

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/**
 * Tests for [[EventManager]] watching and triggering.
 */
class EventManagerTest extends AnyFlatSpec with should.Matchers:
  "EventManager" should "be able to register and trigger events" in:
    val manager = EventManager()
    var watched = 0
    val value1 = 42
    val value2 = 8

    object TestEventType extends EventType[Int]

    manager.watch(TestEventType) { watched += _ }

    manager.notify(TestEventType of value1)
    manager.notify(TestEventType of value2)

    watched shouldBe (value1 + value2)

