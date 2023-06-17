/*
 * Classe le cui istanze di tipo Indexed rappresentano le singole postazioni
 * presenti nel laborarorio di informatica.
 * Contiene un identificatore numerico, il nome dell'utente che la sta utilizzando
 * ed un flag che conta le prenotazioni fatte dei tesisti.
 *
 * E' presente anche una funzione statica "spostaPosto" utile a spostare
 * la prima Postazione che rispetto un certo Predicate<? super Postazione> tra due
 * Deque<Postazione>.
 */

public class Postazione
{
    private final int postazioneID;
    private String utenteID;
    private int prenotato = 0;

    public Postazione(int postazioneID)
    {
        this.postazioneID = postazioneID;
        this.utenteID = "";
    }


    public int getIndex()
    {
        return postazioneID;
    }


    public String getUtilizzatoreID()
    {
        return utenteID;
    }


    public void setUtilizzatoreID(String utenteID)
    {
        this.utenteID = utenteID;
    }


    public boolean isPrenotato()
    {
        return prenotato>0;
    }


    public void prenota()
    {
        prenotato++;
    }


    public void cancellaPrenotazione()
    {
        prenotato--;
    }
}
