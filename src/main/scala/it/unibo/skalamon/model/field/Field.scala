package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.expirable.{Terrain, Room, Weather}

case class Field(
    terrain: Option[Terrain],
    room: Option[Room],
    weather: Option[Weather]
)

class FieldBuilder:
  private val t: Option[Terrain] = None
  private val r: Option[Room] = None
  private val w: Option[Weather] = None

  def build(): Field = Field(t, r, w)

def field(init: FieldBuilder => Unit): Field =
  val builder = new FieldBuilder
  init(builder)
  builder.build()
