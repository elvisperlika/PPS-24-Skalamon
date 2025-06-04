# Skalamon

## Table of Contents

- [Skalamon](#skalamon)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [1. Development Process](#1-development-process)
    - [1.1 Meetings](#11-meetings)
      - [1.1.1 Sprint Planning](#111-sprint-planning)
      - [1.1.2 Daily Scrum](#112-daily-scrum)
      - [1.1.3 Sprint Review](#113-sprint-review)
      - [1.1.4 Sprint Retrospective](#114-sprint-retrospective)
    - [1.2 Task Assignment](#12-task-assignment)
    - [1.3 Task Review](#13-task-review)
    - [1.4 Tools](#14-tools)
  - [2. Requirements](#2-requirements)
    - [2.1 Business Requirements](#21-business-requirements)
    - [2.2 User Requirements](#22-user-requirements)
    - [2.3 Functional Requirements](#23-functional-requirements)
    - [2.4 Non-functional Requirements](#24-non-functional-requirements)
    - [2.5 Implementation Requirements](#25-implementation-requirements)
  - [3. Domain Analysis](#3-domain-analysis)
    - [3.1 Intensional Aspects](#31-intensional-aspects)
    - [3.2 Extensional Aspects](#32-extensional-aspects)
    - [3.3 Common Aspects of Board Games](#33-common-aspects-of-board-games)
  - [4. Architectural Design](#4-architectural-design)
    - [4.1 Architectural Patterns](#41-architectural-patterns)
    - [4.2 Overall Architecture](#42-overall-architecture)
      - [4.2.1 Use of the MVC Pattern](#421-use-of-the-mvc-pattern)
      - [4.2.2 Subview Navigation](#422-subview-navigation)
  - [5. Detailed Design](#5-detailed-design)
    - [5.1 Domain Design](#51-domain-design)
    - [5.2 Extensions](#52-extensions)
    - [5.3 RuleSet and DSL](#53-ruleset-and-dsl)
    - [5.4 Interaction](#54-interaction)
      - [5.4.1 View](#541-view)
      - [5.4.2 Controller](#542-controller)
  - [6. Implementation](#6-implementation)
    - [6.1 Implicit Passing of Type Classes](#61-implicit-passing-of-type-classes)
    - [6.2 DSL Syntax](#62-dsl-syntax)
    - [6.3 CLI](#63-cli)
    - [6.4 App Configuration: GameSetup](#64-app-configuration-gamesetup)
    - [6.5 Games](#65-games)
    - [6.6 Testing](#66-testing)
      - [6.6.1 Test Doubles](#661-test-doubles)
      - [6.6.2 Test Style](#662-test-style)
    - [6.7 Task Division](#67-task-division)
      - [6.7.1 Giorgio Garofalo](#671-giorgio-garofalo)
      - [6.7.2 Elvis Perlika](#672-elvis-perlika)
      - [6.7.3 Norbert Gabos](#673-norbert-gabos)
  - [7. Retrospective](#7-retrospective)
    - [7.1 Sprint 1](#71-sprint-1)
    - [7.2 Sprint 2](#72-sprint-2)
    - [7.3 Sprint 3](#73-sprint-3)
    - [7.4 Sprint 4](#74-sprint-4)
    - [7.5 Sprint 5](#75-sprint-5)
    - [7.6 Sprint 6](#76-sprint-6)
  - [8. User Guide](#8-user-guide)

----

## Introduction

## 1. Development Process
### 1.1 Meetings
#### 1.1.1 Sprint Planning
#### 1.1.2 Daily Scrum
#### 1.1.3 Sprint Review
#### 1.1.4 Sprint Retrospective
### 1.2 Task Assignment
### 1.3 Task Review
### 1.4 Tools

## 2. Requirements
### 2.1 Business Requirements

/* Business Requirements why?:
answering to: why is this SW strategic?
high-level: project goals, customers hopes, developers goals...
should necessarily clarify: how do we judge if the project is successful? */

Business requirements define the "business solution" for a
project, encompassing customer needs and expectations.
Following an initial scoping phase, the following business
requirements have been identified:

 - Develop a system capable of simulating a Pokémon battle that
  closely mirrors the original game, while introducing a few
  minor variations.

 - Enable players to engage in battles either in a one-on-one
  local multiplayer mode or against an AI opponent.

### 2.2 User Requirements
/* User Requirements: how the projects result is used by end users
how users specifically interact with the SW?
which I/O constraints exist? */

 - Users will interact with the system using a keyboard and view
  the game through a graphical user interface (GUI).

 - Users will be able to see various elements that reflect the current
  state of the match, including:
   - Each player's remaining health
   - The attributes of each Pokémon (e.g., health, name, status, etc.)
   - The number of the opponent’s remaining Pokémon
   - The available actions they can take
   - The current Field status (e.g., weather, terrain, room effects)

 - Users can interact with the system by performing different actions,
  such as:
   - Selecting the Pokémon they want to bring into battle
   - Choosing one of the available actions to perform each turn

/* TODO: manca qualcosa */

### 2.3 Functional Requirements
/* Functional Requirements, what functionalities?
what are the functions/features the system provides?
2.1 User Requirements: how the projects result is used by end users
how users specifically interact with the SW?
which I/O constraints exist?
2.2 System Requirements: how the system internally works/operates
what are its rules of behaviour, norms, constraints? */

 - The player selects a team consisting of 1 to 6 Pokémon.

 - **Battle**:
   - Battles are turn-based.
   - In the first turn, each player chooses a Pokémon to send out.
   - On each turn, players choose one of the following actions:
     - Switch Pokémon  
     - Use one of the four available moves (which vary by Pokémon)
   - The match ends when all of a player’s Pokémon have fainted; the
   - opposing player is declared the winner.

 - **Pokémon**:
   - Each Pokémon is defined by several attributes:
     - **Descriptive**: Name, Gender (male/female/none), 1–2 types,
      1–4 moves, 1 passive ability, and weight (in kg)  
     - **Stats**: HP (Health Points), Attack, Defense, Special Attack,
      Special Defense, Speed  
       > These stats are affected by the Pokémon’s level.

 - **Turn**:
   - The system waits until both players have selected an action.
   - Actions are executed in order of priority, as explained later.
   - Once all actions are executed, the turn ends.

 - **Move**:
  Each move is defined by several properties:
   - Name  
   - Type  
   - Power  
   - Accuracy (either a percentage or `-`, meaning the move always hits)  
   - Power Points (number of times the move can be used)  
   - Priority: ranges from -7 to 6, determining which Pokémon acts first.
    In case of equal priority, Speed determines the   order.  
  - Target: *single*, *adjacent*, *all*, *self*, *random*  
    > (Note: target types are included for potential future support of 2v2
    battles.)  
  - Effect: could deal damage, apply a status condition, or alter stats
    (increase/decrease). Each effect has a percentage chance to occur.

- **Move Classes**:
  Each move belongs to a class that determines its behavior. Examples
  include:
  - Single-hit (default)  
  - Multi-hit  
  - Charge (e.g., *Solar Beam*, which skips one turn and attacks the next)

- **Type Efficiency**:
  A move’s type can interact with the target’s types in the following ways:
  - Neutral (1×)  
  - Resisted (0.5×)  
  - Super effective (2× or 4×)  
  - Immune (0×)  
  > All type effectiveness values are shown in the following chart:  
  ![alt text](https://raw.githubusercontent.com/elvisperlika/PPS-24-Skalamon/refs/heads/gh-pages/images/type-efficiency.png)

- **Field**:
  The battlefield can be affected by temporary environmental changes. Each
  lasts exactly 5 turns, and only one instance of each type can be active
  at any time:
  - **Weather**: Can be set by abilities or moves and may boost or reduce
    move effectiveness.
  - **Terrain**: Similar to Weather but only affects grounded Pokémon.
  - **Room Effects**: Also triggered by specific moves. Examples include:
    - *Trick Room*: Reverses move order within each priority bracket; slower
      Pokémon move before faster ones.  
    - *Wonder Room*: Swaps Defense and Special Defense stats.
  - **Side Conditions**: Can be stacked, triggered by specific moves, and
      affect only one side of the field.

- **Abilities**:
  Abilities are passive effects that can trigger at different phases:
  - When switching in  
  - When receiving damage  
  - At the start of each turn  
  - Before or after using a move  
  - At the end of each turn  
  - Upon being knocked out  
  - Permanent (always active)

- **Categories**:
  Similar to move classes, categories describe the effects or alterations a
  move or ability might produce. Examples include:
  - Stat-modifying  
  - Type-modifying  
  - Move-altering

### 2.4 Non-functional Requirements
/* Non-functional Requirements, what qualities?
what are the general properties and quality attributes of the solution?
performance, scalability, reliability, friendliness, . . . */

 - A minimal and intuitive graphical interface to ensure accessibility
  even for less experienced players.
 - Interface response times should be under 100 ms to maintain a smooth
  and fluid user experience.
 - Turn loading times must not exceed one second, especially when playing
  against an AI opponent.

### 2.5 Implementation Requirements
/* Implementation Requirements what, constraints on production ?,
they anticipate details of architecture/design/implementation/process
what are constraints for the implementor? and why
technologies, training, material, internal quality
do not anticipate design decisions! */

 - The code should be modular to facilitate the addition of new Pokémon,
  moves, and other game elements.
 - Design patterns will be used to separate game logic, user interface,
  and data. Specifically, the MVC (Model-View-Controller) pattern will be
  adopted for this purpose.
 - Each component should be easily testable in isolation.

The game will be entirely developed in **Scala** and tested using the
**Scalatest** library. For the graphical interface, the
[**AsciiPanel**](https://github.com/trystan/AsciiPanel) library will
be used.

/* TODO: inserire un eventuale libreria grafica o anche solo una libreria
TODO: che aiuti con l'integrazione del tutto in un terminale
TODO: vedere se useremo o meno AsciiPanel, nel caso è da tolgiere
TODO: mettere anche il prolog, cosi non dobbiamo fare il progettino a part */

## 3. Domain Analysis
### 3.1 Intensional Aspects
### 3.2 Extensional Aspects
### 3.3 Common Aspects of Board Games

## 4. Architectural Design
### 4.1 Architectural Patterns
### 4.2 Overall Architecture
#### 4.2.1 Use of the MVC Pattern
#### 4.2.2 Subview Navigation

## 5. Detailed Design
### 5.1 Domain Design
### 5.2 Extensions
### 5.3 RuleSet and DSL
### 5.4 Interaction
#### 5.4.1 View
#### 5.4.2 Controller

## 6. Implementation
### 6.1 Implicit Passing of Type Classes
### 6.2 DSL Syntax
### 6.3 CLI
### 6.4 App Configuration: GameSetup
### 6.5 Games
### 6.6 Testing
#### 6.6.1 Test Doubles
#### 6.6.2 Test Style
### 6.7 Task Division
#### 6.7.1 Giorgio Garofalo
#### 6.7.2 Elvis Perlika
#### 6.7.3 Norbert Gabos

## 7. Retrospective
### 7.1 Sprint 1
### 7.2 Sprint 2
### 7.3 Sprint 3
### 7.4 Sprint 4
### 7.5 Sprint 5
### 7.6 Sprint 6
## 8. User Guide














TODO: cancellare la parte di sotto (linee guida del prof)

La relazione dovrà contenere tassativamente i seguenti capitoli:

 - Processo di sviluppo adottato (modalità di divisione in itinere dei task, meeting/interazioni pianificate, modalità di revisione in itinere dei task, scelta degli strumenti di test/build/continuous integration) 
 - Requirement specification: 1) requisiti di business, 2) modello di dominio, 3) requisiti funzionali [ 3.1) utente, e 3.2) di sistema ], 4) requisiti non funzionali, 5) requisiti di implementazione)
 - Design architetturale (architettura complessiva, descrizione di pattern architetturali usati, eventuali componenti del sistema distribuito, scelte tecnologiche cruciali ai fini architetturali -- corredato da pochi ma efficaci diagrammi)
 - Design di dettaglio (scelte rilevanti di design, pattern di progettazione, organizzazione del codice -- corredato da pochi ma efficaci diagrammi)
 - Implementazione (per ogni studente, una sotto-sezione descrittiva di cosa fatto/co-fatto e con chi, e descrizione di aspetti implementativi importanti non già presenti nel design, come ad esempio relativamente all'uso di meccanismi avanzati di Scala)
 - Testing (tecnologie usate, grado di copertura, metodologia usata, esempi rilevanti, altri elementi utili)
 - Restrospettiva (descrizione finale dettagliata dell'andamento dello sviluppo, del backlog, delle iterazioni; commenti finali)

 Si noti che la retrospettiva è l'unica sezione che può citare aneddoti di cosa è successo in itinere, mentre le altre sezioni (anche i requisiti) fotografino il risultato finale. Se gli studenti decideranno (come auspicato) di utilizzare un product backlog e/o dei backlog delle varie iterazioni/sprint, è opportuno che questi siano file testuali tenuti in versione in una cartella "process", così che sia ri-verificabile a posteriori la storia del progetto. Si noti anche che la sezione di "Requisiti e Analisi" deve in modo completo e rigoroso descrivere il funzionamento "esterno" del sistema: da solo questa sezione, team diversi produrrebbero sistemi di fatto equivalenti.
