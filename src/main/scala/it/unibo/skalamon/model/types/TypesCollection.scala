package it.unibo.skalamon.model.types

case object Normal
    extends Type(
      superEffectiveAgainst = Nil,
      resistedBy = List(Rock, Steel),
      ineffectiveAgainst = List(Ghost)
    )

case object Fire
    extends Type(
      superEffectiveAgainst = List(Grass, Ice, Bug, Steel),
      resistedBy = List(Fire, Water, Rock, Dragon),
      ineffectiveAgainst = Nil
    )

case object Water
    extends Type(
      superEffectiveAgainst = List(Fire, Ground, Rock),
      resistedBy = List(Water, Grass, Dragon),
      ineffectiveAgainst = Nil
    )

case object Grass
    extends Type(
      superEffectiveAgainst = List(Water, Ground, Rock),
      resistedBy = List(Fire, Grass, Poison, Flying, Bug, Dragon, Steel),
      ineffectiveAgainst = Nil
    )

case object Electric
    extends Type(
      superEffectiveAgainst = List(Water, Flying),
      resistedBy = List(Electric, Grass, Dragon),
      ineffectiveAgainst = List(Ground)
    )

case object Ice
    extends Type(
      superEffectiveAgainst = List(Grass, Ground, Flying, Dragon),
      resistedBy = List(Fire, Water, Ice, Steel),
      ineffectiveAgainst = Nil
    )

case object Fighting
    extends Type(
      superEffectiveAgainst = List(Normal, Ice, Rock, Dark, Steel),
      resistedBy = List(Poison, Flying, Psychic, Bug, Fairy),
      ineffectiveAgainst = List(Ghost)
    )

case object Poison
    extends Type(
      superEffectiveAgainst = List(Grass, Fairy),
      resistedBy = List(Poison, Ground, Rock, Ghost),
      ineffectiveAgainst = List(Steel)
    )

case object Ground
    extends Type(
      superEffectiveAgainst = List(Fire, Electric, Poison, Rock, Steel),
      resistedBy = List(Grass, Bug),
      ineffectiveAgainst = List(Flying)
    )

case object Flying
    extends Type(
      superEffectiveAgainst = List(Grass, Fighting, Bug),
      resistedBy = List(Electric, Rock, Steel),
      ineffectiveAgainst = Nil
    )

case object Psychic
    extends Type(
      superEffectiveAgainst = List(Fighting, Poison),
      resistedBy = List(Psychic, Steel),
      ineffectiveAgainst = List(Dark)
    )

case object Bug
    extends Type(
      superEffectiveAgainst = List(Grass, Psychic, Dark),
      resistedBy = List(Fire, Fighting, Poison, Flying, Ghost, Steel, Fairy),
      ineffectiveAgainst = Nil
    )

case object Rock
    extends Type(
      superEffectiveAgainst = List(Fire, Ice, Flying, Bug),
      resistedBy = List(Fighting, Ground, Steel),
      ineffectiveAgainst = Nil
    )

case object Ghost
    extends Type(
      superEffectiveAgainst = List(Ghost, Psychic),
      resistedBy = List(Dark),
      ineffectiveAgainst = List(Normal)
    )

case object Dragon
    extends Type(
      superEffectiveAgainst = List(Dragon),
      resistedBy = List(Steel),
      ineffectiveAgainst = List(Fairy)
    )

case object Dark
    extends Type(
      superEffectiveAgainst = List(Ghost, Psychic),
      resistedBy = List(Fighting, Dark, Fairy),
      ineffectiveAgainst = Nil
    )

case object Steel
    extends Type(
      superEffectiveAgainst = List(Ice, Rock, Fairy),
      resistedBy = List(Fire, Water, Electric, Steel),
      ineffectiveAgainst = Nil
    )

case object Fairy
    extends Type(
      superEffectiveAgainst = List(Fighting, Dragon, Dark),
      resistedBy = List(Fire, Poison, Steel),
      ineffectiveAgainst = Nil
    )
