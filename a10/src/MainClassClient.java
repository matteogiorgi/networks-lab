import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;


public class MainClassClient
{
    /*
     * Variabili statiche di lunghezza del DatagramPacket in scrittura e tempo
     * in millisec di timeout della "receive()"
     */
    private static int LENGTH = 1024;
    private static int TIMEOUT = 1000;

    public static void main(String... args) throws IOException
    {
        /*
         * Recupero argomenti del "main()": interi identificanti le porte dove
         * sono registrati (nell'ordine) i DatagramSocket di client e server.
         */
        String NAME_SERVER = "";
        int PORT_SERVER = 0000;
        try {
            NAME_SERVER = args.length>0
                        ? args[0]
                        : (String)Optional.empty().orElseThrow(() -> new IllegalArgumentException("ERR -arg 0"));
            PORT_SERVER = args.length>1
                        ? Integer.parseInt(args[1])
                        : (Integer)Optional.empty().orElseThrow(() -> new IllegalArgumentException("ERR -arg 1"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(1);
        }

        /*
         * Alloco una DatagramSocket con timeout ed un DatagramPacket per registrare
         * i messaggi in arrivo; registro un Thread di shutdownHook che chiuderà la
         * DatagramSocket all'arrivo del SIGKILL.
         */
        DatagramSocket socket = new DatagramSocket(8888);
        DatagramPacket pkIn = new DatagramPacket(new byte[LENGTH], LENGTH);
        Runtime.getRuntime().addShutdownHook(new Thread(socket::close));
        socket.setSoTimeout(TIMEOUT);
        Random rand = new Random();

        /*
         * Alloco le variabili di PING ricevuti, tempo minimo e massimo di RTT e
         * tempo totale dei RTT, utili per le statistiche.
         */
        int ricevuti = 0;
        long min = 999;
        long max = 0;
        float sum = 0;

        /*
         * Ciclo di 10 iterazioni dove genero casualmente un long, lo converto in
         * un byte[] shiftando ottetti di bit e lo spedisco sulla DatagramSocket;
         * poi attendo con una "receive()" bloccante il messaggio di risposta dal
         * server, stampo un rigo di resoconto e aggiormo le statistiche.
         */
        for (int n=0; n<10; n++) {
            long msg = Math.abs(rand.nextLong());
            DatagramPacket pkOut = new DatagramPacket(
                    Convert.toByte(msg), 0, Long.BYTES,
                    InetAddress.getByName(NAME_SERVER), PORT_SERVER);
            Instant start = Instant.now();
            socket.send(pkOut);

            /*
             * "recive()" bloccante: nel caso scatti il timeout stampo la riga
             * di resoconto e torno a generare un nuovo long.
             */
            try {
                socket.receive(pkIn);
            } catch(SocketTimeoutException e) {
                System.out.printf("PING %d %-20d RTT: *\n", n, Convert.toLong(pkOut.getData()));
                continue;
            }

            Instant stop = Instant.now();
            long duration = Duration.between(start, stop).toMillis();
            System.out.printf("PING %d %-20d RTT: %d ms\n", n, Convert.toLong(pkIn.getData()), duration);
            ricevuti++;
            min = duration<min ? duration : min;
            max = duration>max ? duration : max;
            sum += duration;
        }

        /*
         * Stampo le statistiche di PING inviati, PING ricevuti, RTT minimo,
         * RTT medi e RTT massimo.
         */
        System.out.printf(
                "\n---- PING Statistics ----\n"+
                "10 pachetti trasmessi, %d ricevuti, %d%% persi\n"+
                "round-trip(ms) min/avg/max = %d/%.2f/%d\n",
                ricevuti, (10-ricevuti)*10, min, sum/ricevuti, max
        );
    }
}
