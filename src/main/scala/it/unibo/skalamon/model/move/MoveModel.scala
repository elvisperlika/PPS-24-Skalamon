package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.data.Percentage

object MoveModel:

  /** Represents the [[Move]]'s accuracy.
    */
  enum Accuracy:
    /** The move never fails.
      */
    case NeverFail

    /** Chance for the move to hit, expressed as a percentage.
      *
      * @param value
      *   hit chance percentage (0-100)
      */
    case Of(value: Percentage)

  extension (a: Accuracy)
    /** Converts the accuracy to a display string.
      * @return
      *   A string representation of the accuracy.
      */
    def toDisplayString: String = a match
      case Accuracy.NeverFail => "Never misses"
      case Accuracy.Of(p)     => p.toString

  /** Represents the category of a [[Move]]. Determines how damage is calculated
    * or if the move affects status.
    */
  enum Category:
    /** Physical move: deals damage based on physical attack and defense.
      */
    case Physical

    /** Special move: deals damage based on special attack and special defense.
      */
    case Special

    /** Status move: does not deal damage, but applies effects or modifies
      * stats.
      */
    case Status
