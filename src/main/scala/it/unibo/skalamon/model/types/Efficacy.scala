package it.unibo.skalamon.model.types

/** Efficacy encapsulate multipliers.
  */
enum Efficacy:
  case SuperEffective
  case Effective
  case Resisted
  case Ineffective

  /** Get multiplier value.
    * @return
    *   Multiplier
    */
  def value: Double = this match
    case SuperEffective => 2.0
    case Effective      => 1.0
    case Resisted       => 0.5
    case Ineffective    => 0.0

  /** Get the Efficacy usable string.
    * @return
    *   name as String
    */
  def str: String = this match
    case SuperEffective => "Super Effective"
    case _              => toString
