# Assegnamento 02 - Ufficio postale

Simulare il flusso di clienti in un ufficio postale che ha 4 sportelli. Nell'ufficio esiste:
* un'ampia sala d'attesa in cui ogni persona può entrare liberamente (quando entra, ogni persona prende il numero dalla numeratrice e aspetta il proprio turno in questa sala)
* una seconda sala, meno ampia e posta davanti agli sportelli, in cui si può entrare solamente a gruppi di k persone
* una persona inizialmente si mette in coda nella prima sala, poi passa nella seconda sala

Ogni persona impiega un tempo differente per la propria operazione allo sportello; una volta terminata l'operazione, la persona esce dall'ufficio.

Scrivere un programma in cui:

* l'ufficio viene modellato come una classe JAVA, in cui viene attivato un `ThreadPool` di dimensione uguale al numero degli sportelli
* la coda delle persone presenti nella sala d'attesa è gestita esplicitamente dal programma
* la seconda coda (davanti agli sportelli) è quella gestita implicitamente dal ThreadPool
* ogni persona viene modellata come un task, che deve essere assegnato ad uno dei thread associati agli sportelli
* si preveda di far entrare tutte le persone nell'ufficio postale, all'inizio del programma

> ##### Facoltativo: prevedere il caso di un flusso continuo di clienti e la possibilità che l'operatore chiuda lo sportello stesso dopo che in un certo intervallo di tempo non si presentano clienti al suo sportello.




## Esecuzione assegnamento

Compilare i sorgenti presenti in `/src` con `javac` ed eseguire la classe `MainClass`.