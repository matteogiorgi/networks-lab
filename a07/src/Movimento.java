/*
 * Classe che rappresenta i movimenti eseguiti nel singolo ContoCorrente.
 * La classe possiede due campi String: "date" che rappresenta la data
 * alla quale e' stato eseguito il movimento e "reason" che rappresenta la
 * causale del movimento.
 *
 * Sono poi presenti i metodi pubblici "getDate" e "getReason" per il
 * retrieve dei rispettivi campi.
 */


public class Movimento
{
    private final String date;
    private final String reason;
    
    public Movimento(String date, String reason)
    {
        this.date = date;
        this.reason = reason;
    }

    public String getDate()
    {
        return date;
    }

    public String getReason()
    {
        return reason;
    }
}
