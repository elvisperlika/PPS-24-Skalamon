package it.unibo.skalamon.model.event

import it.unibo.skalamon.model.behavior.Behavior

import scala.reflect.{ClassTag, classTag}

/** An [[EventType]] that represents the application of a specific behavior onto
  * a [[it.unibo.skalamon.model.behavior.BehaviorsContext]].
  * @param runtimeClass
  *   Optional runtime class of the behavior type to override the
  *   statically-defined type [[B]].
  * @tparam B
  *   The type of the behavior that this event represents.
  */
class BehaviorEvent[B <: Behavior: ClassTag](
    runtimeClass: Option[Class[? <: B]] = None
) extends EventType[B]:
  /** The type of the event. */
  val tag: ClassTag[B] = runtimeClass.map(ClassTag(_)).getOrElse(classTag[B])

  override def equals(obj: Any): Boolean =
    obj match
      case that: BehaviorEvent[_] => that.tag.runtimeClass.isAssignableFrom(this.tag.runtimeClass)
      case _                      => false

  override def hashCode(): Int = tag.hashCode()

  override def toString: String =
    s"BehaviorEvent(${tag.runtimeClass.getSimpleName})"

extension [T <: Behavior: ClassTag](behavior: T)

  /** Creates an event of this behavior type.
    *
    * @return
    *   An event of the behavior type.
    */
  def event: Event[T] = BehaviorEvent[T](Some(behavior.getClass)) of behavior
