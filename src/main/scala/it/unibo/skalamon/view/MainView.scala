package it.unibo.skalamon.view

import javax.swing.{JFrame, WindowConstants}
import asciiPanel.AsciiPanel
import it.unibo.skalamon.view.battle.{
  BattleInput,
  BattleScreen,
  BattleKeyBindings
}

import java.awt.event.{KeyAdapter, KeyEvent}

/** Represents the main view of the game, which contains the battle screen. It
  * sets up the terminal and handles key events for player inputs.
  */
class MainView() extends JFrame:
  private val terminalWidth: Int = 88
  private val terminalHeight: Int = 37

  /** The terminal used for displaying the battle screen. */
  val terminal: AsciiPanel = AsciiPanel(terminalWidth, terminalHeight)

  private var onKeyPressedCallback: Option[BattleInput => Unit] = None

  addKeyListener(new KeyAdapter:
    override def keyPressed(e: KeyEvent): Unit =
      BattleKeyBindings.keyEventToBattleInput(e).foreach { battleInput =>
        onKeyPressedCallback.foreach(_(battleInput))
      })

  /** Sets up the main view of the game, including the terminal and key event
    * handling.
    */
  def setupView(): Unit =
    setResizable(false)
    add(terminal)
    pack()

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    this.setVisible(true)

  /** Returns the battle screen for the game.
    * @return
    *   The BattleScreen instance that represents the play screen.
    */
  def getPlayScreen: BattleScreen =
    BattleScreen(terminal)

  override def repaint(): Unit =
    terminal.clear()
    super.repaint()

    /** Sets the key pressed handler to process BattleInput events.
      * @param handler
      *   A function that takes a BattleInput and performs an action.
      */
  def setKeyPressedHandler(handler: BattleInput => Unit): Unit =
    onKeyPressedCallback = Some(handler)
