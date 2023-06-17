/*
 * Tra due accessi consecutivi, Professore attende un tempo pari all'estremo
 * superiore dell'intervallo di permanenza nel laboratorio.
 */

public class Professore extends Utente
{
    public Professore(String utenteID, Laboratorio lab)
    {
        super(utenteID, lab);
    }

    @Override
    public void run()
    {
        try {
            for (; k-->0; Thread.sleep(tempoMax)) {
                lab.occupaLaboratorio(utenteID);
                int tempoLavoro = lavora();
                lab.rilasciaLaboratorio(utenteID, tempoLavoro);
            }
        } catch (InterruptedException e) {
            System.out.println("IL LABORATORIO STA CHIUDENTO, "+utenteID+" NON RIENTRA.");
            System.exit(0);
        }
    }
}
