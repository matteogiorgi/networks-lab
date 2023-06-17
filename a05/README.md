# Assegnamento 05 - Weblog

Il log file di un web server contiene un insieme di linee, con il seguente formato:

```
150.108.64.57 - - [15/Feb/2001:09:40:58 -0500] "GET / HTTP 1.0" 200 2511
```

in cui:
* `150.108.64.57` indica l'host remoto (in genere secondo la dotted quad form)
* `[data]`
* `HTTP` request
* status 
* bytes sent 
* eventuale tipo del client (es: `Mozilla/4.0 ...`) 

Scrivere un'applicazione *Weblog* che prende in input il nome del log file (pubblicato sulla pagina del corso) e ne stampa ogni linea, in cui ogni indirizzo IP è sostituito con l'hostname.

Sviluppare due versioni del programma, la prima single-threaded, la seconda che invece utilizza un thread pool, in cui il task assegnato ad ogni thread riguarda la traduzione di un insieme di linee del file.




## Esecuzione assegnamento

Compilare i sorgenti presenti in `/src` con `javac` ed eseguire `MainClassSingleThread` o `MainClassMultiThread` passando per argomento il nome del log file presente in questa stessa directory (`weblog.txt`).