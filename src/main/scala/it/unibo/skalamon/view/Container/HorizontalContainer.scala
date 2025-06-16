package it.unibo.skalamon.view.Container

import asciiPanel.AsciiPanel

/** A row container where boxes are centered.
  * @param terminal
  *   The AsciiPanel that holds all the text.
  * @param startY
  *   The vertical position of the container.
  * @param numBoxes
  *   The number of boxes.
  * @param boxWidth
  *   The width of the boxes.
  * @param boxHeight
  *   The height of the boxes.
  * @param spacing
  *   The spacing between each box.
  */
case class HorizontalContainer(
    terminal: AsciiPanel,
    textList: Seq[String] = Seq(), // TODO: documentare
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
    BoxContainer(terminal, textList, x, startY, boxWidth, boxHeight)
  }
