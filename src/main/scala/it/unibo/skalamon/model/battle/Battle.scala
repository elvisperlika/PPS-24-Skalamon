package it.unibo.skalamon.model.battle

import it.unibo.skalamon.model.battle.turn.Turn
import it.unibo.skalamon.model.event.EventManager

import scala.collection.mutable

/* start temporary */
trait Trainer(name: String)
/* end temporary */

/** Battle model.
  */
trait Battle

object Battle:
  def apply(trainers: List[Trainer]): Battle = new BattleImpl(trainers)

  private class BattleImpl(trainers: List[Trainer]) extends Battle:
    val eventManager: EventManager = EventManager()
    val turns: mutable.Stack[Turn] = mutable.Stack.empty
