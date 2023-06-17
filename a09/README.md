# Assegnamento 09 - NIO echo server

Scrivere:
- un programma *echo-server* usando la libreria java NIO (in particolare `Selector` e `Channels` in modalità non bloccante)
- un programma *echo-client* usando la libreria Java NIO (va bene anche con modalità bloccante).

Il client legge il messaggio da inviare da console, lo invia al server e visualizza quanto ricevuto dal server.
Il server accetta richieste di connessioni dai client, riceve messaggi inviati dai client e li rispedisce (eventualmente aggiungendo "echoed by server" al messaggio ricevuto).




## Esecuzione assegnamento

- Compilare i sorgenti presenti in `/src` con `javac`
- Eseguire `java MainClassServer`
- Eseguire `java MainClassClient` (disconnettere il Client con Ctrl-C)