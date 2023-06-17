/*
 * La classe Cliente implementa un task che simula di essere servito allo
 * sportello con una sleep di un numero casuale di secondi compreso tra 1 e 5.
 * Al termine della sua esecuzione il cliente risveglierà il thread Ufficio in
 * attesa che si liberi un posto nella coda della sala piccola; si metterà in
 * attesa fuori dall'ufficio per poi rimetteri in coda nella sala grande.
 * Questo comportamento viene eseguito un numero casuale di volte da (0 a 3).
 */

import java.util.Random;

public class Cliente implements Runnable
{
    final private String idCliente;
    final private int tempoAttesa = new Random().nextInt(5)+1;
    private int numeroAccessi = new Random().nextInt(3)+1;
    private Ufficio ufficioPostale;

    public Cliente(String idCliente, Ufficio ufficioPostale)
    {
        this.idCliente = idCliente;
        this.ufficioPostale = ufficioPostale;
    }

    @Override
    public void run()
    {
        while (numeroAccessi-->0) {
            // il clente vien servito
            System.out.println("VIENE SERVITO "+idCliente);
            try { Thread.sleep(1000*tempoAttesa); }
            catch(InterruptedException e) {
                System.out.println("L'UFFICIO POSTALE STA CHIUDENTO, "+idCliente+" ESCE APPENA FINISCE.");
                System.exit(0);
            }

            // il cliente risveglia ufficioPostale
            ufficioPostale.sveglia();

            // il cliente si rimette in attesa nella sala grande dopo aver
            // atteso un tempo pari a quella passato allo sportello
            try { Thread.sleep(1000*tempoAttesa); }
            catch(InterruptedException e) {
                System.out.println("L'UFFICIO POSTALE STA CHIUDENTO, "+idCliente+" NON RIENTRA.");
                System.exit(0);
            }
            ufficioPostale.add(this);
        }
    }
}