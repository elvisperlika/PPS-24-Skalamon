package it.unibo.skalamon.view.Container

import asciiPanel.AsciiPanel

/** Creates a box in the terminal that can contain text.
  * @param terminal
  *   The AsciiPanel that holds all the text.
  * @param x
  *   The 'x' starting coordinate of the box.
  * @param y
  *   The 'y' starting coordinate of the box.
  * @param width
  *   The width of the box.
  * @param height
  *   The height of the box.
  */
class BoxContainer(
    private val terminal: AsciiPanel,
    x: Int,
    y: Int,
    width: Int,
    height: Int
):
  drawEmptyBox(x, y, width, height)

  /** Draws the box boundaries
    * @param x
    *   The 'x' starting coordinate of the box.
    * @param y
    *   The 'y' starting coordinate of the box.
    * @param width
    *   The width of the box.
    * @param height
    *   The height of the box.
    */
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
