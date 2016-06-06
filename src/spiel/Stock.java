package spiel;

import Datenbank.Datenbank;

import java.sql.SQLException;
import java.util.Date;

/**
 * Klasse Stock - dient zur Datenhaltung
 *
 * @author KNapret
 */
public class Stock {
    /**
     * Die Strafpunkte des Stocks
     */
    private int strafpunkte;

    /**
     * Die Strafpunkte des Stocks werden initial auf 0 gesetzt
     */
    public Stock() {

        this.strafpunkte = 0;
    }

    /**
     * Liefert die aktuellen Strafpunkte auf dem Stock
     *
     * @return strafpunkte int Wert der Strafpunkte
     */
    public int getStrafpunkte() {
        return this.strafpunkte;
    }

    /**
     * Reduziert die Strafpunkte des Stocks um die ubergebene Anzahl und gibt diese zurück.
     * Wenn die anzahl -gt- strafpunkte auf dem Stock, werden nur die übrigen zurückgegeben.
     *
     * @param anzahl Int
     * @param spielID
     * @return anzahl int = anzahl
     */
    public int popStrafpunkt(int anzahl, int spielID) {

        if (this.strafpunkte >= anzahl) {
            this.strafpunkte -= anzahl;
            try {
                Datenbank.getInstance().updateStock(anzahl,spielID);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return anzahl;

        } else if (this.strafpunkte < anzahl) {
            int puffer = strafpunkte;
            strafpunkte = 0;
            try {
                Datenbank.getInstance().updateStock(puffer,spielID);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return puffer;
        } else {
            return 0;
        }


    }

    /**
     * Liest die aktuellen Strafpunkte aus der Datenbank und stetzt diese.
     *
     * @param spielID Int- Kennung des Spiels
     * @see Datenbank#selectStockStatus(int)
     */

    public void updateStock(int spielID) {

        try {
            this.strafpunkte = Datenbank.getInstance().selectStockStatus(spielID);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
