package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.expirable.{Room, Terrain, Weather}
import it.unibo.skalamon.model.field.fieldside.FieldSide

/* temporary classes start */
case class Trainer(name: String)
/* temporary classes end */

case class Field(
    sides: Map[Trainer, FieldSide],
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

  def build(sides: Map[Trainer, FieldSide]): Field = Field(sides, t, r, w)

def field(trainers: List[Trainer])(init: FieldBuilder => Unit): Field =
  val builder = new FieldBuilder
  init(builder)

  def createSides(trainers: List[Trainer]): Map[Trainer, FieldSide] =
    trainers.foldLeft(Map[Trainer, FieldSide]())((map, t) =>
      map + (t -> FieldSide())
    )
  builder.build(createSides(trainers))
