# PPS-24-Skalámon

**Skalámon** is a faithful clone of the classic 
Pokémon First Generation battle system.
It features a turn-based strategic combat mechanic
between two teams of Pokémon (or rather _Skalámon_), each controlled by a trainer.

## Features

- Enjoy intense offline battles between Skalámon creatures with your friends
- Battle logic faithfully inspired by the original game mechanics
- Create your own Skalámon generation using a fluent and expressive DSL
- Minimalist GUI that keeps the focus on your strategy
- Turn-based system designed for competitive and casual play
- Quick to set up — just download, launch, and start battling

## Requirements

// TODO

## How to play


**Download the latest JAR file from the GitHub releases page.**

Run the JAR, then you and your friend can create your own teams and join the battle.

### Commands

Player 1
- Moves are assigned to buttons: **Q - W - E - R**
- Skalámon Team members are assigned to buttons: **1 - 2 - 3 - 4 - 5 - 6**

Player 2
- Moves are assigned to buttons: **Z - X - C - V**
- Skalámon Team members are assigned to buttons: **A - S - D - F - G**

One action per turn per player: if a move is executed, switching is not allowed — and vice versa.

The turn automatically progresses once both players have made their choices.

// TODO: add game screenshot

## Create your Skalámon generation

### Skalámon creatures DSL

Easy way to create your Skalámon using a fluent DSL

Customize the following attributes:
1. **Name** – Give your Skalámon a unique name
2. **Types** – Specify one or more elemental types
3. **Max HP** – Define its maximum health points
4. **Weight** – Set its weight
5. **Ability** – Set its weight
6. **Statistics** – Define its default statistics:
   1. _Attack_
   2. _Defense_
   3. _Special Attack_
   4. _Special Defense_
   5. _Speed_
7. **Moves set** – Assign one to four moves

```scala 3
 def pikachu: Pokemon =
    pokemon("Pikachu"):
      _ typed Electric hp 35 weighing 6.0.kg ability static stat Attack -> 55 stat Defense -> 40 stat SpecialAttack -> 50 stat SpecialDefense -> 50 stat Speed -> 90 moves (
        tackle,
        quickAttack,
        thunderbolt,
        thunderWave
      )
```

```scala
def bulbasaur: Pokemon =
    pokemon("Bulbasaur"):
      _ typed Grass + Poison hp 45 weighing 6.9.kg ability naturalCure stat Attack -> 49 stat Defense -> 49 stat SpecialAttack -> 65 stat SpecialDefense -> 65 stat Speed -> 45 moves(
        tackle,
        grassKnot,
        razorLeaf,
        bulletSeed
      )
```

### Move DSL

Creating your Skalámon moves is just as easy. You can customize:

1. **Name** – The move’s name
2. **Priority** – The move’s execution priority
3. **Type** – Choose one elemental type
4. **Category** – One of: _Physical_, _Special_, or _Status_
5. **Accuracy** – The chance to hit
6. **Power Points (PP)** – Number of times the move can be used
7. **Success Behavior** – Effects that trigger on success (can be combined)
8. **Failure Behavior** – Effects that trigger on failure (can be combined)


```scala
def quickAttack: Move =
    move("Quick Attack", Normal, Physical):
      _ pp 30 priority 1 onSuccess SingleHitBehavior(40)
```

```scala
def dragonDance: Move =
    import it.unibo.skalamon.model.behavior.kind.+
    move("Dragon Dance", Dragon, Status):
      _ pp 20 onSuccess new BehaviorGroup(
        StatChangeBehavior(Attack + 1),
        StatChangeBehavior(Speed + 1)
      ) with TargetModifier(Self)
```

### Ability DSL

Create your abilities by customizing:
1. **Name** – The name of the ability
2. **Hooks** – A list of behaviors triggered by specific game events

```scala
def sandStream: Ability =
    ability("Sand Stream"):
      _.on(ActionEvents.Switch): (source, _, switch) =>
        if switch.in is source then
          WeatherBehavior(Sandstorm(_))
        else
          nothing
```

### Field effects DSL

TODO
