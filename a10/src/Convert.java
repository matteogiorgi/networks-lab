/*
 * Classe per la conversione di numeri in byte[] e viceversa. Sono presenti
 * solamente i metodi necessari per questo assegnamento: long->byte[] e byte[]->long.
 */
public class Convert
{
    /*
     * toByte shifta a destr ciascun ottetto del long passato per argomento per
     * poi castarlo a byte ed inserirlo in un nuovo byte[].
     */
    public static byte[] toByte(long lng)
    {
        return new byte[] {
                (byte) (lng >> 56),
                (byte) (lng >> 48),
                (byte) (lng >> 40),
                (byte) (lng >> 32),
                (byte) (lng >> 24),
                (byte) (lng >> 16),
                (byte) (lng >> 8),
                (byte) lng
        };
    }

    /*
     * toLong riposiziona correttamente ciasun ottetto (byte) per poi ottenere il
     * long originario mettendo in or bit a bit i risultati parziali.
     */
    public static long toLong(byte[] b)
    {
        return ((long) b[0] << 56)
             | ((long) b[1] & 0xff) << 48
             | ((long) b[2] & 0xff) << 40
             | ((long) b[3] & 0xff) << 32
             | ((long) b[4] & 0xff) << 24
             | ((long) b[5] & 0xff) << 16
             | ((long) b[6] & 0xff) << 8
             | ((long) b[7] & 0xff);
    }
}
