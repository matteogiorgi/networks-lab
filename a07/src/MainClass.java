/*
 * Classe contenente il main. Qui viene letto "accounts.json" (con Gson-Streaming) e,
 * ad ogni ciclo, lanciato un thread Lettore con il ContoCorrente appena allocato.
 * Il thread si mette poi in wait su "singleton", in attesa che tutti i lettori abbiano
 * notificato la terminazione del loro compito. In ultimo stampa i risultati archiviati
 * su "contatoreGlobale"
 */


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;


public class MainClass
{
    public static void main(String... args) throws JsonIOException, JsonSyntaxException, IOException, InterruptedException
    {
        // Inizializzo una Map con le coppie chiave valore di
        // tutte le possibili causali usando una anonymus subclass
        Map<String,Integer> contatoreGlobale = new ConcurrentHashMap<>();

        // Funzione che aggiorna il contatore gloabale
        // da passare ai singoli lettori
        // BiConsumer<String,Integer> aggiornaContatoreGlobale = (key, val) -> contatoreGlobale.merge(key, val, Integer::sum);

        // Dichiaro un ExecutorService per lanciare i lettori
        // e il SingletonOperator per sincronizzare i threads
        ExecutorService executor = Executors.newCachedThreadPool();
        SingletonOperator<Optional<Integer>> singleton = SingletonOperator.get(Optional.of(0));

        // Scorro il .json campo per campo, creo i
        // conti corrente e faccio partire i lettori
        FileReader fr = new FileReader(args[0]);
        try (JsonReader jr = new JsonReader(fr)) {
            jr.beginArray();
            while (jr.hasNext()) {
                jr.beginObject();
                jr.nextName(); String owner = jr.nextString();
                ContoCorrente conto = new ContoCorrente(owner);
                jr.nextName();
                jr.beginArray();
                while (jr.hasNext()) {
                    jr.beginObject();
                    jr.nextName(); String date = jr.nextString();
                    jr.nextName(); String records = jr.nextString();
                    Movimento m = new Movimento(date, records);
                    conto.addRecord(m);
                    jr.endObject();
                }
                jr.endArray();
                jr.endObject();
                executor.execute(new Lettore(conto, (k, v) -> contatoreGlobale.merge(k, v, Integer::sum)));
                singleton.flick(o -> o.ifPresent(i -> --i));
            }
            jr.endArray();
        }

        // Spengo l'ExecutorService, mi metto in attesa della
        // terminazione di tutti i lettori e stampo i risultati
        executor.shutdown();
        singleton.check(o -> o.get()>0);
        contatoreGlobale.forEach((r,i) -> System.out.println("Numero totale di "+r+" = "+i));
    }
}
