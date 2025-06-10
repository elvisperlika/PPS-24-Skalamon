package it.unibo.skalamon.controller.battle.turn

import it.unibo.skalamon.controller.battle.{Action, Trainer}

trait TurnControllerProxy:
  /** Actions map.
    */
  private var _actions: Map[Trainer, Action] = Map()

  def actions: Map[Trainer, Action] = _actions

  def actions_=(newActions: Map[Trainer, Action]): Unit = _actions = newActions

  /** Add new Action in 'actions' map.
    * @param t
    *   Is the Trainer
    * @param a
    *   Is the Action
    */
  def addAction(t: Trainer, a: Action): Unit

object TurnControllerProxy:

  def apply(nTrainers: Int): TurnControllerProxy = 
    TurnControllerProxyImpl(nTrainers)

  private class TurnControllerProxyImpl(nTrainers: Int)
      extends TurnControllerProxy:

    override def addAction(t: Trainer, a: Action): Unit = actions =
      require(actions.size < nTrainers)
      actions + (t -> a)
