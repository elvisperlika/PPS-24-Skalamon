package it.unibo.skalamon.view

import javax.swing.JFrame

/** */
trait View extends JFrame:
  def createView(): Unit
