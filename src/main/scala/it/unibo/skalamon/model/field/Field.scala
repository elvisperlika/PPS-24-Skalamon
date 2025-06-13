package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.expirable.{Terrain, Room, Weather}

case class Field(
    terrain: Option[Terrain],
    room: Option[Room],
    weather: Option[Weather]
)

class FieldBuilder:
  private var t: Option[Terrain] = None
  
  def terrain(name: String): Unit = t = Some(Terrain(name))

  def build(): Field = Field(t, None, None)

def field(init: FieldBuilder => Unit): Field =
  val builder = new FieldBuilder
  init(builder)
  builder.build()
