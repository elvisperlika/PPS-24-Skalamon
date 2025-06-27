package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.field.FieldEffectMixin.{Room, Terrain, Weather}
import it.unibo.skalamon.model.field.fieldside.FieldSide

/** Immutable [[Field]] is the zone Pokémon battle. [[Field]] has as many
  * [[FieldSide]] as Pokémon in battle, and it's dynamic environment with
  * dynamic creation of [[Room]], [[Terrain]] or [[Weather]] caused by Pokémon
  * moves.
  *
  * @param sides
  *   Specific piece of the battlefield where Pokémon in battle is located
  * @param terrain
  *   The active [[Terrain]], if any
  * @param room
  *   The active [[Room]], if any
  * @param weather
  *   The active [[Weather]], if any
  */
case class Field(
    sides: Map[Trainer, FieldSide],
    terrain: Option[Terrain],
    room: Option[Room],
    weather: Option[Weather]
)

extension (oldField: Field)
  /** Update the field side of a [[Trainer]].
    * @param t
    *   [[Trainer]] affected by the update
    * @param s
    *   [[FieldSide]] to apply instead the current [[FieldSide]] of [[t]]
    * @return
    *   Field with the side changed
    */
  def changeSide(t: Trainer, s: FieldSide): Field =
    def updateSide(trainer: Trainer, side: FieldSide): Map[Trainer, FieldSide] =
      oldField.sides.map((t1, s1) => if t1 == t then (t1, s) else (t1, s1))
    Field(updateSide(t, s), oldField.terrain, oldField.room, oldField.weather)

/** Builder to create new [[Field]].
  */
class FieldBuilder:
  private var terrain: Option[Terrain] = None
  private var room: Option[Room] = None
  private var weather: Option[Weather] = None

  /** [[Terrain]] setter.
    * @param t
    *   Terrain to apply
    */
  def setTerrain(t: Terrain): Unit = terrain = Some(t)

  /** [[Room]] setter.
    * @param r
    *   Room to apply
    */
  def setRoom(r: Room): Unit = room = Some(r)

  /** [[Weather]] setter.
    * @param w
    *   Weather to apply
    */
  def setWeather(w: Weather): Unit = weather = Some(w)

  /** Generate a battlefield.
    * @param sides
    *   One Side for every player
    * @return
    *   Battlefield
    */
  def build(sides: Map[Trainer, FieldSide]): Field =
    Field(sides, terrain, room, weather)

/** Create a [[Field]] from a list of [[Trainer]] and the configuration defined
  * by the [[FieldBuilder]].
  * @param trainers
  *   Trainers list
  * @param init
  *   FieldBuilder configuration - by default is empty
  * @return
  *   Field without [[FieldSide]]s
  */
def field(trainers: List[Trainer])(init: FieldBuilder => Unit =
  _ => ()): Field =
  val builder = new FieldBuilder
  init(builder)
  builder.build(trainers.map(t => t -> FieldSide()).toMap)
