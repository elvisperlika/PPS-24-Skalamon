package it.unibo.skalamon.view.screen

import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

trait Screen:
  def displayOutput(terminal: AsciiPanel): Unit
  def respondToUserInput(key: KeyEvent): Screen
