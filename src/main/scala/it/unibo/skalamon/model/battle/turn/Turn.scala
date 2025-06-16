package it.unibo.skalamon.model.battle.turn

import it.unibo.skalamon.model.battle.turn.BattleEvents.*
import it.unibo.skalamon.model.event.{Event, EventManager}

/** [[Turn]]
  */
case class Turn():
  
  /** @param e
    *   Event
    */
  def trigger(e: Event[_]): Unit = e.eventType match
    case TurnStart => ???
