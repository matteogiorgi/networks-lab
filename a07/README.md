# Assegnamento 07 - Gestione conti corrente

Viene dato un file JSON compresso (in formato GZIP) contenente i conti correnti di una banca.
La struttura del file JSON è descritta [a questo link](https://elearning.di.unipi.it/pluginfile.php/60902/mod_resource/content/1/Struttura%20allegato%20assignment.pdf).

Ogni conto corrente contiene il nome del correntista ed una lista di movimenti.
I movimenti registrati per un conto corrente possono essere molto numerosi, per ogni movimento vengono registrati la data e la causale del movimento.
L'insieme delle causali possibili è fissato: `Bonifico`, `Accredito`, `Bollettino`, `F24`, `PagoBancomat`.

Progettare un'applicazione che attiva un insieme di thread. Uno di essi legge dal file gli oggetti "conto corrente" e li passa, uno per volta, ai thread presenti in un thread pool. Si vuole trovare, per ogni possibile causale, quanti movimenti hanno quella causale.

La lettura dal file deve essere fatta utilizzando l'API GSON per lo streaming.




## Esecuzione assegnamento

Compilare i sorgenti presenti in `/src` con `javac` usando la libreria `gson.jar` in allegato ed eseguire `MainClass` passando per argomento il file .json da leggere.
