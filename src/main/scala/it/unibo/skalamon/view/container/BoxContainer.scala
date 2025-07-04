package it.unibo.skalamon.view.container

import asciiPanel.AsciiPanel

import java.awt.Color
import scala.annotation.tailrec

/** Creates a box in the terminal that can contain text.
  *
  * @param terminal
  *   The AsciiPanel that holds all the text.
  * @param containerData
  *   The data to be displayed in the box, consisting of text and color.
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
    private val containerData: BoxContainerData,
    x: Int,
    y: Int,
    width: Int,
    height: Int
):
  private val HorizontalPadding = 2
  private val VerticalPadding = 1
  private val MinBoxBorder = 1

  private val text = BoxContainerData.text(containerData)
  private val color = BoxContainerData.color(containerData)
  // Validate text
  require(
    text.forall(_.length <= width - (HorizontalPadding * 2)),
    s"Each text line must be at most ${width - (HorizontalPadding * 2)} characters long."
  )
  require(
    text.length <= height - (VerticalPadding * 2),
    s"Text list must have fewer than ${height - (VerticalPadding * 2)} lines."
  )

  drawEmptyBox(color, x, y, width, height)
  drawText(text, color, x + HorizontalPadding, y + VerticalPadding)

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
  private def drawEmptyBox(
      color: Color,
      x: Int,
      y: Int,
      width: Int,
      height: Int
  ): Unit =
    val maxWidth = width - MinBoxBorder
    val maxHeight = height - MinBoxBorder

    for (i <- MinBoxBorder until maxWidth) {
      terminal.write('-', x + i, y, color)
      terminal.write('-', x + i, y + maxHeight, color)
    }

    for (i <- MinBoxBorder until maxHeight) {
      terminal.write('|', x, y + i, color)
      terminal.write('|', x + maxWidth, y + i, color)
    }

    terminal.write('+', x, y, color)
    terminal.write('+', x + maxWidth, y, color)
    terminal.write('+', x, y + maxHeight, color)
    terminal.write('+', x + maxWidth, y + maxHeight, color)

  /** Writes the text content inside the box.
    *
    * @param textList
    *   The lines of text.
    * @param x
    *   X coordinate for text start.
    * @param y
    *   Y coordinate for first line of text.
    */
  @tailrec
  private def drawText(
      textList: Seq[String],
      color: Color,
      x: Int,
      y: Int
  ): Unit =
    textList match
      case h +: t =>
        terminal.write(h, x, y, color)
        drawText(t, color, x, y + 1)
      case _ => ()

/** Represents the data contained in a BoxContainer.
  * @param text
  *   The text to be displayed in the box.
  * @param color
  *   The color of the box.
  */
opaque type BoxContainerData = (Seq[String], Color)

/** Companion object for BoxContainerData. Provides an apply method to create a
  * BoxContainerData instance and methods to access its text and color.
  */
object BoxContainerData:
  def apply(text: Seq[String], color: Color): BoxContainerData = (text, color)
  def text(data: BoxContainerData): Seq[String] = data._1
  def color(data: BoxContainerData): Color = data._2
