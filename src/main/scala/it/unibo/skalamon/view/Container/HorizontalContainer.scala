package it.unibo.skalamon.view.Container

import asciiPanel.AsciiPanel

/** Represents a horizontal container that holds multiple boxes in a row.
  * @param terminal
  *   The AsciiPanel that holds all the text.
  * @param boxesData
  *   A sequence of sequences of BoxContainerData.
  * @param startY
  *   The 'y' starting coordinate for the first box.
  * @param boxWidth
  *   The width of each box.
  * @param boxHeight
  *   The height of each box.
  * @param spacing
  *   The spacing between each box (default is 2).
  */
case class HorizontalContainer(
    terminal: AsciiPanel,
    boxesData: Seq[BoxContainerData],
    startY: Int,
    boxWidth: Int,
    boxHeight: Int,
    spacing: Int = 2
):
  private val numBoxes = boxesData.length
  private val totalWidth = numBoxes * boxWidth + spacing * (numBoxes - 1)
  private val startX = (terminal.getWidthInCharacters - totalWidth) / 2

  val boxes: Seq[BoxContainer] =
    boxesData.zipWithIndex.map { case (boxData, i) =>
      val x = startX + i * (boxWidth + spacing)
      new BoxContainer(
        terminal,
        boxData,
        x,
        startY,
        boxWidth,
        boxHeight
      )
    }
