package it.unibo.skalamon.model.event

import it.unibo.skalamon.model.pokemon.BattlePokemon

/** An event that can be triggered and watched by an [[EventManager]].
  * @tparam T
  *   The type of data carried by the event.
  * @param eventType
  *   The type of the event.
  * @param data
  *   The data carried by the event.
  */
case class Event[T](
    eventType: EventType[T],
    data: T
)

/** A type of [[Event]] that can be watched by an [[EventManager]].
  * @tparam T
  *   The type of data carried by the event.
  */
trait EventType[T]:
  /** Creates a concrete event of this type with the given data.
    * @param data
    *   The data to be carried by the event.
    * @return
    *   An [[Event]] of this type containing the data.
    */
  def of(data: T): Event[T] = Event(this, data)
