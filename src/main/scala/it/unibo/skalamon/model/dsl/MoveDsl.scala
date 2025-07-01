package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.move.Move
import it.unibo.skalamon.model.types.Type

/** A builder for creating Move instances using a DSL-like syntax.
  *
  * @param name
  *   The name of the move being built.
  */
class MoveBuilder(private val name: String):
  private var priority = 0
  private var moveType: Option[Type] = None

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

  /** Builds the Move instance with the provided attributes.
    *
    * @return
    *   The constructed Move instance.
    */
  def build: Move = // TODO assign move type once it's implemented
    Move(name, priority)

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