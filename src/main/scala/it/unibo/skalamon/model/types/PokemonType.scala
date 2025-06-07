package it.unibo.skalamon.model.types

/** Type
  *
  * A move’s type can be neutral (1x), resisted (0.5x), super effective (2x or
  * 4x) or immune (0x) to the target’s types. Effectiveness is summable, Fire is
  * effective against Grass and Bug type, if a Pokémon is Grass and Bug type the
  * Fire attack will be super effective.
  */
trait Type:

  /** 2x effectiveness
    * @return
    */
  def effectiveAgainst: List[Type]

  /** 1/2x effectiveness
    * @return
    */
  def resistedBy: List[Type]

  /** 0x effectiveness
    * @return
    */
  def ineffectiveAgainst: List[Type] // per “immune”

case object Electric extends Type {
  override def effectiveAgainst: List[Type] = List(Water, Flying)
  override def resistedBy: List[Type] = List(Grass, Electric, Dragon)
  override def ineffectiveAgainst: List[Type] = List(Ground)
}

case object Water extends Type {
  override def effectiveAgainst: List[Type] = ???
  override def resistedBy: List[Type] = ???
  override def ineffectiveAgainst: List[Type] = ???
}

case object Flying extends Type {
  override def effectiveAgainst: List[Type] = ???
  override def resistedBy: List[Type] = ???
  override def ineffectiveAgainst: List[Type] = ???
}

case object Ground extends Type {
  override def effectiveAgainst: List[Type] = ???
  override def resistedBy: List[Type] = ???
  override def ineffectiveAgainst: List[Type] = ???
}

case object Grass extends Type {
  override def effectiveAgainst: List[Type] = ???
  override def resistedBy: List[Type] = ???
  override def ineffectiveAgainst: List[Type] = ???
}

case object Dragon extends Type {
  override def effectiveAgainst: List[Type] = ???
  override def resistedBy: List[Type] = ???
  override def ineffectiveAgainst: List[Type] = ???
}
