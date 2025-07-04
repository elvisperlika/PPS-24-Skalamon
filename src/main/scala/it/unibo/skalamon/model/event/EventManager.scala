package it.unibo.skalamon.model.event

import scala.collection.mutable

/** An event manager that allows watching and triggering [[Event]]s.
  */
class EventManager:
  private var watchers: Map[EventType[_], List[Any => Unit]] = Map.empty

  /** A queue of events that have been triggered but not yet notified to
    * watchers. They can be notified later using [[notifyQueue()]].
    */
  val queue: mutable.Queue[Event[_]] = mutable.Queue.empty

  /** Registers a callback to be called when an event of the specified type is
    * triggered.
    *
    * @param eventType
    *   The type of event to watch.
    * @param callback
    *   The function to call when the event is triggered, receiving the data of
    *   the event.
    */
  def watch[T](eventType: EventType[T])(callback: T => Unit): Unit =
    watchers = watchers.updated(
      eventType,
      callback.asInstanceOf[Any => Unit] :: watchers.getOrElse(eventType, Nil)
    )

  /** Notifies all watchers of the specified event type with the given data.
    *
    * @param event
    *   The event to notify watchers about.
    */
  def notify[T](event: Event[T]): Unit =
    watchers.get(event.eventType) foreach:
      _.foreach(callback => callback(event.data))

  /** Empties the event queue and notifies all watchers of the events in the
    * queue.
    */
  def notifyQueue(): Unit =
    while queue.nonEmpty do
      notify(queue.dequeue())

/** A provider of a constant [[EventManager]] instance. */
trait EventManagerProvider:
  /** The event manager instance to use for event handling. */
  val eventManager: EventManager
