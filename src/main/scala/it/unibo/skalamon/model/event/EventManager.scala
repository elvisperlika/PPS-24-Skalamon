package it.unibo.skalamon.model.event

/** An event manager that allows watching and triggering [[Event]]s.
  */
class EventManager:
  private var watchers: Map[EventType[_], List[Any => Unit]] = Map.empty

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

/** A provider of a constant [[EventManager]] instance. */
trait EventManagerProvider:
  /** The event manager instance to use for event handling. */
  val eventManager: EventManager
