# Assegnamento 08 - Valutazione tecniche bufferizzazione di JAVA

Scopo dell'assignment è dare una valutazione delle prestazioni di diverse strategie di bufferizzazione di I/O offerte da JAVA.
Scrivere un programma che copi un file di input in un file di output, utilizzando le seguenti modalità alternative di bufferizzazione, valutando il tempo impiegato per la copia del file in ognuna delle seguenti strategie:

- FileChannel con buffer indiretti
- FileChannel con buffer diretti
- FileChannel utilizzando l'operazione transferTo()
- Buffered Stream di I/O
- stream letto in un byte-array gestito dal programmatore

Confrontare le prestazioni delle diverse soluzioni, variando la dimensione del file (da qualche kbyte fino ad almeno una decina di Megabyte) e la dimensione del buffer. Riportare i risultati ottenuti nel codice sorgente, in un commento.




## Esecuzione assegnamento

Compilare i sorgenti presenti in `/src` con `javac` ed eseguire `MainClass` passando per argomento i files sui quali fare i test.
