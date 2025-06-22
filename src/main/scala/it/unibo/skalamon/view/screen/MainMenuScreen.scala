package it.unibo.skalamon.view.screen

import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

class MainMenuScreen(terminal: AsciiPanel) extends Screen:
  terminal.write("rl tutorial", 1, 1)
  terminal.writeCenter("-- press [enter] to start --", 22)

  override def respondToUserInput(key: KeyEvent): Screen =
    if (key.getKeyCode eq KeyEvent.VK_ENTER) this // new PlayScreen()
    else this
