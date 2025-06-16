package it.unibo.skalamon.controller.battle

/* start Temporary classes */
case class MutablePokemon(name: String, isKO: Boolean = false)

case class Trainer(name: String, team: List[MutablePokemon])
/* end Temporary classes */

/** Battle controller manage battle status and end game logic.
  */
trait BattleController:

  /** Update view and model's data.
    */
  def update(): Unit

object BattleController:
  def apply(trainers: List[Trainer]) = ???