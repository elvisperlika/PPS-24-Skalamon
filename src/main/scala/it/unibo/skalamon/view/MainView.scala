package it.unibo.skalamon.view

import it.unibo.skalamon.view.screen.*

import javax.swing.{JFrame, WindowConstants}
import java.awt.event.{KeyEvent, KeyListener}
import asciiPanel.AsciiPanel

class MainView() extends JFrame:
  private val terminalWidth: Int = 80
  private val terminalHeight: Int = 35

  val terminal: AsciiPanel = AsciiPanel(terminalWidth, terminalHeight)

  def setupView(): Unit =
    setResizable(false)
    add(terminal)
    pack()

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    this.setVisible(true)

  def getPlayScreen(): BattleScreen =
    BattleScreen(terminal)

  override def repaint(): Unit =
    terminal.clear
    super.repaint()
