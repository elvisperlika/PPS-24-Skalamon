package it.unibo.skalamon.controller.battle.turn

enum TurnPhase:
  case PokemonGetInField
  case WaitingPlayersChooseMoves
  case ComputeMoves
  case EndTurn
