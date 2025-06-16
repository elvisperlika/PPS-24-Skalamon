package it.unibo.skalamon.view.Container

import asciiPanel.AsciiPanel

/** Creates a box in the terminal that can contain text.
  *
  * @param terminal
  *   The AsciiPanel that holds all the text.
  * @param textList
  *   List containing row by row all the text contained in the box.
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
    private val textList: Seq[String],
    x: Int,
    y: Int,
    width: Int,
    height: Int
):
  private val HorizontalPadding = 2
  private val VerticalPadding = 1
  private val MinBoxBorder = 1

  // Validate text
  require(
    textList.forall(_.length <= width - (HorizontalPadding * 2)),
    s"Each text line must be at most ${width - (HorizontalPadding * 2)} characters long."
  )
  require(
    textList.length <= height - (VerticalPadding * 2),
    s"Text list must have fewer than ${height - (VerticalPadding * 2)} lines."
  )

  drawEmptyBox(x, y, width, height)
  drawText(textList, x + HorizontalPadding, y + VerticalPadding)

  /** Draws the box boundaries
    *
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
    val maxWidth = width - MinBoxBorder
    val maxHeight = height - MinBoxBorder

    for (i <- MinBoxBorder until maxWidth) {
      terminal.write('-', x + i, y)
      terminal.write('-', x + i, y + maxHeight)
    }

    for (i <- MinBoxBorder until maxHeight) {
      terminal.write('|', x, y + i)
      terminal.write('|', x + maxWidth, y + i)
    }

    terminal.write('+', x, y)
    terminal.write('+', x + maxWidth, y)
    terminal.write('+', x, y + maxHeight)
    terminal.write('+', x + maxWidth, y + maxHeight)

  /** Writes the text content inside the box.
    *
    * @param textList
    *   The lines of text.
    * @param x
    *   X coordinate for text start.
    * @param y
    *   Y coordinate for first line of text.
    */
  private def drawText(textList: Seq[String], x: Int, y: Int): Unit =
    textList match
      case h +: t =>
        terminal.write(h, x, y)
        drawText(t, x, y + 1)
      case _ => ()
