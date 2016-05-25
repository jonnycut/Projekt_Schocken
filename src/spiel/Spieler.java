package spiel;

import javax.swing.*;


/**
 * Created by KNapret on 23.05.2016.
 * <pre>
 * Hauptklasse des Spielers
 *
 * Enthaelt alle noetigen Daten eines Spielers
 *
 * </pre>
 *
 *
 */
public class Spieler implements Comparable <Spieler>{

    private Becher becher;
    private int strafpunkte;
    private int haelfte;
    private Icon profilBild;
    private String letztesBild;
    private String name;
    private boolean fertig = false;

    /**
     * Erstellt einen neuen Spieler mit dem angegeben Namen und Profilbild
     * Becher wird erstellt, Strafpunkte und Hälfte auf 0 gesetzt
     * @param name - Spielername
     * @param profilBild - ProfilBild des Spielers
     */
    public Spieler(String name, Icon profilBild){

        this.becher = new Becher();
        this.profilBild = profilBild;
        this.strafpunkte = 0;
        this.haelfte = 0;
        this.letztesBild = becher.getBild();
        this.name = name;
    }

    /**
     * Liefert das letzte gewürfelte Bild als String zurücl
     * @return String {Schock|General|Straße|Zahl}
     */
    public  String getLetztesBild(){
        return letztesBild;
    }

    /**
     * Liefert den Spielernamen
     * @return String
     */

    public boolean getFertig(){
        return this.fertig;
    }

    public void setFertig(){
        this.fertig = true;
    }
    public String getName() {
        return name;
    }

    /**
     * Liefert den Becher des Spielers
     * @return Object Becher
     */
    public Becher getBecher(){
        return  this.becher;
    }

    /**
     * Liefert das Profilbild des Spielers
     * @return Object ImageIcon
     */
    public Icon getProfilBild() {
        return profilBild;
    }

    /**
     * Liefert die aktuellen Strafpunkte des Spielers
     * @return int
     */
    public int getStrafpunkte() {
        return strafpunkte;
    }

    /**
     * Nutzt die wuerfeln() Methode des Bechers
     * Ändert das Attribut letztesBild auf den aktuellen Wert
     */
    public void wuerfeln(){
        this.becher.wuerfeln();
        this.letztesBild = this.becher.getBild();

    }

    /**
     * Zieht [anzahl] Strafunkte vom Spieler ab.
     * @param anzahl  Anzahl der Strafpunkte, die der Spieler abgibt.
     */
    public void popStrafpunkte(int anzahl){

        this.strafpunkte -= anzahl;

    }

    /**
     * Fügt dem Spieler [anzahl] Strafpunkte hinzu
     * @param anzahl Anzahl Strafpunkte, die der Spieler bekommt
     */

    public void pushStrafpunkte(int anzahl){

        this.strafpunkte += anzahl;
    }

    /**
     * <pre>
     * Würfelmethode für das initiale Auswürfeln des Beginners.
     * Liefert zum späteren, einfacheren Vergleich die Summe des Wurfs zurück</pre>
     * @return Int Summe der 3 Würfel
     */
    public int beginnerWuerfeln(){

        this.becher.wuerfeln();
        Wuerfel[] temp = becher.getWuerfel();
        return temp[0].getWert()+temp[1].getWert()+temp[2].getWert();

    }

    /**
     * <pre>
     * compareTo Methode um Spieler vergleichbar zu machen.
     * Spieler werden anhand ihrer Würfe unterschieden.
     * Nutzt Becher.compareTo(becher2)</pre>
     * @param s2 Object Spieler, mit dem verglichen wird
     * @return int - negativ, wenn dieser Spieler einen schlechteren Wurf hat,<br></br>
     * positiv, wenn dieser Spieler einen besseren Wurf hat.
     * 0 bei Gleichstand
     */

    @Override
    public int compareTo(Spieler s2) {
        return this.becher.compareTo(s2.getBecher());
    }
}
