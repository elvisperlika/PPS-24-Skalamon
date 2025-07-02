package it.unibo.skalamon.view

import javax.swing.{JFrame, WindowConstants}
import asciiPanel.AsciiPanel
import it.unibo.skalamon.view.battle.{
  BattleInput,
  BattleScreen,
  keyEventToBattleInput
}

import java.awt.event.{KeyAdapter, KeyEvent}

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

  def getPlayScreen: BattleScreen =
    BattleScreen(terminal)

  override def repaint(): Unit =
    super.repaint()

  private var onKeyPressedCallback: Option[BattleInput => Unit] = None

  def setKeyPressedHandler(handler: BattleInput => Unit): Unit =
    onKeyPressedCallback = Some(handler)

  addKeyListener(new KeyAdapter:
    override def keyPressed(e: KeyEvent): Unit =
      keyEventToBattleInput(e).foreach { battleInput =>
        onKeyPressedCallback.foreach(_(battleInput))
      })
