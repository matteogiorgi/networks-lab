/*
 * Runnable Lettore il cui compito è quello, dato un ContoCorrente, di leggerne
 * i movimenti presenti, contarne il tipo e aggiornare un contatore globale.
 *
 * Oltre al campo "conto" di tipo ContoCorrente, contiene:
     - "contatoreGlobale" -> ConcurrentMap<String,Integer> da aggiornare a lettura ultimata
     - "contatoreLocale"  -> Map<String,Integer> nel quale tiene traccia dei movimenti letti
 *   - "singleton"        -> SingletonOperator<Optional<Integer>> per la sincronizzazione con il Main
 */


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;


public class Lettore implements Runnable
{
    private final ContoCorrente conto;
    private final SingletonOperator<Optional<Integer>> singleton = SingletonOperator.get();
    private final BiConsumer<String,Integer> aggiornaContatoreGlobale;
    private Map<String,Integer> contatoreLocale = new HashMap<>();

    public Lettore(ContoCorrente conto, BiConsumer<String,Integer> aggiornaContatoreGlobale)
    {
        this.conto = conto;
        this.aggiornaContatoreGlobale = aggiornaContatoreGlobale;
    }

    /*
     * Legge la lista movimenti del conto corrente passatogli archiviando i risultati in "contatoreLocale",
     * aggiorna "contatoreGlobale" (condiviso con gli altri thread) e invoca la "flick" di "singleton"
     * decrementandone la variabile interna.
     */
    @Override
    public void run()
    {
        conto.getRecords().forEach(m -> contatoreLocale.merge(m.getReason(), 1, Integer::sum));
        contatoreLocale.forEach((s,i) -> aggiornaContatoreGlobale.accept(s, i));
        singleton.flick(o -> o.ifPresent(i -> --i));
    }
}
