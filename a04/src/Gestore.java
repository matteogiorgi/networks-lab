import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

// il Gestore è una code concorrente, così che i Lettori
// possano estrarre un file da leggere in modo sincrono
public class Gestore extends ConcurrentLinkedQueue<String> implements Runnable
{
    private final int nThread;
    private int nFileDaLeggere;
    private ConcurrentHashMap<String,Integer> tabella = new ConcurrentHashMap<>();

    // il Gestore tiene anche conto del numero dei file da leggere (nFileDaLeggere)
    // in modo da controllare se tutti i Lettori hanno finito il loro compito
    public Gestore(List<String> listPath, int nThread)
    {
        super(listPath);
        this.nThread = nThread;
        this.nFileDaLeggere = this.size();
    }

    // metodo usato dai Lettori per aggiornare il contatore
    // dei file da leggere e svegliare il Gestore
    public synchronized void sveglia()
    {
        nFileDaLeggere--;
        notify();
    }

    // metodo che il Gestore usa per controllare se i Lettori hanno
    // finito il loro compito ed eventualmente mettersi in attesa
    private synchronized void check() throws InterruptedException
    {
        while (nFileDaLeggere>0)
            wait();
    }

    // metodo usato dai Lettori per aggiornare la tabella globale delle
    // occorrenze con i dati archiviati nella loro personale tabella
    public void aggiornaTabella(String lettera, Integer contatore)
    {
        tabella.merge(lettera, contatore, Integer::sum);
    }

    // metodo che il Gestore usa per stampare su file
    // i dati della tabella globale delle occorrenze
    private void stampaTabella(String fileName)
    {
        File fileDaScrivere = new File(fileName);
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileDaScrivere))) {
            BiConsumer<String,Integer> scriviDato = (lettera, contatore) -> {
                try { bWriter.write(lettera+", "+contatore+"\n"); }
                catch (IOException e) { e.printStackTrace(); }
            };
            tabella.forEach(scriviDato);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void run()
    {
        ExecutorService exe = Executors.newFixedThreadPool(nThread);
        forEach(filePath -> exe.execute(new Lettore(this)));

        exe.shutdown();

        try { check(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        stampaTabella("tabella_occorrenze");
    }
}
