package spiel;

import javax.swing.*;
import java.util.Comparator;
import java.util.HashMap;


/**
 * Created by KNapret on 23.05.2016.
 * <pre>
 * Hauptklasse des Spielers
 *
 * Enthaelt alle noetigen Daten eines Spielers
 *
 * </pre>
 */
public class Spieler implements Comparable<Spieler> {

    private Becher becher;



    private int strafpunkte;
    private int haelfte;
    private Icon profilBild;
    private String letztesBild;
    private String name;
    private boolean fertig = false;
    private HashMap<String, Integer> statistik = new HashMap<>();

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
        this.haelfte = 0;
        this.letztesBild = becher.getBild();
        this.name = name;

        String[] wuerfe = {"Schock aus", "Schock", "General", "Straße", "Zahl"};
        for (String s : wuerfe) {
            this.statistik.put(s, 0);
        }
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
     * Liefert den Spielernamen
     *
     * @return String
     */

    public boolean getFertig() {
        return this.fertig;
    }

    public void setFertig() {
        this.fertig = true;
    }

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

    public HashMap<String, Integer> getStatistik() {
        return this.statistik;
    }

    public void pushStatistik(String key, Integer value) {
        statistik.put(key, value);
    }

    /**
     * Nutzt die wuerfeln() Methode des Bechers
     * Ändert das Attribut letztesBild auf den aktuellen Wert
     */
    public void wuerfeln() {
        this.becher.wuerfeln();
        this.letztesBild = this.becher.getBild();
        statistik.put(letztesBild, statistik.get(letztesBild) + 1);

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
     * <pre>
     *     Setzt die Wuerfel des Bechers auf die uebergebenen int Werte.
     *     Wird ein Wert mit 0 uebergeben, wird dieser Wuerfel nicht veraendert.
     *
     * </pre>
     *
     * @param w1 int {1-6} Wert des ersten Wuerfels, 0 fuer nicht veraendern.
     * @param w2 int {1-6} Wert des zweiten Wuerfels, 0 fuer nicht veraendern.
     * @param w3 int {1-6} Wert des dritten Wuerfels, 0 fuer nicht veraendern.
     */
    public void setWuerfel(int w1, int w2, int w3) {
        becher.setWurf(w1, w2, w3);

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
     * Würfelmethode für das initiale Auswürfeln des Beginners.
     * Liefert zum späteren, einfacheren Vergleich die Summe des Wurfs zurück</pre>
     *
     * @return Int Summe der 3 Würfel
     */
    public int beginnerWuerfeln() {

        this.becher.wuerfeln();
        Wuerfel[] temp = becher.getWuerfel();
        return temp[0].getWert() + temp[1].getWert() + temp[2].getWert();

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
}
