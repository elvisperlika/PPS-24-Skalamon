package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.pokemon.MutablePokemon

/**
 *
 */
case class MoveContext(
                        move: MutableMove,
                        target: MutablePokemon,
                        source: MutablePokemon,
                        var hits: List[MoveContext.Hit] = List.empty,
                      )

object MoveContext {
  case class Hit(power: Int)
}