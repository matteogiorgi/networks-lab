import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Partita implements Runnable
{
    private Socket connection;
    static final int MAXVAL = 100;

    public Partita(Socket connection)
    {
        this.connection = connection;
    }

    @Override
    public void run()
    {
        Random rand = new Random();
        int mostro = rand.nextInt(MAXVAL)+1;
        int salute = rand.nextInt(MAXVAL)+1;
        int pozione = rand.nextInt(MAXVAL)+1;

        System.out.println("CONNESSO: "+connection);
        try (Scanner input = new Scanner(connection.getInputStream());
             PrintWriter output = new PrintWriter(connection.getOutputStream(), true)) {
            output.println("Stato mostro: "+mostro);
            output.println("Stato salute: "+salute);
            output.println("Stato pozione: "+pozione);
            while (input.hasNextLine()) {
                String cmd = input.nextLine();
                switch (cmd) {
                    case "1":  // COMBATTI IL MOSTRO
                        mostro -= rand.nextInt(mostro)+1;
                        salute -= rand.nextInt(salute)+1;
                        break;
                    case "2":  // BEVI LA POZIONE
                        int rigenera = pozione>0 ? rand.nextInt(pozione)+1 : 0;
                        pozione -= rigenera;
                        salute += rigenera;
                        break;
                    default:
                        break;
                }
                output.println("Stato mostro: "+mostro);
                output.println("Stato salute: "+salute);
                output.println("Stato pozione: "+pozione);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
