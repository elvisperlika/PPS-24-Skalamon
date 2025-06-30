package it.unibo.skalamon.model.battle

import it.unibo.skalamon.model.field.{Field, changeSide, field}
import it.unibo.skalamon.model.field.terrain.Mud
import it.unibo.skalamon.model.field.weather.Snow
import it.unibo.skalamon.model.battle.ExpirableSystem.removeExpiredEffects
import it.unibo.skalamon.model.field.fieldside.FieldSide
import it.unibo.skalamon.model.field.fieldside.kind.Spikes
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ExpirableSystemTest extends AnyFlatSpec with should.Matchers:

  val bob: Trainer = Trainer("Bob", Nil, None)
  val alice: Trainer = Trainer("Alice", Nil, None)

  "Expirable System" should "remove expired field status with all expired" in:
    var battleField: Field = field(bob :: alice :: Nil) { b =>
      b.setTerrain(Mud(1)) // Duration = 5 turns
      b.setWeather(Snow(1)) // Duration = 5 turns
    }
    battleField = battleField.removeExpiredEffects(7)
    battleField shouldEqual field(bob :: alice :: Nil)()

  it should "remove expired field status with part expired" in:
    var battleField: Field = field(bob :: alice :: Nil) { b =>
      b.setTerrain(Mud(1)) // Duration = 5 turns
      b.setWeather(Snow(4)) // Duration = 5 turns
    }
    battleField = battleField.removeExpiredEffects(7)
    battleField shouldEqual field(bob :: alice :: Nil)(_.setWeather(Snow(4)))

  it should "remove expired side conditions" in:
    var battleField: Field = field(bob :: alice :: Nil) { b =>
      b.setTerrain(Mud(1)) // Duration = 5 turns
      b.setWeather(Snow(4)) // Duration = 5 turns
    }
    // Spikes Duration = 5 turns
    battleField =
      battleField.changeSide(bob, FieldSide(Spikes(1) :: Spikes(5) :: Nil))
    battleField = battleField.removeExpiredEffects(7)
    battleField shouldEqual Field(
      sides = Map(alice -> FieldSide(), bob -> FieldSide(Spikes(5) :: Nil)),
      terrain = None,
      room = None,
      weather = Some(Snow(4))
    )
