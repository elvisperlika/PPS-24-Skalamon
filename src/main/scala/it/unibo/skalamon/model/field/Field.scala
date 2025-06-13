package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.expirable.{Terrain, Room, Weather}

case class Field(
    terrain: Option[Terrain],
    room: Option[Room],
    weather: Option[Weather]
)

class FieldBuilder:
  private var t: Option[Terrain] = None
  private var r: Option[Room] = None
  private var w: Option[Weather] = None

  def terrain(name: String): Unit = t = Some(Terrain(name))
  def room(name: String): Unit = r = Some(Room(name))
  def weather(description: String): Unit = w = Some(Weather(description))

  def build(): Field = Field(t, r, w)

def field(init: FieldBuilder => Unit): Field =
  val builder = new FieldBuilder
  init(builder)
  builder.build()
