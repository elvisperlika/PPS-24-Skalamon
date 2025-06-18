package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.FieldEffectMixin.{Room, Terrain, Weather}
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

  def setTerrain(t: Terrain): Unit = terrain = Some(t)
  def setRoom(r: Room): Unit = room = Some(r)
  def setWeather(w: Weather): Unit = weather = Some(w)

  def build(sides: Map[Trainer, FieldSide]): Field =
    Field(sides, terrain, room, weather)

def field(trainers: List[Trainer])(init: FieldBuilder => Unit): Field =
  val builder = new FieldBuilder
  init(builder)
  builder.build(trainers.map(t => t -> FieldSide()).toMap)
