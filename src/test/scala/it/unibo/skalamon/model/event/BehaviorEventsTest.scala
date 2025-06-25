package it.unibo.skalamon.model.event

import it.unibo.skalamon.model.behavior.kind.{SingleHitBehavior, StatusBehavior}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for triggering and handling behavior events in the event manager.
  */
class BehaviorEventsTest extends AnyFlatSpec with should.Matchers:
  private val hitBehavior = SingleHitBehavior(10)

  "Behaviors" should "trigger their own event" in:
    val eventManager = EventManager()
    var notified = false
    eventManager.watch(BehaviorEvent[SingleHitBehavior]) { behavior =>
      notified = true
      behavior.power shouldBe hitBehavior.power
    }
    eventManager.notify(hitBehavior.event)

  "EventManager" should "not notify for different behavior types" in:
    val eventManager = EventManager()
    var notified = false
    eventManager.watch(BehaviorEvent[StatusBehavior]) { _ =>
      notified = true
    }
    eventManager.notify(hitBehavior.event)
    notified shouldBe false
