package spiel;

import Datenbank.Datenbank;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;


/**
 *
 * <pre>
 * Hauptklasse des Spielers
 *
 * Enthaelt alle noetigen Daten eines Spielers
 *
 * </pre>
 * @author KNapret
 * @see java.lang.Comparable
 */
public class Spieler implements Comparable<Spieler> {
    /**
     * Der Becher des Spielers
     * @see Becher
     */
    private Becher becher;

    /**
     * Anzahl der Strafpunkte des Spielers
     */
    private int strafpunkte;

    /**
     * Der Startwurf, der beim Auswürfeln gewürfelt wird
     * @see Spieler#beginnerWuerfeln()
     */
    public int startwurf;
    /**
     * Das Profilbild des Spielers
     */
    private Icon profilBild;
    /**
     * Das jeweils letzt geworfene Bild des Spielers<br></br>
     * {Schock aus | Schock | General | Straße | Zahl}
     */
    private String letztesBild;
    /**
     * Der Spielername
     */
    private String name;
    /**
     * Ob ein Spieler fertig ist
     */
    private boolean fertig = false;
    /**
     * Ob ein Spieler aktiv ist ( gerade am zug
     */
    private boolean aktiv = false;


    /**
     * Erstellt einen neuen Spieler mit dem angegeben Namen und Profilbild
     * Becher wird erstellt, Strafpunkte und Hälfte auf 0 gesetzt
     *
     * @param name       - Spielername
     * @param profilBild - ProfilBild des Spielers
     */
    public Spieler(String name, Icon profilBild) {

        this.becher = new Becher();
        this.profilBild = profilBild;
        this.strafpunkte = 0;
        this.letztesBild = becher.getBild();
        this.name = name;

    }

    /**
     * Liefert das letzte gewürfelte Bild als String zurücl
     *
     * @return String {Schock|General|Straße|Zahl}
     */
    public String getLetztesBild() {
        return letztesBild;
    }


    /**
     * Liefert Spieler.fertig
     * @return boolean
     */
    public boolean getFertig() {
        return this.fertig;
    }

    /**
     * Setzt einen Spieler aktiv oder inaktiv
     * @param aktiv boolean {true | false}
     */
    public void setAktiv(boolean aktiv){
        this.aktiv = aktiv;
    }

    /**
     * Liefert, ob ein Spieler aktiv ist
     * @return aktiv boolean {true | false}
     */
    public boolean getAktiv(){
        return this.aktiv;
    }

    /**
     * Setzt Spieler.fertig auf den angegebenen Wert
     *
     *
     */
    public void setFertig(boolean fertig) {
        this.fertig = fertig;
    }
    /**
     * Liefert den Spielernamen
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Liefert den Becher des Spielers
     *
     * @return Object Becher
     */
    public Becher getBecher() {
        return this.becher;
    }

    public void setStrafpunkte(int strafpunkte) {
        this.strafpunkte = strafpunkte;
    }

    /**
     * Liefert das Profilbild des Spielers
     *
     * @return Object ImageIcon
     */
    public Icon getProfilBild() {
        return profilBild;
    }

    /**
     * Liefert die aktuellen Strafpunkte des Spielers
     *
     * @return int
     */
    public int getStrafpunkte() {
        return strafpunkte;
    }


    /**
     * <pre>
     * Nutzt die wuerfeln() Methode des Bechers
     * Ändert das Attribut letztesBild auf den aktuellen Wert
     * </pre>
     * @see Becher#wuerfeln()
     */
    public void wuerfeln() {
        this.becher.wuerfeln();
        this.letztesBild = this.becher.getBild();



    }

