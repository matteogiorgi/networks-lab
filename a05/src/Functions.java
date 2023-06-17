/*
 * File contenente interfaccie funzionali rappresentanti funzioni e operazioni che
 * lanciano Eccezioni, oltre alla classe pubblica FunWrap che possiede metodi
 * statici necessari a catturare e gestire le eccezioni lanciate dai metodi delle
 * interfaccie.
 *
 * Questa classe è stata pensata per gestire le eccezione lanciate dalle invocazioni
 * dei lambda dal momento che nessuna delle interfaccie funzionali in java.util.function
 * è pensata per lanciare eccezioni; è molto utile anche per organizzare in modo
 * definitivo la gestione di un certo tipo di eccezione.
 * La classe FunWrap può ovviamente essere estesa e, sovrascrivendo i metodi, è
 * possibile cambiare a piacimento la gestione dell'eccezione.
 *
 * L'implementazione migliore sarebbe stata quella di usare file separati per ciascuna
 * interfaccia ed un package per tutto il codice sottostante ma per lo scopo di questo
 * assegnamento, ho ritenuto più pratico un unico file per tutto il codice.
 */


import java.io.IOException;
import java.text.ParseException;


@FunctionalInterface
interface Suppl<T>
{
    T get() throws Exception;
}


@FunctionalInterface
interface Cons<T>
{
    void accept(T argT) throws Exception;
}


@FunctionalInterface
interface BiCons<T,V>
{
    void accept(T argT, V argV) throws Exception;
}


@FunctionalInterface
interface VoidFun
{
    void run() throws Exception;
}


@FunctionalInterface
interface Fun<T,R>
{
    R apply(T argT) throws Exception;
}


@FunctionalInterface
interface BiFun<T,V,R>
{
    R apply(T argT, V argV) throws Exception;
}


@FunctionalInterface
interface TriFun<T,V,U,R>
{
    R apply(T argT, V argV, U argU) throws Exception;
}


public class Functions
{
    public static <T> T get(Suppl<T> suppl)
    {
        T result = null;
        try {
            result = suppl.get();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> void accept(Cons<T> cons, T arg)
    {
        try {
            cons.accept(arg);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T,V> void accept(BiCons<T,V> biCons, T arg1, V arg2)
    {
        try {
            biCons.accept(arg1, arg2);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(VoidFun voidFun)
    {
        try {
            voidFun.run();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T,R> R apply(Fun<T,R> fun, T argT)
    {
        R result = null;
        try {
            result = fun.apply(argT);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T,V,R> R apply(BiFun<T,V,R> biFun, T argT, V argV)
    {
        R result = null;
        try {
            result = biFun.apply(argT, argV);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.exit(1);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T,V,U,R> R apply(TriFun<T,V,U,R> triFun, T argT, V argV, U argU)
    {
        R result = null;
        try {
            result = triFun.apply(argT, argV, argU);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
