package it.unibo.skalamon.model.event

import it.unibo.skalamon.model.behavior.Behavior

import scala.reflect.{ClassTag, classTag}

/** An [[EventType]] that represents the application of a specific behavior onto
  * a [[it.unibo.skalamon.model.behavior.BehaviorsContext]].
  */
class BehaviorEvent[B <: Behavior: ClassTag] extends EventType[B]:
  /** The type of the event. */
  val tag: ClassTag[B] = classTag[B]

extension [T <: Behavior: ClassTag](behavior: T)

  /** Creates an event of this behavior type.
    *
    * @return
    *   An event of the behavior type.
    */
  def event: Event[T] = BehaviorEvent[T] of behavior
