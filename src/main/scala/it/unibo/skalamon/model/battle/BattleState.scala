package it.unibo.skalamon.model.battle

import it.unibo.skalamon.model.field.Field
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.pokemon.BattlePokemon

/** The immutable state of a battle, containing all the trainers involved, their
  * Pokémon, battle rules and other relevant battle conditions.
  */
case class BattleState(
    trainers: List[Trainer],
    field: Field,
    rules: BattleRule
):
  /** Returns a new battle state with [[target]] updated according to [[map]].
    */
  def updatePokemon(
      target: BattlePokemon,
      map: BattlePokemon => BattlePokemon
  ): BattleState =
    copy(
      trainers = trainers.map { trainer =>
        trainer.copy(
          team = trainer.team.map { pokemon =>
            if (pokemon is target) map(pokemon) else pokemon
          }
        )
      }
    )

  /** Returns a new battle state with [[target]]'s moves updated according to [[map]].
    */
  def updateMove(
      target: BattlePokemon,
      move: BattleMove,
      map: BattleMove => BattleMove
  ): BattleState =
    updatePokemon(target, pokemon =>
      pokemon.copy(
        moves = pokemon.moves.map { m =>
          if (m eq move) map(m) else m
        }
      )
    )
