package it.unibo.skalamon.model.behavior.visitor

import it.unibo.skalamon.model.behavior.kind.*

/** A visitor for different types of behaviors.
  * @tparam T
  *   The return type of the visitor methods.
  */
trait BehaviorVisitor[T]:
  def visit(behavior: SingleHitBehavior): T
  def visit(behavior: HealthBehavior): T
  def visit(behavior: StatChangeBehavior): T
  def visit(behavior: StatusBehavior): T
  def visit(behavior: WeatherBehavior): T
