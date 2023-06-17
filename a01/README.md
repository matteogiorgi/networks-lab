# Assegnamento 01 - Threadpool e files

Scrivere un programma che dato in input una lista di directories, comprima tutti i file in esse contenuti, con l'utility gzip.

> ##### Ipotesi semplificativa: zippare solo i file contenuti nelle directories passate in input, non considerare ricorsione su eventuali soIttodirectories.

Il riferimento ad ogni file individuato viene passato ad un task, che deve essere eseguito in un threadpool; individuare nelle API JAVA la classe di supporto adatta per la compressione.

NOTA: l'utilizzo dei threadpool è indicato, perchè I task presentano un buon mix tra I/O e computazione:
* I/O heavy: tutti i file devono essere letti e scritti
* CPU-intensive: la compressione richiede molta computazione

> ##### Facoltativo: comprimere ricorsivamente I file in tutte le sottodirectories.




## Esecuzione assegnamento

Compilare i sorgenti presenti in `/src` con `javac` ed eseguire la classe `MainClass` passando per argomento la lista delle directory nelle quali comprimere (ricorsivamente) i file in esse contenuti.

Nota che l'argomento di `MainClass` può anche essere un elenco vuoto, in tal caso il programma si limiterà a procedere su una lista vuota di directory senza lanciare nessuna eccezione.