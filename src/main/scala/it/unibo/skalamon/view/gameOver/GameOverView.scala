package it.unibo.skalamon.view.gameOver

import it.unibo.skalamon.model.battle.Trainer

/** Represents a view for the game-over screen in the ScalaMon game.
  *
  * This trait defines the interface for updating the game-over view with the
  * winner's name after a battle concludes.
  */
trait GameOverView:

  /** Update the game-over view with the winner's name.
    * @param winner
    *   The trainer who won the battle.
    */
  def update(winner: Option[Trainer]): Unit

/** Provides a factory method to create a new GameOverView instance.
  */
object GameOverView:
  def apply(screen: GameOverScreen): GameOverView =
    new GameOverViewImpl(screen)

  /** Create a new GameOverView.
    * @param screen
    *   The screen to be used for the game-over view.
    * @return
    *   A new instance of GameOverView.
    */
  private class GameOverViewImpl(screen: GameOverScreen) extends GameOverView:

    override def update(winner: Option[Trainer]): Unit =
      screen.setWinner(winner)
      screen.repaint()
