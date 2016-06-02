package spiel;

import Datenbank.Datenbank;
import gui.GUI;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Klasse der Häfte, dient zu Datenhaltung und Spielsteuerung
 *
 * @author KNapret
 */
public class Haelfte {
    /**
     * Die Gui, zu der diese Hälfte gehört
     *
     * @see GUI
     */
    private GUI gui;
    /**
     * Der Stock der Hälfte
     *
     * @see Stock
     */
    private Stock stock;
    /**
     * Die aktuelle Runde der Hälfte
     *
     * @see Runde
     */
    private Runde runde;
    /**
     * Der Verlierer der Hälfte
     *
     * @see Spieler
     */
    private Spieler verlierer;
    /**
     * Die Art der Häfte {1|2|3}
     */
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
     *
     * @param gui Die aktuelle GUI
     */
    public Haelfte(GUI gui) {
        //ToDo: Art der Häfte wird wo festgelegt?
        this.gui = gui;
        this.stock = new Stock();
        this.runde = new Runde(this.stock, this.verlierer, this.gui);
        this.verlierer = null;
        this.art = 0;

    }

    /**
     * Liefert die Art der aktuellen Runde
     *
     * @return String {erste | zweite | finale}
     */
    public int getArt() {
        return this.art;
    }

    /**
     * Liefert die aktuelle Runde der Haelfte
     *
     * @return Runde: Object
     */
    public Runde getRunde() {
        return this.runde;
    }

    /**
     * Resettet den Stock, damit wieder Chips vorhanden sind.
     */
    public void resetStock() {
        this.stock = new Stock();
    }

    /**
     * Liefert den Stock der Hälfte
     *
     * @return Stock Object
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * Setzt den Verlierer der Hälfte
     *
     * @param verlierer Spieler Object
     */

    public void setVerlierer(Spieler verlierer) {
        this.verlierer = verlierer;
    }

    /**
     * Setzt die Art der Hälfte
     *
     * @param art int {1|2|3}
     */
    public void setArt(int art) {
        this.art = art;
    }

    /**
     * Aktualisiert das Hälftenobjekt mit den aktuellen Daten aus der Datenbank
     *
     * @param spielID Int- Die SpielID, zu dem die Hälfte gehöt
     * @see Datenbank#selectAktuelleHaelfte(int)
     * @see Datenbank#selectHaelftenVerlierer(int, int)
     *
     */
    public void updateHaelfte(int spielID) {
        try {
            this.art = Datenbank.getInstance().selectAktuelleHaelfte(spielID);
            String verliererName = Datenbank.getInstance().selectHaelftenVerlierer(spielID, this.art);
            this.verlierer = new Spieler(verliererName, Datenbank.getInstance().selectProfilBild(verliererName));
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
