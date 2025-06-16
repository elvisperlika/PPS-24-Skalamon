package it.unibo.skalamon.view

import it.unibo.skalamon.view.screen.*

import javax.swing.{JFrame, WindowConstants}
import java.awt.event.{KeyEvent, KeyListener}
import asciiPanel.AsciiPanel

class MainView extends View, KeyListener:
  private var terminal: Option[AsciiPanel] = None
  private var screen: Option[Screen] = None

  private val terminalWidth: Int = 80
  private val terminalHeight: Int = 35

  def createView(): Unit =
    terminal = Option(AsciiPanel(terminalWidth, terminalHeight))
    setResizable(false)
    add(terminal.get)
    pack()

    // screen = Option(MainMenuScreen())
    screen = Option(PlayScreen("Fabrizio", "Edgar"))
    addKeyListener(this)
    repaint()
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    this.setVisible(true)

  override def repaint(): Unit =
    terminal.get.clear
    screen.get.displayOutput(terminal.get)
    super.repaint()

  override def keyPressed(e: KeyEvent): Unit =
    screen = Option(screen.get.respondToUserInput(e))
    repaint
  override def keyReleased(e: KeyEvent): Unit = {}
  override def keyTyped(e: KeyEvent): Unit = {}
