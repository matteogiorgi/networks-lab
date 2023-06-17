/*
 * Classe che rappresenta il laboratorio di informatica contenente la lista dei posti
 * e due Deque<Postazione> contenenti ciascuna i posti disponibili e quelli già occupati,
 * utili a tenere traccia dello stato di Laboratorio.
 *
 * La classe mette a disposizione metodi thread-safe per l'acquisizione, rilascio
 * di un posto (utili per i Runnable Studente), per l'acquisizione, rilascio dell'intero
 * Laboratorio (utili a Professore) e per l'acquisizione di uno specifico posto
 * (utili per Tesista).
 *
 * I metodi di rilascio sono pensati per implementare le priorità richieste nelle
 * specifiche dell'esercizio (Professore -> Tesista -> Studente).
 *
 */


import java.util.*;
import java.util.function.Predicate;
import java.time.Clock;


public class Laboratorio extends ArrayList<Postazione>
{
    private int professoriFermi = 0;
    private final Clock clock = Clock.systemUTC();
    private Deque<Postazione> postazioniDisponibili;
    private Deque<Postazione> postazioniOccupate;

    public Laboratorio(int numPosti)
    {
        super(numPosti);
        for (int i=0; i<10; i++)
            add(new Postazione(i));

        super.sort(Comparator.comparingInt(Postazione::getIndex));

        postazioniDisponibili = new LinkedList<>(this);
        postazioniOccupate = new LinkedList<>();
    }


    private Postazione spostaPosto(Deque<Postazione> from, Deque<Postazione> to, Predicate<? super Postazione> pr, String utenteID)
    {
        Postazione daSpostare = null;
        for (Iterator<Postazione> i = from.iterator(); i.hasNext();) {
            daSpostare = i.next();
            if (pr.test(daSpostare)) {
                daSpostare.setUtilizzatoreID(utenteID);
                i.remove();
                to.addLast(daSpostare);
                break;
            }
        }
        return daSpostare;
    }


    public synchronized void occupaPostazione(String utenteID) throws InterruptedException
    {
        while (postazioniDisponibili.isEmpty() || professoriFermi>0 || stream().allMatch(Postazione::isPrenotato)) {
            System.out.println("\u001B[35m" + utenteID + " in attesa" + "\u001B[0m");
            wait();
        }

        Postazione postoPreso = spostaPosto(postazioniDisponibili, postazioniOccupate, p->!p.isPrenotato(), utenteID);
        System.out.println("\u001B[34m" + utenteID + " occupa la postazione " + postoPreso.getIndex() + " al tempo " + clock.millis() + "\u001B[0m");
    }


    public synchronized void rilasciaPostazione(String utenteID, int tempoLavoro)
    {
        Postazione postoRilasciato = spostaPosto(postazioniOccupate, postazioniDisponibili, p->p.getUtilizzatoreID().equals(utenteID), "");
        notifyAll();
        System.out.println("\u001B[32m" + utenteID + " ha lavorato alla postazione " + postoRilasciato.getIndex() + " per " + tempoLavoro + "ms e" + " termina al tempo " + clock.millis() + "\u001B[0m");
    }


    public synchronized void occupaLaboratorio(String utenteID) throws InterruptedException
    {
        professoriFermi++;
        while (!postazioniOccupate.isEmpty()) {
            System.out.println("\u001B[35m" + utenteID + " in attesa" + "\u001B[0m");
            wait();
        }

        // forEach(p->p.setUtilizzatoreID(utenteID));
        postazioniOccupate = new LinkedList<>(this);
        postazioniDisponibili = new LinkedList<>();

        professoriFermi--;
        System.out.println("\u001B[34m" + utenteID + " occupa il laboratorio al tempo " + clock.millis() + "\u001B[0m");
    }


    public synchronized void rilasciaLaboratorio(String utenteID, int tempoLavoro)
    {
        // forEach(p->p.setUtilizzatoreID(""));
        postazioniOccupate = new LinkedList<>();
        postazioniDisponibili = new LinkedList<>(this);

        notifyAll();
        System.out.println("\u001B[32m" + utenteID + " ha lavorato per " + tempoLavoro + "ms e" + " termina al tempo " + clock.millis() + "\u001B[0m");
    }


    public synchronized void occupaPostazioneN(String utenteID, int numPosto) throws InterruptedException
    {
        Postazione mioPosto = get(numPosto);
        mioPosto.prenota();
        while (postazioniDisponibili.isEmpty() || !mioPosto.getUtilizzatoreID().equals("") || (professoriFermi>0)) {
            System.out.println("\u001B[35m" + utenteID + " in attesa" + "\u001B[0m");
            wait();
        }

        Predicate<Postazione> check = posto -> posto.getIndex()==mioPosto.getIndex();
        Postazione postoPreso = spostaPosto(postazioniDisponibili, postazioniOccupate, check, utenteID);
        postoPreso.cancellaPrenotazione();
        System.out.println("\u001B[34m" + utenteID + " occupa la postazione "+ postoPreso.getIndex() + " al tempo " + clock.millis() + "\u001B[0m");
    }
}
