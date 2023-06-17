# Assegnamento 03 - Gestione laboratorio

Il laboratorio di Informatica del Polo Marzotto è utilizzato da tre tipi di utenti (studenti, tesisti e professori) ed ogni utente deve fare una richiesta al tutor per accedere al laboratorio. I computer del laboratorio sono numerati da 1 a 20. Le richieste di accesso sono diverse a seconda del tipo dell'utente:
* i *professori* accedono in modo esclusivo a tutto il laboratorio, poichè hanno necessità di utilizzare *tutti* i computer per effettuare prove in rete
* i *tesisti* richiedono l'uso esclusivo di *un solo computer*, identificato dall'indice `i`, poiché su quel computer è installato un particolare software necessario per lo sviluppo della tesi
* gli *studenti* richiedono l'uso esclusivo di *un qualsiasi computer*

I professori hanno priorità su tutti nell'accesso al laboratorio, i tesisti hanno priorità sugli studenti; nessuno però può essere interrotto mentre sta usando un computer.
Scrivere un programma Java che simuli il comportamento degli utenti e del tutor:
* il programma riceve in ingresso il numero di studenti, tesisti e professori che utilizzano il laboratorio ed attiva un thread per ogni utente
* ogni utente accede `k` volte al laboratorio, con `k` generato casualmente
* simulare l'intervallo di tempo che intercorre tra un accesso ed il successivo e l'intervallo di permanenza in laboratorio mediante il metodo `sleep()`
* il tutor deve coordinare gli accessi al laboratorio
* il programma deve terminare quando tutti gli utenti hanno completato i loro accessi al laboratorio
* simulare gli utenti con dei thread e incapsulare la logica di gestione del laboratorio all'interno di un monitor




## Esecuzione assegnamento

Compilare i sorgenti presenti in `/src` con `javac` ed eseguire la classe `MainClass`.