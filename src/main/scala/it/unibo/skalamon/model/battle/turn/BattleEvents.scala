package it.unibo.skalamon.model.battle.turn

import it.unibo.skalamon.model.battle.{Trainer, Turn}
import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Room,
  SideCondition,
  Terrain,
  Weather
}
import it.unibo.skalamon.model.move.MoveContext
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.status.Status

object BattleEvents:
  object TurnStart extends EventType[Turn]
  object TurnEnd extends EventType[Turn]
  object ChangeStatus extends EventType[Status]
  object PokemonSwitchIn extends EventType[BattlePokemon]
  object PokemonSwitchOut extends EventType[BattlePokemon]
  object Hit extends EventType[MoveContext]
  object Miss extends EventType[MoveContext]
  object CreateWeather extends EventType[Weather]
  object CreateRoom extends EventType[Room]
  object ExpiredRoom extends EventType[Room]
  object CreateTerrain extends EventType[Terrain]
  object ExpiredTerrain extends EventType[Terrain]
  object CreateSideCondition extends EventType[SideCondition]
  object ExpiredSideCondition extends EventType[SideCondition]
