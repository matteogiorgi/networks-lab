/*
 * Tra due accessi consecutivi, Tesista attende un tempo pari al valor medi0 tra gli
 * estremi dell'intervallo di permanenza nel laboratorio.
 */

public class Tesista extends Utente
{
    private final int numPosto;
    public Tesista(String utenteID, Laboratorio lab, int numPosto)
    {
        super(utenteID, lab);
        this.numPosto = numPosto;
    }

    @Override
    public void run()
    {
        try {
            for (; k-->0; Thread.sleep((tempoMax+tempoMin)/2)) {
                lab.occupaPostazioneN(utenteID, numPosto);
                int tempoLavoro = lavora();
                lab.rilasciaPostazione(utenteID, tempoLavoro);
            }
        } catch (InterruptedException e) {
            System.out.println("IL LABORATORIO STA CHIUDENTO, "+utenteID+" NON RIENTRA.");
            System.exit(0);
        }
    }
}
