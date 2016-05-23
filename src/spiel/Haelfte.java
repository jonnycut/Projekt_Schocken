package spiel;

/**
 * Created by KNapret on 23.05.2016.
 */
public class Haelfte {

    private Stock stock;
    private Runde runde;
    private Spieler verlierer;
    private String art;

    public Haelfte(){
        //ToDo: Art der Häfte wird wo festgelegt?

        this.stock = new Stock();
        this.runde = new Runde(this.stock);
        this.verlierer =null;
        this.art = null;

    }

    public String getArt(){
        return this.art;
    }

    public Runde getRunde(){
        return this.runde;
    }

    public void resetStock(){
        this.stock = new Stock();
    }


}
