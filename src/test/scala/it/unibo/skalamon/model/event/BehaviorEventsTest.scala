package it.unibo.skalamon.model.event

import it.unibo.skalamon.model.behavior.kind.{SingleHitBehavior, StatusBehavior}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for triggering and handling behavior events in the event manager.
  */
class BehaviorEventsTest extends AnyFlatSpec with should.Matchers with BeforeAndAfterEach:
  private val hitBehavior = SingleHitBehavior(10)
  
  private var eventManager: EventManager = _
  private var notified: Boolean = _
  
  override def beforeEach(): Unit =
    eventManager = EventManager()
    notified = false

  "Behaviors" should "trigger their own event" in:
    eventManager.watch(BehaviorEvent[SingleHitBehavior]) { behavior =>
      notified = true
      behavior.power shouldBe hitBehavior.power
    }
    eventManager.notify(hitBehavior.event)
    notified shouldBe true

  "EventManager" should "not notify for different behavior types" in:
    eventManager.watch(BehaviorEvent[StatusBehavior]) { _ =>
      notified = true
    }
    eventManager.notify(hitBehavior.event)
    notified shouldBe false
