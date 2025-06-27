package it.unibo.skalamon.model.battle

import it.unibo.skalamon.model.event.{Event, EventManager}
import it.unibo.skalamon.model.field.Field

import scala.collection.immutable.Queue

/** The immutable state of a battle, containing all the trainers involved, their
  * Pokémon, and other relevant battle conditions.
  * @param eventQueue
  *   The queue of events that have been generated during the state update, and
  *   will need to be notified and processed before the next state update.
  */
case class BattleState(
    trainers: List[Trainer],
    field: Field,
    eventQueue: Queue[Event[_]] = Queue.empty
):
  /** Notifies the event queue to the given event manager, processing each event
    * in the queue and removing it from the queue after notification.
    *
    * At the end of this operation, the event queue will be empty.
    * @param eventManager
    *   The event manager to notify about the events in the queue.
    * @return
    *   A copy of this [[BattleState]] with the event queue emptied.
    */
  def notifyEventQueue(eventManager: EventManager): BattleState =
    eventQueue.foldLeft(this) { (state, event) =>
      eventManager.notify(event)
      state.copy(eventQueue = state.eventQueue.dequeue._2)
    }
