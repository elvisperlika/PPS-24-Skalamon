package it.unibo.skalamon.model.field

/** Status that mutate the [[Field]] for a defined number of turns.
  */
trait Expirable(bornTurn: Int, elapsedTurn: Int):

  /** Check if status is yet on course.
    * @param currentTurn
    *   Battle's current turn.
    * @return
    *   True if status ends his period of execution.
    */
  def isExpired(currentTurn: Int): Boolean = currentTurn >= bornTurn + elapsedTurn
