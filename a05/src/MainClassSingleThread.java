/*
 * Classe contenente il main.
 * Qui vengono aperti FileReader e FileWriter per leggere il logfile e scriverne
 * uno nuovo contenenti i nomi degli host invece degli indirizzi.
 * Per il confronto delle prestazioni con la versione singlethread, viene scritto
 * su un file una stringa contenente data, ora e durata dell'esecuzione fatta; a
 * questo scopo è consigliato passare per argomento sempre lo stesso path, così
 * da scrivere il log dei tempi nel medesimo file.
 */

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.time.Instant;
import java.time.Duration;
import java.util.Calendar;
import java.net.InetAddress;

public class MainClassSingleThread
{
    public static void main(String... args)
    {
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
         * Alloco un BufferedReader con cui bufferizzo il FileReader creato in
         * precedenza. Per ciascuna linea presente nel logfile, estrapolo l'indirizzo,
         * ne ottengo il nome, rimpiazzo l'indirizzo con il nome trovato e scrivo
         * la nuova linea nel nuovo logfile (oltre a stamparla a schermo).
         */
        BufferedReader bufferIn = Functions.apply(BufferedReader::new, fRead);
        bufferIn.lines().forEach(line -> {
            String address = line.substring(0, line.indexOf(' '));
            String name = Functions.apply(InetAddress::getByName, address).getHostName();
            String newLine = line.replaceFirst(address, name);
            Functions.accept(fWrite::write, newLine+"\n");
            System.out.println(newLine);
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
                "Esecuzione singlethread di "+
                Calendar.getInstance().getTime().toString()+
                ": "+durata.toString()+"\n"
        );
        Functions.run(fTempi::close);
    }
}
