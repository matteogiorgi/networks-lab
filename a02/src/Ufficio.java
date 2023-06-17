/*
 * La classe Ufficio implementa il task che simula l'ufficio postale ed estende la classe
 * LinkedBlockingQueue, simulando cosi' la sala grande.
 * La sala piccola e' implementata con un ThreadPoolExecutor "salaPiccola" inizializzato
 * con un ArrayBlockingQueue per accodare i clienti e AbortPolicy che solleva una
 * RejectedExecutionException in caso di coda piena.
 *
 * Quando viene creata l'istanza dell'Ufficio, la sala grande viene riempita di
 * clienti (fino al massimo consentito); al momento dell'esecuzione dell'Ufficio,
 * verrà riempita la salaPiccola con i primi clienti contenuti nella sala grande
 * (facendo automaticamente partire i singoli clienti che verranno serviti agli sportelli).
 *
 * L'Ufficio controllerà ripetutamente il numero di clienti nelle due sale,
 * eventualmente spostando i clienti dalla grande alla piccola; quando non fosse possibile,
 * eseguirà una "wait()" in attesa che qualche cliente, che ha finito l'esecuzione, lo risvegli.
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Ufficio extends LinkedBlockingQueue<Cliente> implements Runnable
{
    private ThreadPoolExecutor salaPiccola;
    private int numClientiSalaPiccola;

    public Ufficio(int numSportelli, int numClientiIniziale, int numClientiSalaPiccola)
    {
        super();
        this.numClientiSalaPiccola = numClientiSalaPiccola;
        this.salaPiccola = new ThreadPoolExecutor(
                numSportelli,
                numSportelli,
                60L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(numClientiSalaPiccola),
                new ThreadPoolExecutor.AbortPolicy()
        );

        /* Tutti i clienti entrano nella sala grande */
        for (int i=0; i<numClientiIniziale; i++) {
            add(new Cliente("cliente_"+i, this));
        }
    }

    public synchronized void sveglia()
    {
        notify();
    }

    public synchronized void attesa() throws InterruptedException
    {
        while (salaPiccola.getQueue().size()==numClientiSalaPiccola) {
            wait();
        }
    }

    @Override
    public void run()
    {
        /* i primi clienti della coda si spostano nella sala piccola */
        for (int i=0; i<salaPiccola.getMaximumPoolSize(); i++) { 
            salaPiccola.execute(remove());
        }

        try {
            while (salaPiccola.getQueue().size()<numClientiSalaPiccola && !isEmpty()) {
                salaPiccola.execute(poll());
                attesa();
            }
            salaPiccola.shutdown();
            if (!salaPiccola.awaitTermination(15L, TimeUnit.SECONDS)) {
                System.out.println("L'UFFICIO POSTALE STA CHIUDENTO, CHI NON E' AGLI SPORTELLI ESCA.");
                salaPiccola.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            salaPiccola.shutdownNow();
        }
    }
}