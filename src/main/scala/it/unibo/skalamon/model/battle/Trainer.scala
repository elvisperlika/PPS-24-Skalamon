package it.unibo.skalamon.model.battle

import it.unibo.skalamon.model.pokemon.BattlePokemon

/** */
case class Trainer(
    name: String,
    team: List[BattlePokemon],
    inField: Option[BattlePokemon] = None
)
