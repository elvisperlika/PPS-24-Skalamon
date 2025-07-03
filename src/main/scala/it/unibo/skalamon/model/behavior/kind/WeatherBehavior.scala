package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.*
import it.unibo.skalamon.model.behavior.visitor.BehaviorVisitor
import it.unibo.skalamon.model.field.FieldEffectMixin.Weather

/** Behavior that applies a weather to the field.
  *
  * @param weather
  *   The weather to be applied, with the current turn index as a parameter
  */
case class WeatherBehavior(weather: (turnIndex: Int) => Weather) extends Behavior:
  override def accept[T](visitor: BehaviorVisitor[T]): T = visitor.visit(this)
