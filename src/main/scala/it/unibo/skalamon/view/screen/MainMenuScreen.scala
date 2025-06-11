package it.unibo.skalamon.view.screen

import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

class MainMenuScreen extends Screen:
  def displayOutput(terminal: AsciiPanel): Unit =
    terminal.write("rl tutorial", 1, 1)
    terminal.writeCenter("-- press [enter] to start --", 22)

  def respondToUserInput(key: KeyEvent): Screen =
    if (key.getKeyCode eq KeyEvent.VK_ENTER) new PlayScreen
    else this
