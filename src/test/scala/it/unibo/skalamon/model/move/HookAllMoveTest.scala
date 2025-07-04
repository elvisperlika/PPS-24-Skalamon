package it.unibo.skalamon.model.move

import it.unibo.skalamon.PokemonTestUtils
import it.unibo.skalamon.controller.battle.BattleController
import it.unibo.skalamon.controller.battle.action.SwitchAction
import it.unibo.skalamon.model.battle.turn.BattleEvents.CreateWeather
import it.unibo.skalamon.model.battle.{Battle, BattleState, Trainer}
import it.unibo.skalamon.model.field.fieldside.FieldSide
import it.unibo.skalamon.model.field.weather.Snow
import it.unibo.skalamon.model.field.{Field, field}
import it.unibo.skalamon.model.pokemon.BattlePokemon
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class HookAllMoveTest extends AnyFlatSpec with should.Matchers:

  var trainerBob: Trainer =
    PokemonTestUtils.trainerBob.copy(_inField =
      Some(PokemonTestUtils.trainerBob.team.head)
    )
  var trainerAlice: Trainer =
    PokemonTestUtils.trainerAlice.copy(_inField =
      Some(PokemonTestUtils.trainerAlice.team.head)
    )

  var battle: Battle = Battle(trainerBob :: trainerAlice :: Nil)
  var controller: BattleController = BattleController(battle)
  def currentState: BattleState = battle.currentTurn.get.state.snapshot
  def getTrainerFromState(name: String): Trainer =
    battle.currentTurn.get.state.snapshot.trainers.find(_.name == name).get
  def alice: Trainer = getTrainerFromState("Alice")
  def bob: Trainer = getTrainerFromState("Bob")

  val aliceAction: SwitchAction = SwitchAction(trainerBob.team.head)
  val bobAction: SwitchAction = SwitchAction(trainerAlice.team.head)

  "HookAllMove" should "add all field effects pokemon rules but they are not effective" in:
    // start with empty field
    currentState.field shouldBe field(trainerBob :: trainerAlice :: Nil)()
    controller.start() // start turn
    controller.update() // waiting moves
    controller.registerAction(alice, aliceAction)
    controller.registerAction(bob, bobAction)
    controller.update() // execute moves
    controller.update() // end turn
    alice.inField.get.currentHP == trainerAlice.inField.get.currentHP
    bob.inField.get.currentHP == trainerBob.inField.get.currentHP

  it should "mutate Pokémon with Weather effect" in:
    controller.start() // start turn
    controller.update() // waiting moves
    controller.registerAction(alice, aliceAction)
    controller.registerAction(bob, bobAction)
    controller.update() // execute moves
    controller.update() // end turn

    currentState.field shouldBe Field(
      sides = Map(bob -> FieldSide(Nil), alice -> FieldSide(Nil)),
      terrain = None,
      room = None,
      weather = Some(Snow(1)) // damage all not Ice type Pokémon for 10 hp
    )
    // both Pokémon in field are not Ice type so both will take damage
    alice.inField.get.currentHP shouldEqual trainerAlice.inField.get.currentHP - 10
    bob.inField.get.currentHP shouldEqual trainerBob.inField.get.currentHP - 10
