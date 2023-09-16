import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Scanner;

public class Lettore implements Runnable
{
    private final Gestore pathList;
    private HashMap<String,Integer> tabella = new HashMap<>();

    public Lettore(Gestore pathList)
    {
        // ogni lettore ha il riferimento al Gestore, in
        // modo da accedere ai metodi sincronizzati per:
        //   1. estrarre un file da leggere
        //   2. aggiornare la tabella globale
        //   3. risvegliare il Gestore una volta finito
        this.pathList = pathList;

        // inizializzo a 0 le occorrenze dei caratteri alfabetici
        // nel caso qualcuno di essi non comparisse in alcuno dei file
        for (char ch='a'; ch<='z'; ++ch) 
            this.tabella.put(String.valueOf(ch), 0);
        for (char ch='A'; ch<='Z'; ++ch) 
            this.tabella.put(String.valueOf(ch), 0);
    }

    @Override
    public void run()
    {
        // estraggo il file da leggere (sync)
        // e lo leggo solo se esiste e non è una directory
        File fileDaLeggere = new File(pathList.poll());
        if (fileDaLeggere.isFile()) {
            // leggo il file carattere per carattere, aggiornando le occorrenze
            try (Reader reader = new FileReader(fileDaLeggere)) {
                char[] buffer = new char[1024];
                int numCharsRead;
                while ((numCharsRead = reader.read(buffer)) != -1) {
                    for (int i = 0; i < numCharsRead; i++) {
                        char ch = buffer[i];
                        if (String.valueOf(ch).matches("^[a-zA-Z]*$")) {
                            tabella.merge(String.valueOf(ch), 1, Integer::sum);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // aggiorno la tabella globale (sync)
        // e risveglio il Gestore
        tabella.forEach(pathList::aggiornaTabella);
        pathList.sveglia();
    }
}
