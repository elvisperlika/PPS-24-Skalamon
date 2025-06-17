package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.field.PokemonRule
import it.unibo.skalamon.model.types.Type

enum WhenAffect:
  case OnCreation
  case EveryTurn
  
/** [[Weather]] changes last 5 turns and can be triggered by moves or abilities.
  * Only one [[Weather]] can be active at a time. If one is active, it is
  * overridden.
  */
trait Weather(name: String, whenAffect: WhenAffect):

  /** Type affected by the weather that increase or decrease the move's power by
    * a multiplier.
    */
  val typesMultiplierMap: Map[Type, Double]

  /** List of actions to perform on Pokémon in the field.
    */
  val fieldEffects: List[PokemonRule]
