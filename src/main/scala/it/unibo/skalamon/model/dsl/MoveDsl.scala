package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior}
import it.unibo.skalamon.model.move.Move
import it.unibo.skalamon.model.types.Type

/** A builder for creating Move instances using a DSL-like syntax.
  *
  * @param name
  *   The name of the move being built.
  */
class MoveBuilder(private val name: String) extends DslBuilder[Move]:
  private var priority = 0
  private var moveType: Option[Type] = None
  private var success: Behavior = EmptyBehavior
  private var fail: Behavior = EmptyBehavior

  /** Sets the type of the move.
    *
    * @param moveType
    *   The type to assign to the move.
    * @return
    *   This for chaining.
    */
  def typed(moveType: Type): MoveBuilder =
    this.moveType = Some(moveType)
    this

  /** Sets the priority of the move.
    *
    * @param priority
    *   The priority value.
    * @return
    *   This for chaining.
    */
  def priority(priority: Int): MoveBuilder =
    this.priority = priority
    this

  /** Sets the behavior of the move in case of success.
    *
    * @param success
    *   The behavior to execute on success.
    * @return
    *   This for chaining.
    */
  def onSuccess(success: Behavior): MoveBuilder =
    this.success = success
    this

  /** Sets the behavior of the move in case of failure.
    *
    * @param fail
    *   The behavior to execute on failure.
    * @return
    *   This for chaining.
    */
  def onFail(fail: Behavior): MoveBuilder =
    this.fail = fail
    this

  /** Builds the Move instance with the provided attributes.
    *
    * @return
    *   The constructed Move instance.
    */
  override def build: Move = // TODO assign move type once it's implemented
    Move(name, priority, success, fail)

/** DSL function to create a Move instance.
  *
  * @param name
  *   The name of the move.
  * @param build
  *   A function that takes a `MoveBuilder` and returns a modified builder.
  * @return
  *   The constructed Move instance.
  */
def move(name: String)(build: MoveBuilder => MoveBuilder): Move =
  build(MoveBuilder(name)).build
