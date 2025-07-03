package it.unibo.skalamon.view.battle

import it.unibo.skalamon.model.battle.{
  BattleState,
  Trainer
}
import it.unibo.skalamon.model.pokemon.BattlePokemon

trait BattleView:

  /** Update the whole battle view.
    * @param battleState
    *   The current state of the battle.
    */
  def update(battleState: BattleState): Unit

/** Provides a factory method to create a new BattleView instance.
  */
object BattleView:
  def apply(screen: BattleScreen): BattleView =
    new BattleViewImpl(screen)

  /** Create a new BattleView.
    * @param screen
    *   The screen to be used for the battle view.
    * @return
    *   A new instance of BattleView.
    */
  private class BattleViewImpl(screen: BattleScreen) extends BattleView:

    override def update(battleState: BattleState): Unit =
      val trainers = battleState.trainers
      require(
        trainers.size == BattleScreen.playerNumber,
        s"Expected ${BattleScreen.playerNumber} trainers, but got ${trainers.size}."
      )

      val Seq(player, opponent) = trainers

      screen.setPlayersName(player.name, opponent.name)
      screen.setBattlePokemon(player.inField, opponent.inField)
      screen.setPokemonTeam(
        teamWithKeys(player, "player"),
        teamWithKeys(opponent, "opponent")
      )
      screen.setMoves(
        movesWithKeys(player, "player"),
        movesWithKeys(opponent, "opponent")
      )

    /** Returns the team of a trainer without the Pokémon currently in the
      * field.
      * @param trainer
      *   The trainer whose team is to be returned.
      * @return
      *   A list of BattlePokemon excluding the one currently in the field.
      */
    private def teamWithoutInField(trainer: Trainer): List[BattlePokemon] =
      trainer.inField.map(p => trainer.team.filterNot(_.id == p.id))
        .getOrElse(trainer.team)

    /** Returns the team of a trainer with each Pokémon paired with a key
      * binding.
      * @param trainer
      *   The trainer whose team is to be returned.
      * @param side
      *   The side of the trainer, either "player" or "opponent".
      * @return
      */
    private def teamWithKeys(
        trainer: Trainer,
        side: String
    ): List[BattlePokemonWithKey] =
      teamWithoutInField(trainer).zipWithIndex.flatMap { case (poke, i) =>
        BattleKeyBindings.getPokemonKeyChar(side, i).map(key =>
          BattlePokemonWithKey(poke, key)
        )
      }

    /** Returns the moves of a trainer with each move paired with a key binding.
      * @param trainer
      *   The trainer whose moves are to be returned.
      * @param side
      *   The side of the trainer, either "player" or "opponent".
      * @return
      */
    private def movesWithKeys(
        trainer: Trainer,
        side: String
    ): List[BattleMoveWithKey] =
      trainer.inField.map(_.moves).getOrElse(List.empty)
        .zipWithIndex.flatMap { case (move, i) =>
          BattleKeyBindings.getKeyChar(side, i).map(char =>
            BattleMoveWithKey(move, char)
          )
        }
