/*
 * Classe singleton Thread-safe contenente un oggetto "titty" di tipo generico T
 * inaccessibile dall'esterno se non con i metodi pubblici sincronizzati "check()"
 * e "flick()" che richiedono un'interfaccia funzionale per essere eseguiti.
 *
 * Questa classe, implementata in modo completamente generico, può essere usata in
 * diversi contesti; qui ha il compito di contatore sincronizzato sul quale possono
 * mettersi eventualmente in attesa i Thread che la utilizzano.
 */


import java.util.function.Predicate;
import java.util.function.UnaryOperator;


public final class SingletonOperator<T>
{
    private T titty;
    private SingletonOperator(T t)
    {
        this.titty = t;
    }
    private static SingletonOperator<?> singleton = null;

    /*
     * Metodo statico sincronizzato che restituisce il singleton
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> SingletonOperator<T> get()
    {
        return (SingletonOperator<T>) singleton;
    }

    /*
     * Metodo statico sincronizzato che restituisce il singleton
     * aggiornandone il valore nel caso sia nullo
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> SingletonOperator<T> get(T t)
    {
        if (singleton==null)
            singleton = new SingletonOperator<>(t);
        return (SingletonOperator<T>) singleton;
    }

    /*
     * Metodo statico sincronizzato che usa Predicate<T> passatogli per testare
     * "titty" ed eventualmente mettere in attesa su questo SingletonOperator il
     * Thread chiamante, il cui Runnable ha invocato il metodo.
     */
    public synchronized void check(Predicate<T> pred)
    {
        while(pred.test(titty))
            Functions.run(this::wait);
    }

    /*
     * Metodo statico sincronizzato che usa UnaryOperator<T> per modificare il
     * valore di titty, riassegnarlo e notificare ai Thread in attesa su questo
     * SingletonOperator l'avvenuta modifica.
     */
    public synchronized void flick(UnaryOperator<T> fun)
    {
        titty = fun.apply(titty);
        notifyAll();
    }
}
