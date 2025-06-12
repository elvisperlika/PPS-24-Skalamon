package it.unibo.skalamon.view.screen

import it.unibo.skalamon.view.Container.*

import java.awt.Color
import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

class PlayScreen(
    private val playerName: String,
    private val opponentName: String
) extends Screen:

  private def setPlayersName(
      terminal: AsciiPanel,
      player: String,
      opponent: String
  ): Unit =
    terminal.writeCenter(opponent, 1)
    terminal.writeCenter(player, 22)
    HorizontalContainer(terminal, 3, 4, 12, 5)

  override def displayOutput(terminal: AsciiPanel): Unit =
    setPlayersName(terminal, playerName, opponentName)

  override def respondToUserInput(key: KeyEvent): Screen =
    this
