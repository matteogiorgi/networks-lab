import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Optional;
import java.util.Random;


public class MainClassServer
{
    /*
     * Variabili statiche di lunghezza del DatagramPacket in scrittura e tempo
     * in millisec della simulazione perdita pacchetto.
     */
    private static int LENGTH = 1024;
    private static int TIMEOUT = 1001;

    public static void main(String... args) throws InterruptedException, IOException
    {
        /*
         * Recupero argomento del "main()": intero identificante la porta dove è
         * registrato il DatagramSocket del server;
         */
        int PORT_SERVER = 0000;
        long SEED = 0;
        try {
            PORT_SERVER = args.length>0
                        ? Integer.parseInt(args[0])
                        : (Integer)Optional.empty().orElseThrow(() -> new IllegalArgumentException("ERR -arg 0"));
            SEED = args.length>1
                        ? Long.parseLong(args[1])
                        : 1234567890;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(1);
        }

        /*
         * Alloco una DatagramSocket ed un DatagramPacket per registrare i messaggi
         * in arrivo; registro un Thread di shutdownHook che chiuderà la DatagramSocket
         * all'arrivo del SIGKILL.
         */
        DatagramSocket socket = new DatagramSocket(PORT_SERVER);
        DatagramPacket pkIn = new DatagramPacket(new byte[LENGTH], LENGTH);
        Runtime.getRuntime().addShutdownHook(new Thread(socket::close));
        Random rand = new Random(SEED);
        System.out.println("STARTING SERVER");

        /*
         * Ciclo infinito dove eseguo una "receive()" bloccante sulla DatagramSocket,
         * simulo un ritardo (generato casualmente) e spedisco un nuovo pacchetto
         * indietro verso il client contenente il medesimo byte[] che mi era arrivato.
         */
        for (int n=0;; n++) {
            try {
                socket.receive(pkIn);
            } catch (IOException e) {
                System.out.println("\nCLOSING SERVER");
            }

            /*
             * Generando un numero casale n (con 0<=n<=3), simulo la perdita del
             * 25% dei pacchetti e torno a bloccarmi sulla "receive()".
             */
            if (rand.nextInt(4)==0) {
                Thread.sleep(TIMEOUT);
                System.out.printf(
                        "%s:%s> PING %d %-20d ACTION: not sent\n",
                        pkIn.getAddress(), pkIn.getPort(),
                        n, Convert.toLong(pkIn.getData())
                );
                continue;
            }

            int delay = rand.nextInt(100);
            Thread.sleep((long)delay);
            System.out.printf(
                    "%s:%s> PING %d %-20d ACTION: delayed %d ms\n",
                    pkIn.getAddress(), pkIn.getPort(),
                    n, Convert.toLong(pkIn.getData()), delay
            );

            DatagramPacket pkOut = new DatagramPacket(
                    pkIn.getData(), 0, pkIn.getLength(),
                    InetAddress.getLocalHost(), pkIn.getPort());
            socket.send(pkOut);
        }
    }
}
