package it.unibo.skalamon.view.Container

import asciiPanel.AsciiPanel

class BoxContainer(
    private val terminal: AsciiPanel,
    x: Int,
    y: Int,
    width: Int,
    height: Int
):
  drawEmptyBox(x, y, width, height)

  private def drawEmptyBox(x: Int, y: Int, width: Int, height: Int): Unit =
    for (i <- 1 until width - 1)
      terminal.write('-', x + i, y)
      terminal.write('-', x + i, y + height - 1)

    for (i <- 1 until height - 1)
      terminal.write('|', x, y + i)
      terminal.write('|', x + width - 1, y + i)

    terminal.write('+', x, y)
    terminal.write('+', x + width - 1, y)
    terminal.write('+', x, y + height - 1)
    terminal.write('+', x + width - 1, y + height - 1)
