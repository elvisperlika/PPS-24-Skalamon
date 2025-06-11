package it.unibo.skalamon.view.screen

import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

class PlayScreen extends Screen:
  def displayOutput(terminal: AsciiPanel): Unit =
    terminal.write("You are having fun.", 1, 1)
    terminal.writeCenter("-- press [escape] to lose or [enter] to win --", 22)

  def respondToUserInput(key: KeyEvent): Screen =
    this
