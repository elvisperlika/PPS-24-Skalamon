package it.unibo.skalamon.model.status

/** Represents the status that has been assigned to a Pokémon.
  * @param Status
  *   The status that has been assigned to a Pokémon.
  * @param TurnAssigned
  *   The turn on which the status was assigned.
  */
case class AssignedStatus[T <: Status](status: T, turnAssigned: Int)
