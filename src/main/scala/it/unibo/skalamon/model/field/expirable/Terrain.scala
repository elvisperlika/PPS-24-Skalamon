package it.unibo.skalamon.model.field.expirable

/** Like [[Weather]], a [[Terrain]] lasts 5 turns and there can be only one at a time.
  * [[Terrain]] effects only apply to grounded Pokémon (non-flying type and without
  * the Levitate ability).
  */
case class Terrain(name: String)
