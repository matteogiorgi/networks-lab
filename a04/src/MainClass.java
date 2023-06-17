import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// questa classe serve solamente come setter iniziale
// del programma poi termina e lascia fare tutto al Gestore
public class MainClass
{
    public static void main(String[] args) throws InterruptedException
    {
        ExecutorService exe = Executors.newSingleThreadExecutor();
        exe.execute(new Gestore(Arrays.asList(args), 3));

        exe.shutdown();
        exe.awaitTermination(1L, TimeUnit.MILLISECONDS);
    }
}
