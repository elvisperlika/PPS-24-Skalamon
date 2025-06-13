package it.unibo.skalamon.model.field.expirable.weather

/** [[Weather]] changes last 5 turns and can be triggered by moves or abilities.
  * Only one [[Weather]] can be active at a time. If one is active, it is
  * overridden.
  */
case class Weather(name: String)
