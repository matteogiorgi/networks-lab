import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.function.Function;


public class MainClass
{
    // Metodo statico che usa due FileChannels per la copia del file il cui nome è passato per argomento (input).
    // Il metodo prevede sia passata per argomento anche la funzione da usare per allocare il ByteBuffer così
    // da scegliere se usare un buffer indiretto (ByteBuffer::allocate) o uno diretto (ByteBuffer::allocateDirect).
    private static int strategiaBuffer(String input, String output, Function<Integer,ByteBuffer> allocaFun, int buffLen)
    {
        Instant start = Instant.now();
        try (FileChannel iChannel = new FileInputStream(input).getChannel();
             FileChannel oChannel = new FileOutputStream(output).getChannel();) {
            ByteBuffer bb = allocaFun.apply(buffLen);
            while (iChannel.read(bb) != -1) {
                bb.flip();
                while (bb.hasRemaining()) oChannel.write(bb);
                bb.compact();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Instant stop = Instant.now();
        return Duration.between(start, stop).getNano();
    }


    // Metodo statico che usa due FileChannels e il metodo transferTo()
    // per la copia del file il cui nome è passato per argomento (input).
    private static int strategiaTransfer(String input, String output)
    {
        Instant start = Instant.now();
        try (FileChannel iChannel = new RandomAccessFile(input, "rw").getChannel();
             FileChannel oChannel = new RandomAccessFile(output, "rw").getChannel()) {
            oChannel.transferFrom(iChannel, 0, iChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Instant stop = Instant.now();
        return Duration.between(start, stop).getNano();
    }


    // Metodo statico che usa due Stream bufferizzati per la
    // copia del file il cui nome è passato per argomento (input).
    public static int strategiaBufferedStream(String input, String output)
    {
        Instant start = Instant.now();
        try (BufferedInputStream iBuffer = new BufferedInputStream(new FileInputStream(input));
             BufferedOutputStream oBuffer = new BufferedOutputStream(new FileOutputStream(output))) {
            for (int byteLetti = 0; (byteLetti=iBuffer.read()) != -1;) {
                oBuffer.write(byteLetti);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Instant stop = Instant.now();
        return Duration.between(start, stop).getNano();
    }


    // Metodo statico che usa due Stream e un byte-arry di supporto
    // per la copia del file il cui nome è passato per argomento (input).
    public static int strategiaByteArray(String input, String output, int buffLen)
    {
        Instant start = Instant.now();
        try (FileInputStream iFile = new FileInputStream(input);
             FileOutputStream oFile = new FileOutputStream(output)) {
            byte[] buffer = new byte[buffLen];
            while (iFile.read(buffer) != -1) {
                oFile.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Instant stop = Instant.now();
        return Duration.between(start, stop).getNano();
    }


    // Metodo usato per l'esecuzione e la stampa a schermo dei tempi
    // impiegati dai 5 metodi di copia su tutti i file passati per argomento.
    private static void printTest(String[] args, int buffLen) {
        for (String input : args) {
            ArrayList<CopiaFile> arr = new ArrayList<>(5);
            arr.add(new CopiaFile("    FileChannel con buffer indiretti", strategiaBuffer(input, "out1", ByteBuffer::allocate, buffLen)));
            arr.add(new CopiaFile("    FileChannel con buffer diretti", strategiaBuffer(input, "out2", ByteBuffer::allocateDirect, buffLen)));
            arr.add(new CopiaFile("    FileChannel con tranferTo()", strategiaTransfer(input, "out3")));
            arr.add(new CopiaFile("    Buffered Stream I/O", strategiaBufferedStream(input, "out4")));
            arr.add(new CopiaFile("    byte-array", strategiaByteArray(input, "out5", buffLen)));
            arr.sort((a,b) -> a.temp()<b.temp() ? -1 : 1);
            System.out.println();
            System.out.println("TEST FILE: "+input);
            System.out.println("BUFFER LEN: "+buffLen);
            arr.forEach(e -> System.out.println(e.toString()));
        }
    }


    public static void main(String... args)
    {
        printTest(args, 2*1024);
        printTest(args, 4*1024);
        printTest(args, 8*1024);
        printTest(args, 16*1024);
        System.out.println();

        /*
         * Dai test si evince che il metodo con i Buffered Stream è costantemente il più lento a prescindere dalla grandezza
         * del file e/o del buffer di copia mentre il metodo che usa transferTo() è quasi sempre uno dei due più veloci.
         * FileChannel con buffer diretti e byte-array dimostrano di essere mediamente veloci ma non si evince un pattern
         * che indichi un cambiamento di velocià al variare della grandezza del file e/o del buffer di copia.
         * Per File di "grandi" dimensioni, usando un buffer di copia maggiore, sembra che i FileChannel con buffer indiretti,
         * si comportino meglio di quelli diretti.
         * 
         * In conclusione, i metodi usati possono essere classificati come segue (mediamente):
         *   1. FileChannel con transferTo()
         *   2. FileChannel con buffer diretti
         *   3. byte-array
         *   4. FileChannel con buffer indiretti
         *   5. Buffered Stream I/O
         */
    }
}
