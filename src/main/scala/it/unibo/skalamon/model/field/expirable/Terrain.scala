package it.unibo.skalamon.model.field.expirable

import it.unibo.skalamon.model.field.expirable.weather.Weather

/** Like [[Weather]], a [[Terrain]] lasts 5 turns and there can be only one at a time.
  * [[Terrain]] effects only apply to grounded Pokémon (non-flying type and without
  * the Levitate ability).
  */
case class Terrain(name: String)
