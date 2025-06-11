package it.unibo.skalamon.controller.battle.turn

import it.unibo.skalamon.controller.battle.{Action, Trainer}

import scala.concurrent.{ExecutionContext, Future, Promise}

trait TurnControllerProxy:
  private var _actions: Map[Trainer, Action] = Map()

  /** Actions map getter.
    * @return
    *   Map: Trainer -> Action
    */
  def actions: Map[Trainer, Action] = _actions

  /** Actions map setter.
    * @param newActions
    *   Replace actions map
    */
  def actions_=(newActions: Map[Trainer, Action]): Unit = _actions = newActions

  /** Add new Action in 'actions' map.
    * @param t
    *   Trainer
    * @param a
    *   Action
    */
  def addAction(t: Trainer, a: Action): Unit

  /** Actions map getter when map is ready.
    * @return
    *   Map: Trainer -> Action
    */
  def getChosenActions: Future[Map[Trainer, Action]]

object TurnControllerProxy:

  def apply(nTrainers: Int): TurnControllerProxy =
    TurnControllerProxyImpl(nTrainers)

  private class TurnControllerProxyImpl(nTrainers: Int)
      extends TurnControllerProxy:

    private val promise: Promise[Map[Trainer, Action]] = Promise()

    override def addAction(t: Trainer, a: Action): Unit =
      require(actions.size < nTrainers)
      actions = actions + (t -> a)
      if (actions.size == nTrainers && !promise.isCompleted)
        promise.success(actions)

    given ExecutionContext = ExecutionContext.global
    override def getChosenActions: Future[Map[Trainer, Action]] = promise.future
