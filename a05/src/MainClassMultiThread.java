/*
 * Classe contenente il main.
 * Qui vengono aperti FileReader e FileWriter per leggere il logfile e scriverne
 * uno nuovo contenenti i nomi degli host invece degli indirizzi.
 * Per il confronto delle prestazioni con la versione multithread, viene scritto
 * su un file una stringa contenente data, ora e durata dell'esecuzione fatta; a
 * questo scopo è consigliato passare per argomento sempre lo stesso path, così
 * da scrivere il log dei tempi nel medesimo file.
 */

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.time.Instant;
import java.time.Duration;
import java.util.List;
import java.util.LinkedList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.InetAddress;

public class MainClassMultiThread
{
    public static void main(String... args)
    {
        /*
        /*
         * Dichiaro i file di log:
         *   logFileIn    -> logfile in input passato per argomento con le righe da leggere
         *   logFileOut   -> logfile in output dove scrivere con le righe modificate
         *   logFileTempi -> logfile dei tempi dove sono archiviati i dati sulle varie esecuzioni
         */
        String logFileIn = args[0];
        String logFileOut = "newWebLog.txt";
        String logFileTempi = "logTempi.txt";

        /*
         * Salvo l'Instant di inizio, poi alloco un FileReader ed un FileWriter
         * usando le directory passate per argomento che mi serviranno per leggere
         * il log e scrivere quello nuovo.
         */
        Instant start = Instant.now();
        FileReader fRead = Functions.apply(FileReader::new, logFileIn);
        FileWriter fWrite = Functions.apply(FileWriter::new, logFileOut);

        /*
         * Alloco un ExecutorService che fornirà i thread sui quali correranno i
         * Runnable atti, ciascuno, a leggere una riga del file; un BufferedReader
         * con cui bufferizzo il FileReader creato in precedenza; un SingletonOperator
         * contenente una List<String> che archivia le linee modificate dai Runnable.
         *
         * SingletonOperator serve a sincronizzare thread lettori e main: per
         * ciascuna linea presente nel logfile, l'ExecutorService lancia un Runnable
         * che, dalla linea, estrapola l'indirizzo, ne ottiene il nome, rimpiazza
         * l'indirizzo con il nome trovato ed invoca la "flick()" di SingletonOperator
         * dove viene scritta la nuova linea nel nuovo logfile ed aggiornata la
         * lista interna al SingletonOperator aggiungendo la suddetta linea.
         * Inoltre, lo Stream usato per scorrere le linee del file, viene mappato
         * in un Stream<Integer> per poi sommarne gli elementi così da ottenere
         * il numero totale di linee da usare poi in "check()" di SingletonOperator.
         */
        ExecutorService executor = Executors.newCachedThreadPool();
        BufferedReader bufferIn = Functions.apply(BufferedReader::new, fRead);
        SingletonOperator<List<String>> singleton = SingletonOperator.get(new LinkedList<>());

        int lineCount = bufferIn.lines().mapToInt(line -> {
            executor.execute(() -> {
                String address = line.substring(0, line.indexOf(' '));
                String name = Functions.apply(InetAddress::getByName, address).getHostName();
                String newLine = line.replaceFirst(address, name);
                singleton.flick(lineList -> {
                    Functions.accept(fWrite::write, newLine+"\n");
                    lineList.add(newLine);
                    return lineList;
                });
            });
            return 1;
        }).sum();

        /*
         * Invoco la "shutdown()", mi metto in attesa usando la "check()" di
         * SingletonOperator e poi invoco la "flick()" per stampare a schermo la
         * lista completa delle linee modificate dai Runnable.
         */
        executor.shutdown();
        singleton.check(lineList -> lineList.size()!=lineCount);
        singleton.flick(lineList -> {
            System.out.println(String.join("\n", lineList));
            return null;
        });

        /*
         * Chiudo i FileReader e FileWriter, salvo l'istante di fine e mi calcolo
         * la Duration che intercorre tra inizio e fine. Posso stampare a schermo
         * il tempo impiegato.
         */
        Functions.run(fRead::close);
        Functions.run(fWrite::close);
        Instant stop = Instant.now();
        Duration durata = Duration.between(start, stop);

        System.out.println(
                "\033[1;39mTempo totale esecuzione: "+
                "\033[1;31m"+durata+"\033[0m"
        );

        /*
         * Alloco un FileWriter in append dove scrivo una linea contenente data,
         * ora e durata dell'esecuzione appena conclusa, poi chiudo il FileWriter.
         */
        FileWriter fTempi = Functions.apply(FileWriter::new, logFileTempi, true);
        Functions.accept(
                fTempi::write,
                "Esecuzione multithread di "+
                Calendar.getInstance().getTime().toString()+
                ": "+durata.toString()+"\n"
        );
        Functions.run(fTempi::close);
    }
}
