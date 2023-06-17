# Assegnamento 10 - Time server

Definire un Server TimeServer, che:
- invia su un gruppo di multicast dategroup, ad intervalli regolari, la data e l’ora
- attende tra un invio ed il successivo un intervallo di tempo simulato mediante il metodo sleep()

L’indirizzo IP di dategroup viene introdotto da linea di comando. Definire quindi un client TimeClient che si unisce a dategroup e riceve, per dieci volte consecutive, data ed ora, le visualizza, quindi termina.





## Esecuzione assegnamento

- Compilare i sorgenti presenti in `/src` con `javac`
- Eseguire `java TimeServer 239.1.10.1 51811`
- Eseguire `java TimeClient 239.1.10.1 51811`
