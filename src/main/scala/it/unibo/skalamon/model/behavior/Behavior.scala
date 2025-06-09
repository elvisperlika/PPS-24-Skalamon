package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.behavior.modifier.BehaviorModifiers

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
    *   A new container of the same type, ideally a copy, with the behavior applied.
    */
  def apply[T <: WithBehaviors](container: T)(using
      modifiers: BehaviorModifiers = BehaviorModifiers()
  ): T =
    container.append(container.behaviors ++ delegates.map((_, modifiers)))

/** An empty behavior that does nothing.
  */
object EmptyBehavior extends Behavior:
  override def apply[T <: WithBehaviors](container: T)(using
      modifiers: BehaviorModifiers
  ): T = container
