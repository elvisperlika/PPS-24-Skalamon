package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.types.Type

trait FieldEffect:

  /** Type affected by the weather that increase or decrease the move's power by
    * a multiplier.
    */
  val typesMultiplierMap: Map[Type, Double]

  /** List of actions to perform on Pokémon in the field.
    */
  val fieldEffects: List[PokemonRule]
