/*
 * Classe contenente il main per il test:
 * contiene il laboratorio 
 */

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class MainClass
{
    public static void main(String... args)
    {
        ExecutorService executorUtenti = Executors.newCachedThreadPool();
        Laboratorio lab = new Laboratorio(10);

        /*
         * La lista degli utenti è creata manualmente in modo da mescolare al
         * meglio i tre tipi di utente che accedono al laboratorio.
         */
        List<Utente> listaUtenti =
                Arrays.asList(
                        new Studente("studente0", lab),
                        new Tesista("tesista0", lab, 0),
                        new Studente("studente1", lab),
                        new Studente("studente2", lab),
                        new Tesista("tesista1", lab, 1),
                        new Studente("studente3", lab),
                        new Studente("studente4", lab),
                        new Studente("studente5", lab),
                        new Studente("studente6", lab),
                        new Professore("professore0", lab),
                        new Studente("studente7", lab),
                        new Tesista("tesista2", lab, 2),
                        new Studente("studente8", lab),
                        new Studente("studente9", lab),
                        new Tesista("tesista3", lab, 3),
                        new Studente("studente10", lab),
                        new Studente("studente11", lab),
                        new Professore("professore1", lab),
                        new Studente("studente12", lab),
                        new Studente("studente13", lab),
                        new Studente("studente14", lab),
                        new Tesista("tesista5", lab, 3),
                        new Studente("studente15", lab),
                        new Studente("studente16", lab),
                        new Tesista("tesista6", lab, 4),
                        new Tesista("tesista7", lab, 4),
                        new Studente("studente17", lab),
                        new Studente("studente18", lab),
                        new Studente("studente19", lab),
                        new Studente("studente20", lab)
                );

        /*
         * Stampo lo stato degli utenti e
         * li faccio eseguire dall'esecutore.
         */
        listaUtenti.forEach(Utente::statoUtente);
        System.out.println("\n:::::::::: LABORATORIO APERTO ::::::::::\n");

        listaUtenti.forEach(executorUtenti::execute);

        try {
            executorUtenti.shutdown();
            if (!executorUtenti.awaitTermination(40L, TimeUnit.SECONDS)) {
                System.out.println("\n:::::::::: LABORATORIO IN CHIUSURA ::::::::::\n");
                executorUtenti.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            executorUtenti.shutdownNow();
        }
    }
}
