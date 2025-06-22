package it.unibo.skalamon.model.battle

/** The immutable state of a battle, containing all the trainers involved, their
  * Pokémon, and other relevant battle conditions.
  */
case class BattleState(
    trainers: List[Trainer]
    // TODO field, weather, terrain, room, side conditions, etc.
)
