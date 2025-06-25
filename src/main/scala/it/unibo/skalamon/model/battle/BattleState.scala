package it.unibo.skalamon.model.battle

import it.unibo.skalamon.model.event.Event
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
)
