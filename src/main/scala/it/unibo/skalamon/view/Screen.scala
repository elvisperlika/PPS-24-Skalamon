package it.unibo.skalamon.view

import asciiPanel.AsciiPanel

trait Screen(private val terminal: AsciiPanel):
  def repaint(): Unit =
    terminal.repaint()
