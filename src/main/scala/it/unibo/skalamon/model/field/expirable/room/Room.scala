package it.unibo.skalamon.model.field.expirable.room

import it.unibo.skalamon.model.field.expirable.weather.Weather

/** Like [[Weather]], a [[Room]] effect lasts 5 turns and there can be only one
  * at a time.
  */
case class Room(name: String)
