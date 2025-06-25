package it.unibo.skalamon.model.battle

import it.unibo.skalamon.model.field.Field

/** The immutable state of a battle, containing all the trainers involved, their
  * Pokémon, and other relevant battle conditions.
  */
case class BattleState(
    trainers: List[Trainer],
    field: Field
)
