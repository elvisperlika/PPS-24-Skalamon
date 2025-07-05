package it.unibo.skalamon.model.event.config

import it.unibo.skalamon.controller.battle.action.{Action, MoveAction}
import it.unibo.skalamon.model.pokemon.Stat.Speed

object OrderingUtils:
  
  private def speedOf(a: Action): Int = a match
    case MoveAction(_, source, _) => source.inField.get.base.stats.base(Speed)
    case _                        => 0

  def actionOrdering(speedCompare: (Int, Int) => Int): Ordering[Action] =
    (a1: Action, a2: Action) =>
      val p1 = a1.priority
      val p2 = a2.priority
      if p1 != p2 then p2.compareTo(p1) // Priorità discendente
      else
        (a1, a2) match
          case (m1: MoveAction, m2: MoveAction) =>
            speedCompare(speedOf(m1), speedOf(m2))
          case _ => 0
