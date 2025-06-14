package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.types.Type

/** [[Weather]] changes last 5 turns and can be triggered by moves or abilities.
  * Only one [[Weather]] can be active at a time. If one is active, it is
  * overridden.
  */
trait Weather(name: String):

  /** Type affected by the weather that change the Type multiplier.
    */
  val typesMultiplierMap: Map[Type, Double]
