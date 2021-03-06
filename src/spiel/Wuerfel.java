package spiel;

import Grafik.Grafik;

import javax.swing.*;
import java.io.Serializable;

/**
 *
 * <pre>
 * Wuerfelklasse
 * </pre>
 * @author KNapret
 *
 */
public class Wuerfel implements Serializable,Comparable<Wuerfel> {
    /**
     * Der Wert eines Würfels {1-6}
     */
    private int wert;
    /**
     * Boolean Wert, ob der Würfel ausgelegt ist
     */
    private boolean draussen;
    /**
     * Das Bild des Würfels
     */
    private Icon grafik;
    /**
     * Alle nötigen Bilder eines Würfels
     */
    private Icon[] bilder ={Grafik.WUERFEL_1, Grafik.WUERFEL_2, Grafik.WUERFEL_3,Grafik.WUERFEL_4,Grafik.WUERFEL_5,Grafik.WUERFEL_6};

    /**
     * <pre>
     * Erstellt einen neuen Wuerfel
     * Setzt den Wert des Wuerfels auf eine Zufallszahl zwischen 1 und 6
     * Setzt das Bild des Wuerfels auf die entsprechende Grafik.
     * Setzt draussen auf false
     *
     * </pre>
     */
    public Wuerfel(){

        this.wert = (int)(Math.random()*6)+1;
        this.grafik = bilder[this.wert-1];
        this.draussen = false;

    }

    /**
     * Aendert "draussen" auf den ubergebenen Wert.
     * @param wert boolean {true | false]}
     */
    public void setDraussen(boolean wert) {
        this.draussen = wert;
    }

    /**
     * Liefert die aktuelle Grafik des Bildes
     * @return Icon - Die Grafik des Wuerfels
     */
    public Icon getGrafik(){
        return this.grafik;
    }

    /**
     * Liefert "draussen" des Wuerfels
     * @return boolean - {true | false}
     */
    public boolean getDraussen(){
        return this.draussen;
    }

    /**
     * Liefert den aktuellen Wert des Wuerfels
     * @return Int - {1 | 2 | 3 | 4 | 5 | 6}
     */
    public int getWert(){
        return this.wert;
    }

    /**
     * <pre>
     *     Setzt den Wert des Wuerfels auf den uebergebenen Int
     *     und passt die Grafik des Wuerfels an.
     *
     *     Wird ein falscher Wert übergeben wird, bricht die Methode ab.
     * </pre>
     * @param wert int {1 - 6}. 0 fuer nicht veraendern
     */

    public void setWert(int wert){
        if(wert<= 6 && wert >=1){
            this.wert = wert;
            this.grafik = bilder[this.wert-1];
        }else{
            return;
        }

    }

    /**
     * Weist dem Wuerfel eine Zufallszahl zwischen 1 und 6 zu
     * und setzt die Grafik des Wuerfels entsprechend.
     * Ist wuerfel.draussen = true, wird der Würfel nicht verändert.
     */

    public void wuerfeln() {

        if(this.draussen == false){
            this.wert = (int)(Math.random()*6)+1;
            this.grafik = bilder[this.wert-1];
        }

    }

    /**
     * Gibt der Wert des Würfels als String zurück
     * @return wert String Object
     */
    public String toString(){
        return ""+this.wert;
    }

    /**
     * <pre>conpareTo(ein anderer Becher)
     * Vergleicht zwei Wuerfel anhand ihrer Werte.
     *
     *
     * </pre>
     * @param w2 - Object: ein anderer Wuerfel
     * @return Int -  negativ: wuerfel -lt- w2
     *               positiv:   wuerfel -gt- w2
     *               0:         wuefel = w2
     *
     */
    @Override
    public int compareTo(Wuerfel w2){

        return w2.getWert()-this.wert;
    }


}
