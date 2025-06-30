package it.unibo.skalamon.model.event.config

import it.unibo.skalamon.controller.battle.action.{Action, MoveAction}
import it.unibo.skalamon.model.pokemon.Stat.Speed

object OrderingUtils:

  given Ordering[Action] with
    def compare(a1: Action, a2: Action): Int =
      def speedOf(a: Action): Int = a match
        case MoveAction(battleMove, source, target) =>
          source.base.stats.base(Speed)
        case _ => 0

      val p1 = a1.priority
      val p2 = a2.priority
      if p1 != p2 then p2.compareTo(p1)
      else
        (a1, a2) match
          case (m1: MoveAction, m2: MoveAction) =>
            speedOf(m2).compareTo(speedOf(m1))
          case _ => 0
