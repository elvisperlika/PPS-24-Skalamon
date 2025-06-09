package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.move.*

case class Stats(
                  hp: Int,
                  attack: Int,
                  defense: Int,
                  specialAttack: Int,
                  specialDefense: Int,
                  speed: Int
                )

sealed trait Gender
case object Male extends Gender
case object Female extends Gender
case object Genderless extends Gender

case class Type(name: String) //TODO: to be removed, it is temporary till merge
case class Ability(name: String) //TODO: to be removed, it is temporary till merge

case class Pokemon(
                        name: String,
                        gender: Gender,
                        types: List[Type],
                        baseStats: Stats,
                        ability: Ability,
                        weightKg: Double,
                        possibleMoves: List[Move]
                      )

case class BattlePokemon(
                            base: Pokemon,
                            level: Int,
                            currentHP: Int,
                            moves: List[BattleMove]
                          ):
  def actualStats: Stats = calculateStats(base.baseStats, level)

  //TODO: battleMove, nonvolatilestatus: Option[Status], volitile: List[Status], trait Status, trait nonvolatileStatus, trait volatileStatus
  
  private def calculateStats(base: Stats, level: Int): Stats =
    def scale(stat: Int): Int = (stat * level / 100.0).round.toInt + 5
    
    Stats(
      hp = (base.hp * level / 100.0).round.toInt + level + 10,
      attack = scale(base.attack),
      defense = scale(base.defense),
      specialAttack = scale(base.specialAttack),
      specialDefense = scale(base.specialDefense),
      speed = scale(base.speed)
    )
  
  def isAlive: Boolean = currentHP > 0
  
  def takeDamage(damage: Int): BattlePokemon =
    this.copy(currentHP = math.max(0, currentHP - damage))
