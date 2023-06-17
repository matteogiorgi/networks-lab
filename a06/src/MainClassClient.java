import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MainClassClient
{
    static final int PORT = 8080;

    public static void main(String[] args)
    {
        // Scanner tastiera = null;
        Scanner tastiera = new Scanner(System.in, "UTF-8");
        Scanner input = null;
        PrintWriter output = null;

        for (boolean newGame=true; newGame;) {
            try (Socket socket = new Socket("127.0.0.1", PORT)) {
                input = new Scanner(socket.getInputStream());
                output = new PrintWriter(socket.getOutputStream(), true);

                String statoMostro = input.nextLine();
                String statoSalute = input.nextLine();
                String statoPozione = input.nextLine();
                System.out.println();
                System.out.println(statoMostro);
                System.out.println(statoSalute);
                System.out.println(statoPozione);

                for (boolean inPartita=true;inPartita;) {
                    System.out.println("1 -> COMBATTI IL MOSTRO\n2 -> BEVI LA POZIONE\n3 -> ESCI DAL GIOCO");
                    System.out.print("Prossima mossa (1-3): ");
                    String cmd = tastiera.nextLine();
                    System.out.println();
                    if (cmd.equals("3")) {
                        inPartita = false;
                    }
                    if (cmd.equals("1") || cmd.equals("2")) {
                        output.println(cmd);
                        statoMostro = input.nextLine();
                        statoSalute = input.nextLine();
                        statoPozione = input.nextLine();
                        System.out.println(statoMostro);
                        System.out.println(statoSalute);
                        System.out.println(statoPozione);
                        if (statoSalute.equals("Stato salute: 0") && statoMostro.equals("Stato mostro: 0")) {
                            System.out.print("Partita pareggiata, iniziare nuova partita? (yes/no): ");
                            newGame = tastiera.nextLine().equals("yes") ? true : false;
                            inPartita = false;
                        } else if (statoSalute.equals("Stato salute: 0")) {
                            System.out.println("Partita persa, ciao!");
                            inPartita = false;
                            newGame = false;
                        } else if (statoMostro.equals("Stato mostro: 0")) {
                            System.out.print("Partita vinta, iniziare nuova partita? (yes/no): ");
                            newGame = tastiera.nextLine().equals("yes") ? true : false;
                            inPartita = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                input.close();
                output.close();
            }
        }
        tastiera.close();
    }
}
