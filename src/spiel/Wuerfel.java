package spiel;

import Grafik.Grafik;

import javax.swing.*;

/**
 * Created by KNapret on 23.05.2016.
 */
public class Wuerfel implements Comparable<Wuerfel> {
    private int wert;
    private boolean draussen;
    private Icon grafik;
    private Icon[] bilder ={Grafik.WUERFEL_1, Grafik.WUERFEL_2, Grafik.WUERFEL_3,Grafik.WUERFEL_4,Grafik.WUERFEL_5,Grafik.WUERFEL_6};

    public Wuerfel(){

        this.wert = (int)(Math.random()*6)+1;
        this.grafik = bilder[this.wert-1];
        this.draussen = false;

    }

    public void setDraussen(boolean wert) {
        this.draussen = wert;
    }

    public Icon getGrafik(){
        return this.grafik;
    }

    public boolean getDraussen(){
        return this.draussen;
    }

    public int getWert(){
        return this.wert;
    }


    public void wuerfeln() {

        if(this.draussen == false){
            this.wert = (int)(Math.random()*6)+1;
            this.grafik = bilder[this.wert-1];
        }

    }

    /**
     * Liefert Wert >0  wenn WuerfelWert > Vergleichswürfel
     * Wert = 0 wenn gleichgroß
     * Wert < 0 wenn WuerfelWert < Vergleichswuerfel
     */
    @Override
    public int compareTo(Wuerfel w2){

        return this.wert - w2.getWert();
    }


}
