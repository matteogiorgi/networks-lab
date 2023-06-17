/*
 * Classe contenente il Main per il test: qui viene estratta la
 * lista di directory dove creare i gz, lanciato il Produttore
 * (che funge pure da coda sincronizzata) ed eseguita la shutdown().
 */

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainClass
{
    public static void main(String[] args)
    {
        List<String> listPath = Arrays.asList(args);
        ExecutorService exe = Executors.newSingleThreadExecutor();
        exe.execute(new Produttore(listPath, 5));

        exe.shutdown();
        try { exe.awaitTermination(1L, TimeUnit.MILLISECONDS); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }
}