/*
 * Tra due accessi consecutivi, Studente attende un tempo pari all'estremo
 * inferiore dell'intervallo di permanenza nel laboratorio.
 */

public class Studente extends Utente
{
    public Studente(String utenteID, Laboratorio lab)
    {
        super(utenteID, lab);
    }

    @Override
    public void run()
    {
        try {
            for (; k-->0; Thread.sleep(tempoMin)) {
                lab.occupaPostazione(utenteID);
                int tempoLavoro = lavora();
                lab.rilasciaPostazione(utenteID, tempoLavoro);
            }
        } catch (InterruptedException e) {
            System.out.println("IL LABORATORIO STA CHIUDENTO, "+utenteID+" NON RIENTRA.");
            System.exit(0);
        }
    }
}
