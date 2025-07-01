package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior}
import it.unibo.skalamon.model.data.{Percentage, percent}
import it.unibo.skalamon.model.move.Move
import it.unibo.skalamon.model.move.MoveModel.{Accuracy, Category}
import it.unibo.skalamon.model.types.Type

/** A builder for creating Move instances using a DSL-like syntax.
  *
  * @param name
  *   The name of the move being built.
  * @param moveType
  *   The type of the move (e.g., Fire, Water, etc.).
  * @param category
  *   The category of the move (e.g., Physical, Special, Status).
  */
class MoveBuilder(
    private val name: String,
    private val moveType: Type,
    private val category: Category
) extends DslBuilder[Move]:
  private var priority = 0
  private var accuracy: Accuracy = Accuracy.Of(100.percent)
  private var success: Behavior = EmptyBehavior
  private var fail: Behavior = EmptyBehavior

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

  def accuracy(accuracy: Accuracy): MoveBuilder =
    this.accuracy = accuracy
    this

  def accuracy(accuracy: Percentage): MoveBuilder =
    this.accuracy(Accuracy.Of(accuracy))

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
  override def build: Move =
    Move(name, priority, moveType, category, accuracy, success, fail)

/** DSL function to create a Move instance.
  *
  * @param name
  *   The name of the move.
  * @param moveType
  *   The type of the move (e.g., Fire, Water, etc.).
  * @param category
  *   The category of the move (e.g., Physical, Special, Status).
  * @param build
  *   A function that takes a `MoveBuilder` and returns a modified builder.
  * @return
  *   The constructed Move instance.
  */
def move(
    name: String,
    moveType: Type,
    category: Category
)(build: MoveBuilder => MoveBuilder): Move =
  build(MoveBuilder(name, moveType, category)).build
