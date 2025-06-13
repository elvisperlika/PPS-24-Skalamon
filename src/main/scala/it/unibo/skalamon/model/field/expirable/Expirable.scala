package it.unibo.skalamon.model.field.expirable

/** Status that mutate the [[Field]] for a defined number of turns.
  */
trait Expirable(turn: Int, elapsedTurn: Int):

  /** Check if status is yet on course.
    * @param currentTurn
    *   Battle's current turn.
    * @return
    *   True if status ends his period of execution.
    */
  def isExpired(currentTurn: Int): Boolean = currentTurn > turn + elapsedTurn
