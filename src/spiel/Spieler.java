package spiel;

import Datenbank.Datenbank;
import sun.security.provider.ConfigFile;

import javax.swing.*;
import java.sql.SQLException;
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
    public int startwurf;
    private Icon profilBild;
    private String letztesBild;
    private String name;
    private boolean fertig = false;
    private boolean aktiv = false;



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
        statistik.put("Schock aus",0);
        statistik.put("Schock",0);
        statistik.put("Straße",0);
        statistik.put("General",0);
        statistik.put("Zahl",0);

        statistik.put(letztesBild,1);

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
     * Liefert Spieler.fertig
     * @return boolean
     */
    public boolean getFertig() {
        return this.fertig;
    }

    public void setAktiv(boolean aktiv){
        this.aktiv = aktiv;
    }

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

    /**<pre>
     * Liefert die Aktuelle Statistik des Spielers als HashMap
     *     Key {Schock aus, Schock, General, Straße, Zahl}
     *     Value {Integer}
     * </pre>
     *
     * @return Object HashMap{String, Integer}
     * @see HashMap
     */

    public HashMap<String, Integer> getStatistik() {
        return this.statistik;
    }

    /**
     * Setzt die Statistik des Spielers
     * @param statistik Object Hashmap{String, Integer}
     * @see HashMap
     */
    public void setStatistik(HashMap<String, Integer> statistik) {
        this.statistik = statistik;
    }

    /**<pre>
     * Erhoeht die Anzahl des uebergebenen Keys in der Statistik um 1.
     * Wird ein falscher key übergeben, wird abgebrochen.
     * </pre>
     * @param key {Schock aus, Schock, General, Straße, Zahl}
     */
    public void pushStatistik(String key) {
        if(statistik.containsKey(key)){
            statistik.put(key, statistik.get(key)+1);
        }else
            return;
    }

    /**
     * <pre>
     * Nutzt die wuerfeln() Methode des Bechers
     * Ändert das Attribut letztesBild auf den aktuellen Wert
     * Erhoeht die Anzahl des gewuerfelten Bildes in der Statistik
     * </pre>
     * @see Becher
     */
    public void wuerfeln() {
        this.becher.wuerfeln();
        this.letztesBild = this.becher.getBild();
        pushStatistik(letztesBild);


    }

    /**
     * <pre>
     * Würfelmethode für das initiale Auswürfeln des Beginners.
     * Schreint das Attribut Startwurf in die Datenbank</pre>
     *
     */
    public void beginnerWuerfeln() {

        this.becher.wuerfeln();
        this.becher.resetWurf();
        Wuerfel[] temp = becher.getWuerfel();
        this.startwurf = (int) (temp[0].getWert() * Math.pow(10, 2)) + temp[1].getWert() * 10 + temp[2].getWert();

        try {
            Datenbank.getInstance().insertStartwurf(name,startwurf);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getStartwurf(){
        return this.startwurf;
    }

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
     *  Setzt dem Becher des Spielers ein neues WuerfelArray als Wurf.
     * Nutzt Becher.setWurf(Wuerfel[]), diese sortiert und setzt Becher.Bild.
     * Danach wird Spieler.letztesBild = Becher.getBild();
     * </pre>
     *
     * @param wuerfelArray - Ein Array aus 3 Object Wuerfel
     * @see Becher#setWurf(Wuerfel[])
     */
    public void setWurt(Wuerfel[] wuerfelArray){
        this.becher.setWurf(wuerfelArray);
        this.letztesBild = becher.getBild();
    }

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
