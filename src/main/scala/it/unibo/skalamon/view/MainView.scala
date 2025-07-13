package it.unibo.skalamon.view

import asciiPanel.AsciiPanel
import it.unibo.skalamon.view.battle.{
  BattleKeyBindings,
  BattleScreen
}
import it.unibo.skalamon.view.gameOver.GameOverScreen

import java.awt.event.{KeyAdapter, KeyEvent}
import javax.swing.{JFrame, WindowConstants}

/** Represents the main view of the game, which contains the battle screen. It
  * sets up the terminal and handles key events for player inputs.
  */
class MainView extends JFrame:
  private val terminalWidth: Int = 100
  private val terminalHeight: Int = 50

  private val screenTitle: String = "Scalamon"

  /** The terminal used for displaying the battle screen. */
  val terminal: AsciiPanel = AsciiPanel(terminalWidth, terminalHeight)

  private var onKeyPressedCallback: Option[InputKeyWords => Unit] = None

  /** Sets up the main view of the game, including the terminal and key event
    * handling.
    */
  def setupView(): Unit =
    setResizable(false)
    add(terminal)
    pack()

    this.setTitle(screenTitle)
    this.setFocusable(true)

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    this.setVisible(true)

  /** Returns the battle screen for the game.
    * @return
    *   The BattleScreen instance that represents the play screen.
    */
  def getPlayScreen: BattleScreen =
    BattleScreen(terminal)

  /** Returns the game-over screen for the game.
    * @return
    *   The GameOverScreen instance that represents the game-over screen.
    */
  def getGameOverScreen: GameOverScreen =
    GameOverScreen(terminal)

  override def repaint(): Unit =
    terminal.clear()
    super.repaint()

  /** Sets the input handler for the main view. This method listens for key
    * events and converts them into BattleInput
    * @param handler
    *   An instance of Inputs that defines how key events are mapped to
    */
  def setInputHandler(handler: Inputs): Unit =
    addKeyListener(new KeyAdapter:
      override def keyPressed(e: KeyEvent): Unit =
        handler.keyEventToKeyWords(e).foreach { battleInput =>
          onKeyPressedCallback.foreach(_(battleInput))
        })

    /** Sets the key pressed handler to process BattleInput events.
      * @param handler
      *   A function that takes a BattleInput and performs an action.
      */
  def setKeyPressedHandler(handler: InputKeyWords => Unit): Unit =
    onKeyPressedCallback = Some(handler)
