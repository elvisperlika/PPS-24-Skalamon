package it.unibo.skalamon.controller.battle.turn

import it.unibo.skalamon.controller.battle.{Action, Trainer}

trait TurnControllerProxy:
  /** Actions map.
    */
  var actions: Map[Trainer, Action] = Map()

  /** Add new Action in 'actions' map.
    * @param t
    *   is the Trainer
    * @param a
    *   is the Action
    */
  def addAction(t: Trainer, a: Action): Unit

object TurnControllerProxy:

  def apply(): TurnControllerProxy = TurnControllerProxyImpl()

  private class TurnControllerProxyImpl extends TurnControllerProxy:
    import TurnPhase.*
    var turnPhase: TurnPhase = PokemonGetInField

    override def addAction(t: Trainer, a: Action): Unit = actions =
      actions + (t -> a)
