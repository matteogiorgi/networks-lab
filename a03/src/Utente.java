/*
 * Classe astratta di tipo Runnable che implementa alcuni metodi utili
 * alle sottoclassi (Studente, Tesista, Professore).
 * Rappresenta i task da lanciare con l'Executor.
 *
 * È presente il metodo pubblico "lavora" che genera un intero pseudo-casuale
 * compreso tra due valori precedentemente definiti e addormenta l'Utente
 * simulando l'intervallo di permanenza nel laboratorio.
 */

import java.util.Random;

public abstract class Utente implements Runnable
{
    protected final String utenteID;
    protected final Laboratorio lab;
    protected int k = new Random().nextInt(3) + 1;
    protected int tempoMin = 2000;
    protected int tempoMax = 5000;

    protected Utente(String utenteID, Laboratorio lab)
    {
        this.utenteID = utenteID;
        this.lab = lab;
    }


    public void statoUtente()
    {
        System.out.println(utenteID + " accede " + k + " volte al laboratorio");
    }


    public int lavora()
    {
        int tempoLavoro = new Random().nextInt(tempoMax-tempoMin) + tempoMax-tempoMin;
        try {
            Thread.sleep(tempoLavoro);
        } catch (InterruptedException e) {
            System.out.println("IL LABORATORIO STA CHIUDENTO, "+utenteID+" ESCE.");
            System.exit(0);
        }
        return tempoLavoro;
    }
}
