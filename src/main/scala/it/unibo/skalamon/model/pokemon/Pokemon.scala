package it.unibo.skalamon.model.pokemon

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

case class Move(name: String) //TODO: to be removed, it is temporary till merge
case class Type(name: String) //TODO: to be removed, it is temporary till merge
case class Ability(name: String) //TODO: to be removed, it is temporary till merge

case class BasePokemon(
                        name: String,
                        gender: Gender,
                        types: List[Type],
                        baseStats: Stats,
                        ability: Ability,
                        weightKg: Double,
                        possibleMoves: List[Move]
                      )

case class MutablePokemon(
                            base: BasePokemon,
                            level: Int,
                            currentHP: Int,
                            moves: List[Move]
                          ):
  def actualStats: Stats = calculateStats(base.baseStats, level)
  
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
  def takeDamage(damage: Int): MutablePokemon =
    this.copy(currentHP = math.max(0, currentHP - damage))
