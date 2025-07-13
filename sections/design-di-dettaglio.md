# Design di dettaglio

### Team builder

All’avvio del software, gli utenti potranno selezionare il loro team per il combattimento. Ciò avviene tramite il team builder, che mostrerà un dizionario che associa un carattere ad ogni Pokémon selezionabile. A quel punto ogni giocatore potrà selezionare il team fornendo una stringa di caratteri validi.

`// uml #teamBuilder`

### Behaviors

Come accennato, un singolo comportamento di mosse ed abilità è modellato da un `Behavior`. L’insieme dei comportamenti viene contenuto in un oggetto `WithBehaviors`, che trova implementazione nei *contesti* di mosse ed abilità. Un contesto contiene le informazioni sull’azione corrispondente, il Pokémon che l’ha scaturita ed il Pokémon target.

Infine, ogni behavior viene tradotto in un’alterazione dello stato di gioco per via di un `BattleStateUpdaterBehaviorVisitor`, che genera una nuova istanza immutabile del `BattleState`.

`// uml #behaviors_design`

### Field

Il Field è un'entità dinamica e modulare che rappresenta l’ambiente in cui si svolge la battaglia. Il `Field` è composta da due lati (FieldSide), uno per ciascuna squadra coinvolta, ed è in grado di ospitare effetti attivi come Weather, Terrain, Room e Side Condition, i quali modificano temporaneamente le regole del combattimento. Questi effetti sono modellati tramite l’interfaccia `FieldEffect`, estendibile con mixin per aggiungere il nome e la descrizione, `TypeModifiers` per definire nuovi moltiplicatori dei tipi ed `Hooks`  per aggiungere reazione ad eventi che applicano `PokemonRule` che a loro volta agiscono sui Pokémon in base al tipo o a condizioni specifiche, applicandosi in risposta a determinati eventi (ad esempio, lo Switch di un Pokémon). Per semplificare la creazione di queste regole, abbiamo introdotto un costruttore fluente chiamato Modify, che consente di definire trasformazioni condizionate sui Pokémon, distinguendo tra tipi inclusi, esclusi o tutti. L’uso del `FieldBuilder` infine permette di istanziare nuovi campi in modo chiaro e dichiarativo, facilitando la configurazione iniziale delle battaglie.

`// uml field_design`

### Battle rules

Nel nostro sistema, le `BattleRule` definiscono le regole fondamentali che governano lo svolgimento dei turni in battaglia, in particolare l’ordine di esecuzione delle azioni. Ogni implementazione stabilisce una strategia di ordinamento diversa in base a criteri come priorità e velocità dei Pokémon. Questo approccio modulare permette di cambiare dinamicamente le regole di battaglia semplicemente sostituendo la strategia di ordinamento. È possibile andare a modificare le regole dello scontro attraverso un altro componente del Field mixin: `MutatedBattleRule`.

`// battle_rule_design`

### Expirable

Nel nostro sistema, alcuni effetti del campo di battaglia sono temporanei e devono cessare automaticamente dopo un certo numero di turni. Per gestire questa esigenza in modo modulare e riutilizzabile, abbiamo introdotto il trait `Expirable`, un mixin che può essere aggiunto a qualsiasi effetto `FieldEffect` o `Status` per fornire funzionalità legate alla durata. Questo trait memorizza il turno di creazione dell’effetto e la sua durata, ed espone metodi come `isExpired` e `turnsLeft` per verificarne la validità nel corso del tempo. Gli effetti o status espirati vengono rimossi automaticamente tramite l’estensione `removeExpiredEffects` definita nel modulo `ExpirableSystem`.

`// #expirable`

### Status

Durante la battaglia, i Pokémon possono essere affetti da Status, condizioni che modificano temporaneamente o permanentemente il loro comportamento o i loro punti vita. Il sistema distingue tra due tipi principali di status: non-volatili e volatili, rappresentati rispettivamente dai trait `NonVolatileStatus`, che persistono per qualche turno, e `VolatileStatus`, che persistono fino a quando il Pokémon non viene scambiato. I `VolatileStatus` vengono rimossi tramite il trait `Expirable` citato in precedenza.

`// uml #status_design`