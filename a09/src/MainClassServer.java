import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;

public class MainClassServer
{
    private static int PORTA = 5555;
    private static char TERMINATORE = ' ';
    private static String RESPONSE = "ECHOED BY SERVER: ";
    private static int LUNGHEZZA = 999;
    private static StringBuilder BUILDER = new StringBuilder();

    public static void main(String... args) throws IOException
    {

        /*
         * Apro una ServerSocketChannel non bloccante sulla porta 5555 ed un Selector
         * dove registro la ServerSocketChannel in OP_ACCEPT
         */
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(PORTA));
        serverChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("ServerChannel registrato sulla porta 5555");

        /*
         * Ciclo infinito dove eseguo una "select()" e scorro sulla lista delle
         * SelectionKey selezionate dal selettore, distinguendo i casi OP_ACCEPT,
         * OP_READ, OP_WRITE.
         */
        for (char x=' '; !selector.keys().isEmpty() && selector.select()>=0; selector.selectedKeys().clear()) {
            for (SelectionKey key : selector.selectedKeys()) {

                /*
                 * OP_ACCEPT: recupero il server e creo la SocketChannel con una
                 * "accept()", la configuro non bloccante e la registro sul
                 * selettore in OP_READ con un ByteBuffer in attachment.
                 */
                if (key.isValid() && key.isAcceptable()) {
                    System.out.println("fase di accept");
                    ServerSocketChannel server = (ServerSocketChannel)key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(LUNGHEZZA));
                    continue;
                }

                /*
                 * OP_READ: recupero il client ed il buffer in attachment e leggo
                 * dal client. Quando ho finito di leggere dal client, leggo dal
                 * buffer e costruisco uno StringBuilder contenente il nome del
                 * file; apro il file con un FileChannel e lo mappo su un MappedByteBuffer;
                 * registro il cliente sul selettore in OP_WRITE con il MappedByteBuffer
                 * in attachment.
                 */
                if (key.isValid() && key.isReadable()) {
                    System.out.println("fase di read");
                    SocketChannel client = (SocketChannel)key.channel();
                    ByteBuffer buffer = (ByteBuffer)((Buffer)key.attachment()).clear();
                    if (TERMINATORE==' ') {
                        ByteBuffer bufferTerminatore = ByteBuffer.allocate(1);
                        if (client.read(bufferTerminatore)>0) {
                            TERMINATORE = (char)((ByteBuffer)bufferTerminatore.flip()).get();
                        }
                        continue;
                    }
                    if (client.read(buffer)>=0) {
                        for (buffer.flip(); buffer.hasRemaining() && (x=(char)buffer.get())!=TERMINATORE; BUILDER.append(x));
                        if (x!=TERMINATORE) continue; 
                    }
                    BUILDER.append(TERMINATORE);

                    /*
                     * Leggo il FileChannel aperto e scrivo dentro dei ByteBuffer
                     * da appendere alla lista BYTELETTI per poi usarla come attachment
                     * del canale in OP_WRITE.
                     */
                    key.interestOps(SelectionKey.OP_WRITE);
                    buffer.clear();
                    key.attach(ByteBuffer.wrap((RESPONSE+BUILDER.toString()).getBytes()));
                    BUILDER = new StringBuilder();
                    continue;
                }

                /*
                 * OP_WRITE: recupero il client ed il buffer in attachment e scrivo
                 * il buffer sul client.
                 */
                if (key.isValid() && key.isWritable()) {
                    System.out.println("fase di write");
                    SocketChannel client = (SocketChannel)key.channel();
                    ByteBuffer attachBuffer = (ByteBuffer)key.attachment();
                    try {
                        if (client.write(attachBuffer)>=0 && !attachBuffer.hasRemaining()) {
                            key.interestOps(SelectionKey.OP_READ);
                            key.attach(ByteBuffer.allocate(LUNGHEZZA));
                        }
                    } catch (IOException e) {
                        System.exit(0);
                    }
                    continue;
                }
            }
        }
    }
}
