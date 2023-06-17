/*
 * Classe che rappresenta il produttore che legge ricorsivamente tutte le
 * sottodirectory presenti nelle root-directory passategli (in full-path)
 * e le archivia, pronte per essere estratte (in modo sincronizzato)
 * ciascuna da un Consumatore differente.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Produttore extends LinkedList<String> implements Runnable
{
    /*
     * Funzione ricorsiva che legge una root-directory e ricorsivamente
     * tutte le sottodirectory presenti (archiviandole dentro se stesso).
     */
    private void seekDir(String dirPath)
    {
        File f = new File(dirPath);
        if (f.isDirectory()) {
            push(dirPath);
            for (String str : f.list()) {
                seekDir(dirPath+"/"+str);
            }
        }
    }

    private int nThread;
    private int nConsumatori;
    public Produttore(List<String> listPath, int nThread)
    {
        super();
        listPath.forEach(root->seekDir(root));
        this.nConsumatori = this.size();
        this.nThread = nThread;
    }

    /*
     * Wrapper del metodo push della classe LinkedList
     * necessario per l'accesso sincrono alla struttura
     */
    @Override
    public synchronized void push(String t)
    {
        super.push(t);
    }

    /*
     * Wrapper del metodo pop della classe LinkedList
     * necessario per l'accesso sincrono alla struttura
     */
    @Override
    public synchronized String pop()
    {
        return super.pop();
    }

    /*
     * Nel run() il Produttore alloca un esecutore con numero thread
     * limitato, mette in esecuzione tanti Consumatore quanti sono le
     * directory lette precedentemente nel costruttore, dopodichè
     * esegue la shutdown()
     */
    @Override
    public void run()
    {
        ExecutorService exe = Executors.newFixedThreadPool(nThread);
        ArrayList<Consumatore> listConsumatori = new ArrayList<>(nConsumatori);
        for(;nConsumatori-->0; listConsumatori.add(new Consumatore(this)));
        listConsumatori.forEach(exe::execute);

        exe.shutdown();
        try { exe.awaitTermination(1L, TimeUnit.MILLISECONDS); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }
}