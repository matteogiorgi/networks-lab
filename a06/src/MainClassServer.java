import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainClassServer
{
    static final int PORT = 8080;

    public static void main(String[] args)
    {
        try (ServerSocket socket = new ServerSocket(PORT)) {
            ExecutorService pool = Executors.newFixedThreadPool(5);
            System.out.println("SERVER ACCESO");
            while (true) {
                pool.execute(new Partita(socket.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
