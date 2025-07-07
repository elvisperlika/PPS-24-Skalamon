package it.unibo.skalamon.view.gameOver

import asciiPanel.AsciiPanel
import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.view.Screen

import java.awt.Color

class GameOverScreen(
    private val terminal: AsciiPanel
) extends Screen:
  import GameOverScreen.*

  def setWinner(winner: Option[Trainer]): Unit =
    val gameOverText = winner match
      case Some(w) => s"Game Over! ${w.name}. wins!"
      case None    => "No one"

    terminal.writeCenter(
      gameOverText,
      (terminal.getWidthInCharacters - gameOverText.length) / 2,
      nameColor
    )
    terminal.repaint()

object GameOverScreen:
  private val nameColor = Color.WHITE
