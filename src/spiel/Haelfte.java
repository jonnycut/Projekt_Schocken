package spiel;

/**
 * Created by KNapret on 23.05.2016.
 */
public class Haelfte {

    private Stock stock;
    private Runde runde;
    private Spieler verlierer;
    private String art;

    /**
     * <pre>
     * Erstellt eine neue Haelfte
     *
     * Erstellt einen neuen Stock
     * Erstellt eine neue Runde, die den Stock und den letzten Verlierer bekommt.
     * Danach wird der aktuelle Verlierer auf NULL gesetzt
     *
     * </pre>
     */
    public Haelfte(){
        //ToDo: Art der Häfte wird wo festgelegt?

        this.stock = new Stock();
        this.runde = new Runde(this.stock,this.verlierer);
        this.verlierer =null;
        this.art = null;

    }

    /**
     * Liefert die Art der aktuellen Runde
     * @return String {erste | zweite | finale}
     */
    public String getArt(){
        return this.art;
    }

    /**
     * Liefert die aktuelle Runde der Haelfte
     * @return Runde: Object
     */
    public Runde getRunde(){
        return this.runde;
    }

    /**
     * Resettet den Stock, damit wieder Chips vorhanden sind.
     */
    public void resetStock(){
        this.stock = new Stock();
    }


}
