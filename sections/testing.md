# Testing

Per la fase di testing abbiamo utilizzato **ScalaTest**, uno dei framework più diffusi e potenti per il testing in Scala. In particolare, ci siamo affidati alla sintassi fornita da **FlatSpec**, che consente di scrivere test in modo **descrittivo e leggibile**, favorendo una struttura narrativa che riflette chiaramente il comportamento atteso dei componenti.

Abbiamo seguito un approccio di **Test Driven Development (TDD)** durante lo sviluppo: ogni funzionalità è stata preceduta dalla scrittura dei test relativi. Questo approccio ci ha permesso di progettare un sistema modulare, con componenti ben separati e facilmente testabili. Inoltre, i test stessi hanno svolto un ruolo importante come **documentazione eseguibile**, facilitando la comprensione delle funzionalità anche in fase di manutenzione.

La suite di test copre in modo estensivo tutte le **logiche core del gioco**.

Per monitorare la copertura del codice, abbiamo utilizzato lo strumento **scoverage**, ottenendo un livello di copertura **molto alto**, che coinvolge la quasi totalità delle classi principali e gestisce anche **casi limite e scenari di errore**.
L’unica componente non sottoposta a test è stata la **View**, in quanto fortemente legata alla libreria `Swing` e all’interazione grafica tramite terminale, elemento difficile da verificare con test unitari. Tuttavia, questa parte è stata verificata manualmente in fase di integrazione, assicurandone il corretto comportamento.

Infine, abbiamo configurato un sistema di **Continuous Integration (CI)** tramite **GitHub Actions**, che esegue automaticamente la suite di test su ogni **Pull Request**. Questo processo ha garantito un costante controllo della qualità del codice, prevenendo regressioni e mantenendo il progetto stabile e affidabile durante l’intero sviluppo.