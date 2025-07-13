# Retrospettiva

Il sistema soddisfa tutti i requisiti iniziali previsti, inclusi quelli funzionali e orientati all’utente. Anche gli obiettivi relativi ai requisiti non funzionali sono stati pienamente conseguiti.

Il software può trovare migliorie future dal punto di vista dell’usabilità: per garantire una piena fedeltà con i giochi originali sarebbe stato necessario mostrare messaggi testuali che descrivono cosa accade durante la battaglia, per assicurare una buona comprensione di ciò che sta avvenendo. Nonostante ciò, l’esperienza utente rimane soddisfacente ed intuitiva.

Durante lo sviluppo del progetto non è stato seguito un approccio Agile in senso stretto. In particolare, anziché procedere per iterazioni e rilasci incrementali con funzionalità parziali ma funzionanti, è stata effettuata un’unica release finale a progetto completato. Questo ha comportato una minore interazione con l'utente finale durante le fasi intermedie.

Uno dei principi fondamentali dell’approccio Agile è infatti quello di consegnare frequentemente software funzionante, anche se in forma ridotta, e di assicurarsi che ogni funzionalità realizzata sia effettivamente fruibile dall’utente finale, idealmente tramite una rappresentazione concreta nell’interfaccia. Nel nostro caso, l’interfaccia grafica è stata integrata solo nelle fasi finali del progetto, quando la logica di gioco era già in gran parte completata e sviluppata tramite TDD. 

Si ritiene che il processo di sviluppo avrebbe potuto beneficiare sia di un’integrazione più progressiva dell’interfaccia grafica, sia di rilasci più frequenti e cadenzati, in modo da facilitare il collaudo continuo delle funzionalità, migliorare la qualità complessiva del prodotto e favorire un feedback più tempestivo.

Il team si dichiara pienamente soddisfatto sia del risultato ottenuto che della collaborazione interna. L’architettura del progetto si è dimostrata scalabile, consentendo al videogioco di evolversi facilmente con nuove versioni in grado di introdurre meccaniche complesse nel gameplay in modo semplice ed efficace, grazie all’uso di DSL dedicati e mixin modulari.