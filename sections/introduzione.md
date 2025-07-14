# Introduzione

Il progetto *Skálamon* si propone di realizzare un sistema per la simulazione di battaglie Pokémon a turni fedele alla celebre serie Nintendo.

Le battaglie Pokémon sono combattimenti strategici a turni in cui due allenatori si sfidano utilizzando una squadra di massimo 6 Pokémon ciascuno. Ogni Pokémon può imparare fino a 4 mosse, ciascuna con effetti diversi. Durante ogni turno, entrambi i giocatori selezionano contemporaneamente un’azione tra le seguenti: eseguire una delle mosse disponibili o sostituire il Pokémon attivo con un altro della propria squadra (non KO). I turni vengono poi risolti tenendo conto di priorità e velocità. Il combattimento termina quando un allenatore non ha più Pokémon disponibili, decretando la vittoria dell’avversario.

L’obiettivo del progetto è quello di simulare correttamente queste meccaniche fondamentali, garantendo coerenza con le regole di gioco e una gestione efficace delle diverse componenti coinvolte (Pokémon, mosse, stato della squadra, input del giocatore, risoluzione dei turni, ecc.).

Sarà inoltre fondamentale che l’architettura risulti modulare e ben organizzata, così da favorire l’aggiunta di nuove funzionalità senza compromettere la stabilità del codice esistente. Un buon risultato sarà anche misurabile dalla facilità con cui sarà possibile testare, comprendere e manutenere il sistema, anche in scenari complessi o in evoluzione.