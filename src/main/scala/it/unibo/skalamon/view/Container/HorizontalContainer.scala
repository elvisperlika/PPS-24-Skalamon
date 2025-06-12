package it.unibo.skalamon.view.Container

import asciiPanel.AsciiPanel

case class HorizontalContainer(
    terminal: AsciiPanel,
    startY: Int,
    numBoxes: Int,
    boxWidth: Int,
    boxHeight: Int,
    spacing: Int = 2
):
  private val totalWidth = numBoxes * boxWidth + spacing * (numBoxes - 1)
  private val startX = (terminal.getWidthInCharacters - totalWidth) / 2

  val boxes: Seq[BoxContainer] = (0 until numBoxes).map { i =>
    val x = startX + i * (boxWidth + spacing)
    BoxContainer(terminal, x, startY, boxWidth, boxHeight)
  }
