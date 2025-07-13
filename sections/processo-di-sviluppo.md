# Processo di sviluppo

# Scrum

La metodologia di sviluppo applicata su questo progetto è di tipo Agile, in particolare si è adottata la variante *Scrum*.

Il team si è stato organizzato nel seguente modo:

- **Product Owner** — Giorgio Garofalo — gestirà il processo di sviluppo, monitorandone l’avanzamento e verificando che i requisiti siano correttamente rispettati
- **Scrum Master** — Elvis Perlika — si assicurerà che il processo Scrum scorra regolarmente e medierà le interazioni tra il Team ed il Product Owner
- **Development Team** — Giorgio Garofalo, Elvis Perlika, Norbert Gabos — si occuperà di sviluppare le feature del progetto e stimare

## **Pianificazione Sprint**

Poiché il team ed il dominio sono nuovi si è scelto di attuare una versione variata di Scrum con sessioni tra i veri Sprint più brevi di cerca 4 giorni, a differenza dei più canonici 7. 

Le riunioni saranno svolte su Discord e l’organizzazione dei task è gestita attraverso GitHub Projects.

## **Assegnazione**

Nella seguente tabella si riportano i vari task assegnati ai vari Developer negli Sprint che hanno portato al rilascio della prima release:

| **Sprint** | **Date** |
| --- | --- |
| 0 | 04/06/2025 |
| 1 | 09/06/2025 |
| 2 | 16/06/2025 |
| 3 | 20/06/2025 |
| 4 | 27/06/2025 |

| **Sprint** | **Date** |
| --- | --- |
| 5 | 23/06/2025 |
| 6 | 30/06/2025 |
| 7 | 04/06/2025 |
| 8 | 08/06/2025 |
|  |  |

| **Feature** | **Dev** | **0** | **1** | **2** | **3** | **4** | **5** | **6** | **7** | **8** |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Design MVC | Full Team | x |  |  |  |  |  |  |  |  |
| Implementazione dei behavior | Giorgio Garofalo |  | x | x | x | x |  |  |  |  |
| Modellazione delle abilità | Giorgio Garofaloa |  | x |  |  |  |  |  |  |  |
| Logica di fine partita | Elvis Perlika |  | x |  |  |  |  |  |  |  |
| Efficacia dei tipi | Elvis Perlika |  | x |  |  |  |  |  |  |  |
| Modellazione dei Pokémon | Norbert Gabos |  | x |  |  |  |  |  |  |  |
| Effetti di stato | Norbert Gabos |  | x |  |  |  |  |  |  |  |
| Gestione della GUI | Norbert Gabos |  | x | x | x | x | x | x | x | x |
| Gestione del campo di combattimento (room, terrain, weather) | Elvis Perlika |  |  | x | x |  |  |  |  |  |
| Ability factory | Giorgio Garofalo |  |  | x | x |  |  |  |  |  |
| Update loop | Giorgio Garofalo |  |  | x | x |  |  |  |  |  |
| Ordering delle azioni da eseguire durante il combattimento | Elvis Perlika |  |  | x | x |  |  |  |  |  |
| Gestione status del Pokémon scaduti | Norbert Gabos |  |  | x | x |  |  |  |  |  |
| Switch Pokémon | Elvis Perlika |  |  | x |  |  |  |  |  |  |
| Catalogazione dei Pokémon | Norbert Gabos |  |  |  |  | x | x | x | x |  |
| Battle loop/routine | Giorgio Garofalo |  |  |  |  | x |  |  |  |  |
| Abilità applicate al BattleState | Giorgio Garofalo |  |  |  |  | x |  |  |  |  |
| Update BattleState ad ogni turn stage event | Giorgio Garofalo |  |  |  |  | x |  |  |  |  |
| Categoria e Abilità delle mosse | Elvis Perlika |  |  |  |  | x |  |  |  |  |
| Calcolo potenza della mosse (Pokémon Gen1) | Elvis Perlika |  |  |  |  | x |  |  |  |  |
| Gestione effetti del campo di battaglia con scadenza | Elvis Perlika |  |  |  |  | x |  |  |  |  |
| Fix campo ‘inField’ non aggiornato | Giorgio Garofalo |  |  |  |  |  | x |  |  |  |
| DSL Pokémon | Giorgio Garofalo |  |  |  |  |  | x |  |  |  |
| Decremento PP mosse | Giorgio Garofalo |  |  |  |  |  |  | x |  |  |
| Team builder | Giorgio Garofalo |  |  |  |  |  |  |  | x |  |
| Room, Weather, Side condition behavior | Elvis Perlika |  |  |  |  |  |  |  | x |  |
| Schermata di GameOver | Norbert Gabos |  |  |  |  |  |  |  | x |  |

## **Modalità revisione Sprint**

Durante il periodo di sviluppo i membri del Team aprono le Pull Request (P.R.) con l’implementazione della feature assegnata. Ad inizio Sprint, ogni membro del Team che ha aperto una P.R. espone la funzionalità implementata e le scelte che ha fatto in merito, ricevendo feedback dagli altri membri del Team.

Nel caso la P.R. venga accolta con feedback positivi e abbia superato i test si prosegue con il Merge, in caso contrario si lascia aperta e la feature è rinviata allo Sprint successivo.

Al termine delle revisioni si procede con l’assegnazione dei nuovi Task.

# Test Driven Development

Per un buon mantenimento della qualità del codice si adotta il **Test Driven Development** con l’obiettivo di mantenere un livello di qualità del codice quanto più alto. Gli strumenti che si andranno ad utilizzare sono:

- ***ScalaTest*** per lo sviluppo dei vari test
- ***SCoverage*** per quantificare e monitorare la copertura dei test sul sorgente di produzione

# Continuous Integration & Delivery

Per mantenere un buon livello di qualità ed affidabilità del codice sono stati sviluppati workflow che eseguono i test del sorgente e controllando che lo stile sia conforme in tutte le porzioni di codice.

Per la parte di delivery è stato creato un workflow che si occupa di produrre il Jar di release.

# Versioning

Si è scelto di adottare di utilizzare la struttura dei **Conventional Commits** per mantenere uniformità nella parte di commitment del codice.

Per il versionamento del sistema si adotta lo standard ***Semantic Versioning**: d*ato un numero di versione nel formato *MAJOR.MINOR.PATCH*, si incrementi:

- la versione **MAJOR** quando si apportano modifiche che rendono il codice non-retrocompatibile
- la versione **MINOR** quando si aggiungono funzionalità, mantenendo il sistema retrocompatibile
- la versione **PATCH** quando si correggono bug, mantenendo il sistema retrocompatibile

# Quality Assurance

Per mantenere uniformità nel codice è stato integrato ***Scala Formatter***.

# **Tools**

Per il progetto in questione sono stati utilizzati i seguenti supporti:

- **Git** e **GitHub** — Code Versioning
- **GitHub Actions** — Continuous Integration
- **GitHub Projects** — Gestione dei tasks
- **Scalatest** — Sviluppo dei test
- **Scoverage** — Controllo della copertura dei test sul sorgente