/*
 * Classe che rappresenta il conto corrente.
 * Possiede i campi "owner" contenente il possidente del conto e "records"
 * contenente l'elenco dei movimenti presenti nel conto.
 *
 * Sono presenti i metodi pubblici "getOwner" e "getRecords" per il retrieve
 * dei rispettivi campi, oltre a "addRecord" necessario per inserire un
 * Movimento del conto.
 */


import java.util.LinkedList;
import java.util.List;


public class ContoCorrente
{
    private final String owner;
    private final List<Movimento> records;

    /*
     * Costruttore1 alloca un oggetto conto corrente contenente:
     *     - owner: titolare passato per orgomento
     *     - records: lista movimenti vuota
     */
    public ContoCorrente(String owner)
    {
        this.owner = owner;
        this.records = new LinkedList<>();
    }

    /*
     * Costruttore2 alloca un oggetto conto corrente contenente:
     *     - owner: titolare passato per orgomento
     *     - records: lista movimenti preallocata passata per argomento
     */
    public ContoCorrente(String owner, List<Movimento> records)
    {
        this.owner = owner;
        this.records = records;
    }

    public String getOwner()
    {
        return owner;
    }

    public List<Movimento> getRecords()
    {
        return records;
    }

    public void addRecord(Movimento record)
    {
        records.add(record);
    }
}
