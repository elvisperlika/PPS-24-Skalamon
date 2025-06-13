package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.expirable.room.Room
import it.unibo.skalamon.model.field.expirable.Terrain
import it.unibo.skalamon.model.field.expirable.weather.Weather
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

extension (oldField: Field)
  def changeSide(t: Trainer, s: FieldSide): Field =
    def updateSide(trainer: Trainer, side: FieldSide): Map[Trainer, FieldSide] =
      oldField.sides.map((t1, s1) => if t1 == t then (t1, s) else (t1, s1))
    Field(updateSide(t, s), oldField.terrain, oldField.room, oldField.weather)

class FieldBuilder:
  private var terrain: Option[Terrain] = None
  private var room: Option[Room] = None
  private var weather: Option[Weather] = None

  def setTerrain(name: String): Unit = terrain = Some(Terrain(name))
  def setRoom(name: String): Unit = room = Some(Room(name))
  def setWeather(description: String): Unit =
    weather = Some(Weather())

  def build(sides: Map[Trainer, FieldSide]): Field =
    Field(sides, terrain, room, weather)

def field(trainers: List[Trainer])(init: FieldBuilder => Unit): Field =
  val builder = new FieldBuilder
  init(builder)

  def createSides(trainers: List[Trainer]): Map[Trainer, FieldSide] =
    trainers.foldLeft(Map[Trainer, FieldSide]())((map, t) =>
      map + (t -> FieldSide())
    )
  builder.build(createSides(trainers))
