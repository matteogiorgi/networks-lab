import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class MainClassClient
{
    private static int PORTA = 5555;
    private static char TERMINATORE = ' ';
    private static String STRINGTERMINATORE = "";
    private static StringBuilder BUILDER = new StringBuilder();

    public static  void main(String... args) throws IOException
    {
        try (Scanner input = new Scanner(System.in)) {
            SocketChannel client = SocketChannel.open(new InetSocketAddress(PORTA));

            System.out.print("Immettere carattere terminatore: ");
            STRINGTERMINATORE = input.nextLine();
            TERMINATORE = STRINGTERMINATORE.charAt(0);

            System.out.print("Immettere messaggio: ");
            String messagio = STRINGTERMINATORE+input.nextLine()+STRINGTERMINATORE;

            for (char x=' ';; BUILDER=new StringBuilder(), messagio=input.nextLine()+STRINGTERMINATORE, x=' ') {

                /*
                 * Apro una SocketChannel sulla porta 5555, alloco un ByteBuffer con il
                 * nome del file e scrivo il buffer nel client.
                 */
                ByteBuffer buffer = ByteBuffer.wrap(messagio.getBytes());
                while (client.write(buffer)>=0 && buffer.hasRemaining());

                /*
                 * Leggo dal client e scrivo nel buffer poi, con il contenuto del buffer,
                 * costruisco uno StringBuilder contenente il testo del file letto dal server
                 */
                for (buffer.clear(); x!=TERMINATORE && client.read(buffer)>=0; buffer.flip()) {
                    for (buffer.flip(); buffer.hasRemaining() && (x=(char)buffer.get())!=TERMINATORE; BUILDER.append(x));
                }
                System.out.print(BUILDER.toString()+"\nImmettere messaggio: ");
            }
        }
    }
}
