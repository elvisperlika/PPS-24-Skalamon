package it.unibo.skalamon.model.event.config

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.controller.battle.action.Action
import it.unibo.skalamon.model.pokemon.Stat.Speed

object OrderingUtils:

  given Ordering[Action] with
    def compare(a1: Action, a2: Action): Int =
      def priorityOf(a: Action): Int = a match
        case SwitchAction() => 6
        case MoveAction(m)  => m.origin.move.priority

      def speedOf(a: Action): Int = a match
        case MoveAction(m) => m.source.base.baseStats.base(Speed)
        case _             => 0

      val p1 = priorityOf(a1)
      val p2 = priorityOf(a2)
      if p1 != p2 then p2.compareTo(p1)
      else
        (a1, a2) match
          case (m1: MoveAction, m2: MoveAction) =>
            speedOf(m2).compareTo(speedOf(m1))
          case _ => 0
