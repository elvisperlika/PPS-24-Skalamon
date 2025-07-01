package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.move.{BattleMove, Move}
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Gender, Pokemon}

extension (pokemon: Pokemon)
  /** Converts a [[Pokemon]] to a [[BattlePokemon]]. */
  def battling(gender: Gender): BattlePokemon =
    BattlePokemon(
      base = pokemon,
      gender = gender,
      currentHP = pokemon.hp,
      moves = pokemon.moves.map(move => BattleMove(move, pp = move.pp)),
      nonVolatileStatus = None,
      volatileStatus = Set.empty
    )

extension (move: Move)
  /** Converts a [[Move]] to a [[BattleMove]]. */
  def battling: BattleMove =
    BattleMove(move, pp = move.pp)
