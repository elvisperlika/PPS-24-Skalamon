package it.unibo.skalamon.model.types

case object Normal extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] = Nil
  override val resistedBy: List[MoveType] = List(Rock, Steel)
  override val ineffectiveAgainst: List[MoveType] = List(Ghost)
}

case object Fire extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] =
    List(Grass, Ice, Bug, Steel)
  override val resistedBy: List[MoveType] = List(Fire, Water, Rock, Dragon)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Water extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] = List(Fire, Ground, Rock)
  override val resistedBy: List[MoveType] = List(Water, Grass, Dragon)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Grass extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] = List(Water, Ground, Rock)
  override val resistedBy: List[MoveType] =
    List(Fire, Grass, Poison, Flying, Bug, Dragon, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Electric extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] = List(Water, Flying)
  override val resistedBy: List[MoveType] = List(Electric, Grass, Dragon)
  override val ineffectiveAgainst: List[MoveType] = List(Ground)
}

case object Ice extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] =
    List(Grass, Ground, Flying, Dragon)
  override val resistedBy: List[MoveType] = List(Fire, Water, Ice, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Fighting extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] =
    List(Normal, Ice, Rock, Dark, Steel)
  override val resistedBy: List[MoveType] =
    List(Poison, Flying, Psychic, Bug, Fairy)
  override val ineffectiveAgainst: List[MoveType] = List(Ghost)
}

case object Poison extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] = List(Grass, Fairy)
  override val resistedBy: List[MoveType] = List(Poison, Ground, Rock, Ghost)
  override val ineffectiveAgainst: List[MoveType] = List(Steel)
}

case object Ground extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] =
    List(Fire, Electric, Poison, Rock, Steel)
  override val resistedBy: List[MoveType] = List(Grass, Bug)
  override val ineffectiveAgainst: List[MoveType] = List(Flying)
}

case object Flying extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] =
    List(Grass, Fighting, Bug)
  override val resistedBy: List[MoveType] = List(Electric, Rock, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Psychic extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] = List(Fighting, Poison)
  override val resistedBy: List[MoveType] = List(Psychic, Steel)
  override val ineffectiveAgainst: List[MoveType] = List(Dark)
}

case object Bug extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] =
    List(Grass, Psychic, Dark)
  override val resistedBy: List[MoveType] =
    List(Fire, Fighting, Poison, Flying, Ghost, Steel, Fairy)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Rock extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] =
    List(Fire, Ice, Flying, Bug)
  override val resistedBy: List[MoveType] = List(Fighting, Ground, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Ghost extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] = List(Ghost, Psychic)
  override val resistedBy: List[MoveType] = List(Dark)
  override val ineffectiveAgainst: List[MoveType] = List(Normal)
}

case object Dragon extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] = List(Dragon)
  override val resistedBy: List[MoveType] = List(Steel)
  override val ineffectiveAgainst: List[MoveType] = List(Fairy)
}

case object Dark extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] = List(Ghost, Psychic)
  override val resistedBy: List[MoveType] = List(Fighting, Dark, Fairy)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Steel extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] = List(Ice, Rock, Fairy)
  override val resistedBy: List[MoveType] = List(Fire, Water, Electric, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Fairy extends TypeImpl {
  override val superEffectiveAgainst: List[MoveType] =
    List(Fighting, Dragon, Dark)
  override val resistedBy: List[MoveType] = List(Fire, Poison, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}
