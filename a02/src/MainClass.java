/*
 * Questa classe serve solamente a lanciare il therad
 * 'direttoreUfficioPostale' che si occuperà di:
 *      1. aprire l'ufficioo postale
 *      2. lanciare clienti
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainClass
{
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("UFFICIO APERTO!");
        System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
        ExecutorService direttoreUfficioPostale = Executors.newSingleThreadExecutor();
        direttoreUfficioPostale.execute(new Ufficio(4, 10, 10));
        direttoreUfficioPostale.shutdown();
        direttoreUfficioPostale.awaitTermination(10L, TimeUnit.SECONDS);
    }
}