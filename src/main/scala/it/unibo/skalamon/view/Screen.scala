package it.unibo.skalamon.view

import asciiPanel.AsciiPanel

/** Represents a screen in the game that can be repainted.
  *
  * @param terminal
  *   The terminal used for rendering the screen.
  */
trait Screen(private val terminal: AsciiPanel):
  /** Repaints the screen.
    */
  def repaint(): Unit =
    terminal.repaint()
