package it.unibo.skalamon.view

import it.unibo.skalamon.view.screen.*

import javax.swing.{JFrame, WindowConstants}
import java.awt.event.{KeyEvent, KeyListener}
import asciiPanel.AsciiPanel

class MainView extends View, KeyListener:
  private var terminal: Option[AsciiPanel] = None
  private var screen: Option[Screen] = None

  def createView(): Unit =
    terminal = Option(AsciiPanel())
    terminal.get.write("rl tutorial", 1, 1)
    add(terminal.get)
    pack()

    screen = Option(MainMenuScreen())
    addKeyListener(this)
    repaint()
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    this.setVisible(true)

  override def repaint(): Unit =
    terminal.get.clear
    screen.get.displayOutput(terminal.get)
    super.repaint()

  def keyPressed(e: KeyEvent): Unit =
    screen = Option(screen.get.respondToUserInput(e))
    repaint
  def keyReleased(e: KeyEvent): Unit = {}
  def keyTyped(e: KeyEvent): Unit = {}
