package it.unibo.skalamon.model.battle

import it.unibo.skalamon.PokemonTestUtils
import it.unibo.skalamon.controller.battle.BattleController
import it.unibo.skalamon.controller.battle.action.{
  Action,
  MoveAction,
  SwitchAction
}
import it.unibo.skalamon.model.battle.turn.BattleEvents.{
  Hit,
  PokemonSwitchIn,
  PokemonSwitchOut
}
import it.unibo.skalamon.model.behavior.kind.{DamageBehavior, SingleHitBehavior}
import it.unibo.skalamon.model.dsl.battling
import it.unibo.skalamon.model.move.MoveModel.Accuracy.Of
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.move.MoveModel.Category.Special
import it.unibo.skalamon.model.move.{BattleMove, Move}
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Male, Pokemon}
import it.unibo.skalamon.model.types.TypesCollection.Electric
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.util.UUID

class SwitchTest extends AnyFlatSpec with should.Matchers:

  var trainerBob: Trainer = PokemonTestUtils.trainerBob
  var trainerAlice: Trainer = PokemonTestUtils.trainerAlice
  val Damage = 10
  val startingHP = 100
  private val simpleMove =
    Move(
      name = "Thunder Shock",
      moveType = Electric,
      category = Special,
      pp = 10,
      accuracy = Of(100.percent),
      success = SingleHitBehavior(Damage)
    )
  val pikachu: BattlePokemon = BattlePokemon(
    Pokemon.pikachu,
    Male,
    startingHP,
    List(BattleMove(simpleMove, 10)),
    None,
    Set.empty
  )

  val machamp: BattlePokemon = Pokemon.machamp.battling(Male)
  val alakazam: BattlePokemon = Pokemon.alakazam.battling(Male)
  val alakazam2: BattlePokemon = Pokemon.alakazam.battling(Male)
  def init(): Unit =
    val bobTeam = machamp :: alakazam :: Nil
    val aliceTeam = pikachu :: alakazam2 :: Nil
    trainerBob = trainerBob.copy(team = bobTeam, _inField = Some(bobTeam.head))
    trainerAlice =
      trainerAlice.copy(team = aliceTeam, _inField = Some(aliceTeam.head))

  "Switch" should "set Trainer's Pokémon in field" in:
    init()
    val battle = Battle(trainerBob :: Nil)
    val controller = BattleController(battle)
    controller.start() // started
    controller.update() // waiting actions
    battle.currentTurn.get.state.snapshot.trainers.head.inField shouldBe Some(
      machamp
    )

    val bobAction = SwitchAction(pIn = alakazam)
    controller.registerAction(trainerBob, bobAction)
    controller.update() // executing actions
    battle.currentTurn.get.state.snapshot.trainers.head.inField shouldBe Some(
      alakazam
    )

  "Switch" should "be performed before a Move" in:
    init()
    val battle = Battle(trainerBob :: trainerAlice :: Nil)
    battle.eventManager.watch(PokemonSwitchIn) { p =>
      println(s"${p.base.name} joins the battle!")
    }
    battle.eventManager.watch(PokemonSwitchOut) { p =>
      println(s"${p.base.name} leaves the battle!")
    }
    battle.eventManager.watch(Hit) { m =>
      println(
        s"${m.source.base.name} hit ${m.target.base.name} with ${m.origin.move.name}!"
      )
    }
    val controller = BattleController(battle)
    controller.start() // started
    controller.update() // waiting actions

    def getTrainerFromState(name: String): Trainer =
      battle.currentTurn.get.state.snapshot.trainers.find(_.name == name).get

    def alice: Trainer = getTrainerFromState("Alice")
    def bob: Trainer = getTrainerFromState("Bob")

    bob.inField shouldBe Some(machamp)
    alice.inField shouldBe Some(pikachu)

    val bobAction = SwitchAction(pIn = alakazam)
    val aliceAction = MoveAction(
      move = alice.inField.get.moves.head,
      source = alice,
      target = bob
    )
    controller.registerAction(trainerBob, bobAction)
    controller.registerAction(trainerAlice, aliceAction)
    controller.update() // executing actions

    bob.team.find(
      _.id == machamp.id
    ).get.currentHP shouldEqual machamp.currentHP
    bob.team.find(
      _.id == alakazam.id
    ).get.currentHP shouldBe 54
