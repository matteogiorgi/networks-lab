/*
 * Classe che rappresente il consumatore che legge il contenuto
 * di una singola directory e lo comprime in formato gz.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.zip.GZIPOutputStream;

public class Consumatore implements Runnable
{
    private LinkedList<String> dirList;
    public Consumatore(LinkedList<String> dirList)
    {
        this.dirList = dirList;
    }

    /*
     * Nel run() il Consumatore estrae in modo sincronizzato una
     * directory dalla LinkedList passatagli e ne converte il contenuto.
     * Nota che il Produttore si è già assicurato che i path contennuti
     * in dirList siano directiory.
     */
    @Override
    public void run()
    {
        File f = new File(dirList.pop());
        for (String str : f.list()) {
            String fullName = f.getPath()+"/"+str;
            if (new File(fullName).isFile()) {
                try { compressGzip(Paths.get(fullName), Paths.get(fullName+".gz")); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    /*
     * Funzione privata per la compressiona del singolo file
     */
    private void compressGzip(Path source, Path target) throws IOException
    {
        try (GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(target.toFile()));
             FileInputStream fis = new FileInputStream(source.toFile())) {
            byte[] buffer = new byte[1024]; int len;
            while ((len = fis.read(buffer)) > 0) {
                gos.write(buffer, 0, len);
            }
        }
    }
}