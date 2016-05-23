package spiel;

import javax.swing.*;
import grafik.Grafik;





/**
 * Created by KNapret on 23.05.2016.
 */
public class Spieler implements Comparable <Spieler>{
    private Becher becher;
    private int strafpunkte;
    private int haelfte;
    private Grafik profilBild;
    private String letztesBild;
    private String name;

    public Spieler(String name, Grafik profilBild){

        this.becher = new Becher();
        this.profilBild = profilBild;
        this.strafpunkte = 0;
        this.haelfte = 0;
        this.letztesBild = null;
        this.name = name;
    }

    public  String getLetztesBild(){
        return letztesBild;
    }

    public Becher getBecher(){
        return  this.becher;
    }

    public Grafik getProfilBild() {
        return profilBild;
    }

    public void wuerfeln(){
        this.becher.wuerfeln();
        this.letztesBild = this.becher.getBild();

    }

    public void popStrafpunkte(int anzahl){

        this.strafpunkte -= anzahl;

    }

    public void pushStrafpunkte(int anzahl){

        this.strafpunkte += anzahl;
    }

    public String beginnerWuerfeln(){

        this.becher.wuerfeln();
        this.letztesBild = this.becher.getBild();
        return this.letztesBild;

    }

    @Override
    public int compareTo(Spieler o) {
        return this.becher.compareTo(o.getBecher());
    }
}
