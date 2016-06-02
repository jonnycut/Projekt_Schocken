package spiel;

import Datenbank.Datenbank;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by KNapret on 23.05.2016.
 */
public class Haelfte {

    private Stock stock;
    private Runde runde;
    private Spieler verlierer;
    private int art;

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
        this.art = 0;

    }

    /**
     * Liefert die Art der aktuellen Runde
     * @return String {erste | zweite | finale}
     */
    public int getArt(){
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

    public Stock getStock() {
        return stock;
    }

    //ToDo: Kommentieren KNA

    public void setVerlierer(Spieler verlierer) {
        this.verlierer = verlierer;
    }

    public void setArt(int art) {
        this.art = art;
    }


    public void updateHaelfte(int spielID) {
        try {
            this.art = Datenbank.getInstance().selectAktuelleHaelfte(spielID);
            String verliererName = Datenbank.getInstance().selectHaelftenVerlierer(spielID,this.art);
            this.verlierer = new Spieler(verliererName,Datenbank.getInstance().selectProfilBild(verliererName));
            this.stock.updateStock(spielID);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