    /**
     * <pre>
     * Würfelmethode für das initiale Auswürfeln des Beginners.
     * Schreibt das Attribut Startwurf in die Datenbank</pre>
     * @see Datenbank#insertStartwurf(String, int)
     *
     */
    public void beginnerWuerfeln() {

        this.becher.wuerfeln();
        this.becher.resetWurf();
        Wuerfel[] temp = becher.getSortierteWuerfel();
        this.startwurf = (int) (temp[0].getWert() * Math.pow(10, 2)) + temp[1].getWert() * 10 + temp[2].getWert();

        try {
            Datenbank.getInstance().insertStartwurf(name,startwurf);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Liefert den Startwurf des Spielers
     * @return startwurf int - dreistellige Int Zahl
     */
    public int getStartwurf(){
        return this.startwurf;
    }

    /**
     * Setzt den Startwurf des Spielers
     * @param startwurf int - dreistellig, absteigend sortiert, muss mit einem Würfel darstellbar sein (z.B. 631 | 521)
     */
    public void setStartwurf(int startwurf){
        this.startwurf=startwurf;
    }

    /**
     * Zieht [anzahl] Strafunkte vom Spieler ab.
     *
     * @param anzahl Anzahl der Strafpunkte, die der Spieler abgibt.
     */
    public void popStrafpunkte(int anzahl) {

        this.strafpunkte -= anzahl;

    }

    /**
     * Fügt dem Spieler [anzahl] Strafpunkte hinzu
     *
     * @param anzahl Anzahl Strafpunkte, die der Spieler bekommt
     */

    public void pushStrafpunkte(int anzahl) {

        this.strafpunkte += anzahl;
    }

    /**
     * <pre>
     *  Setzt dem Becher des Spielers ein neues WuerfelArray als Wurf.
     * Nutzt Becher.setWurf(Wuerfel[]), diese sortiert und setzt Becher.Bild.
     * Danach wird Spieler.letztesBild = Becher.getBild();
     * </pre>
     *
     * @param wuerfelArray - Ein Array aus 3 Object Wuerfel
     * @see Becher#setWurf(Wuerfel[])
     */
    public void setWurf(Wuerfel[] wuerfelArray){
        this.becher.setWurf(wuerfelArray);
        this.letztesBild = becher.getBild();
    }



    /**
     * <pre>
     * compareTo Methode um Spieler vergleichbar zu machen.
     * Spieler werden anhand ihrer Würfe unterschieden.
     * Nutzt Becher.compareTo(becher2)</pre>
     *
     * @param s2 Object Spieler, mit dem verglichen wird
     * @return int - negativ, wenn dieser Spieler einen schlechteren Wurf hat,<br></br>
     * positiv, wenn dieser Spieler einen besseren Wurf hat.
     * 0 bei Gleichstand
     */

    @Override
    public int compareTo(Spieler s2) {
        return this.becher.compareTo(s2.getBecher());
    }

    /**
     * <pre>
     *
     * Vergleicht zwei Spieler anhand ihrer Strafpunkte.
     * Gibt int > 0 zurueck, Wenn s1 mehr Strafpunkte als s2 hat
     * Gibt int = 0 zurueck, wenn beide Spieler gleich viele Strafpunkte haben
     * Gibt int < 0 zurueck, wenn s1 weniger Strafpunkte als s2 hat</pre>
     */
    public static final Comparator<Spieler>

            STRAFPUNKTE_ORDER = new Comparator<Spieler>() {
        public int compare(Spieler s1, Spieler s2) {
            return s1.getStrafpunkte() - s2.getStrafpunkte();
        }
    };

    public static final Comparator<Spieler>

            WURF_ORDER = new Comparator<Spieler>() {
        @Override
        public int compare(Spieler o1, Spieler o2) {
            return o1.getBecher().compareTo(o2.getBecher());
        }
    };

    /**
     * <pre>
     * Vergleicht zwei Spieler anhand ihrer Namen.
     * Nutzt dazu String.compareTo(anotherString)
     * </pre>
     */
    public static final Comparator<Spieler>
            NAME_ORDER = new Comparator<Spieler>() {
        @Override
        public int compare(Spieler s1, Spieler s2) {
            return s1.getName().compareTo(s2.getName());
        }
    };

    /**
     * Vergleicht zwei Spieler anhand ihrer Startwuerfe.
     */
    public static final Comparator<Spieler>
            START_ORDER = new Comparator<Spieler>(){
                @Override
                public int compare(Spieler s1, Spieler s2){
                    return s2.getStartwurf() - s1.getStartwurf();
                }
            };

}
