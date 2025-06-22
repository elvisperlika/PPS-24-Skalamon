package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.behavior.modifier.BehaviorModifiers
import it.unibo.skalamon.model.behavior.visitor.BehaviorVisitor

/** A behavior describes a single-responsibility trait or strategy that can be
  * applied to a move or ability in a battle.
  */
trait Behavior:
  /** A list of behaviors that this behavior delegates to.
    *
    * This allows for composing behaviors in a flexible way, where one behavior
    * can include others as part of its execution.
    */
  protected def delegates: List[Behavior] = List(this)

  /** Applies the behavior to the given move context.
    *
    * @param container
    *   The context to which the behavior is applied.
    * @param modifiers
    *   The global behavior modifiers that can influence the behavior
    * @tparam T
    *   The type of the container that holds the behaviors.
    * @return
    *   A new container of the same type, ideally a copy, with the behavior
    *   applied.
    */
  def apply[T <: WithBehaviors](container: T)(using
      modifiers: BehaviorModifiers = BehaviorModifiers()
  ): T =
    container.append(delegates.map((_, modifiers)))

  /** Accepts a visitor to process this behavior.
    *
    * @param visitor
    *   The visitor that will process this behavior.
    * @tparam T
    *   The type of the result returned by the visitor.
    * @return
    *   The result of the visitor's processing.
    * @throws UnsupportedOperationException
    *   If the behavior does not support visitors. Generally, behaviors that
    *   delegate their logic to other behaviors via [[delegates]] do not support
    *   visitors, as they do not appear in a [[WithBehaviors]] container.
    */
  def accept[T](visitor: BehaviorVisitor[T]): T =
    throw UnsupportedOperationException(
      "This behavior does not support visitors"
    )

/** An empty behavior that does nothing.
  */
object EmptyBehavior extends Behavior:
  override def apply[T <: WithBehaviors](container: T)(using
      modifiers: BehaviorModifiers
  ): T = container
