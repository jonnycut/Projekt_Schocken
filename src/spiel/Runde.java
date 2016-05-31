package spiel;


import sun.security.provider.ConfigFile;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by KNapret on 23.05.2016.
 */
public class Runde {

    //private GUI gui;
    private Stock stock;
    private Spieler beginner;
    private List<Spieler> teilnehmer;

    /**
     * Constructor Runde
     *
     * @param stock    - Der Stock der jeweiligen Runde
     * @param beginner - Wird initial durch Auswuerfeln festgelegt. Danach immer der Verlierer der letzten Runde
     */
    public Runde(Stock stock, Spieler beginner) {
        this.stock = stock;
        this.beginner = beginner;

    }

    public void setTeilnehmer(List<Spieler> teilnehmer) {
        this.teilnehmer = teilnehmer;
        if(teilnehmer.size()>0)
            this.beginner = teilnehmer.get(teilnehmer.size()-1);
    }

    /**
     * Liefert den Stock der Runde
     *
     * @return Object Stock
     */

    public Stock getStock() {
        return this.stock;
    }

    /**
     * Liefert die Maximalanzahl der Würfe, die in der Runde gemacht werden dürfen <br></br>
     * Wird durch den Beginner der Runde vorgelegt
     *
     * @return Int - {1 - 3}
     */
    public int maxWuerfe() {
        return this.beginner.getBecher().getWurf();
    }

    /**
     * <pre>
     *
     * sortiert die Teilnehmerliste absteigend
     * nach Werigkeit der Bilder.
     * Der 1. der Liste ist der Gewinner -> Gibt Anzahl Strafsteine vor,
     * der letzte der Verlierer -> erhält Anzahl Strafsteine.
     * Verteilung der Steine wird durch Aufruf von
     * verteileStrafpunkte(Spieler, Spieler)
     * vorgenommen
     *
     * Wird durch das Spielfeld genutzt</pre>
     * @see gui.Spielfeld
     */
    public void auswertenBilder() {
        Spieler gewinner = null;
        Spieler verlierer = null;


        Collections.sort(teilnehmer,Spieler.WURF_ORDER);

        verlierer = teilnehmer.get(0);
        gewinner = teilnehmer.get(teilnehmer.size() - 1);

        switch (gewinner.getLetztesBild()) {
            case "Schock aus":
                //ToDo: Runde zuende!
                verteileStrafpunkte(gewinner,verlierer,13);
                break;
            case "Schock":
                verteileStrafpunkte(gewinner, verlierer, gewinner.getBecher().getWuerfel()[0].getWert());
                break;
            case "General":
                verteileStrafpunkte(gewinner, verlierer, 3);
                break;
            case "Straße":
                verteileStrafpunkte(gewinner, verlierer, 2);
                break;
            case "Zahl":
                verteileStrafpunkte(gewinner, verlierer, 1);
                break;
            default:
                System.out.println("Fehler im letzten Bild");
        }

        //TestAusgabe, fliegt später raus:
        String wuerfelG = "" + gewinner.getBecher().getWuerfel()[0].getWert() + "-" + gewinner.getBecher().getWuerfel()[1].getWert() + "-" + gewinner.getBecher().getWuerfel()[2].getWert();
        String wuerfelV = "" + verlierer.getBecher().getWuerfel()[0].getWert() + "-" + verlierer.getBecher().getWuerfel()[1].getWert() + "-" + verlierer.getBecher().getWuerfel()[2].getWert();
        String wuerfelM = "" +teilnehmer.get(1).getBecher().getWuerfel()[0].getWert()+"-"+teilnehmer.get(1).getBecher().getWuerfel()[1].getWert()+"-"+teilnehmer.get(1).getBecher().getWuerfel()[2].getWert();

        String ausgabe = "Gewinner: " + gewinner.getName() + " mit: " + gewinner.getLetztesBild() + "(" + wuerfelG + ")" +
                "\n Mitte: " +teilnehmer.get(1).getName() + "mit: " +teilnehmer.get(1).getLetztesBild()+"("+wuerfelM+")"+
                "\n Verlierer: " + verlierer.getName() + " mit: " + verlierer.getLetztesBild() + "(" + wuerfelV + ")"+
                "\n Verlierer hat "+verlierer.getStrafpunkte()+" bekommen";
        JOptionPane.showMessageDialog(null, ausgabe);

        //Resetten der würfe, damit weitergespielt werden kann, wird später vond er GUI gemacht!
        for(Spieler s: teilnehmer){
            s.getBecher().resetWurf();
        }

    }

    /**
     * <pre>
     *     Verteilt die Strafchips der Runde.
     *     Wenn [ANZAHL] noch auf Stock, bekommt der Verlierer diese
     *     Wenn [ANZAHL] < StockChips: Verlierer bekommt übrige Chips
     *     Wenn Stock = Leer: Verlierer bekommt [ANZAHL] von Gewinner
     * </pre>
     *
     * @param gewinner  Object Spieler - Gewinner der Runde
     * @param verlierer Object Spieler Verlierer der Runde
     * @param anzahl    Int Anzahl der Strafchips
     */
    public void verteileStrafpunkte(Spieler gewinner, Spieler verlierer, int anzahl) {
        //ToDo: Schock Aus = RundenEnde
        if (this.stock.getStrafpunkte() == 0) {
            gewinner.popStrafpunkte(anzahl);
            verlierer.pushStrafpunkte(anzahl);
        } else {
            verlierer.pushStrafpunkte(stock.popStrafpunkt(anzahl));
        }


    }

    //testmethode
    public boolean pruefeFertig() {
        int counter = 0;

        for (Spieler s : teilnehmer) {
            if (s.getFertig() == true)
                counter++;
        }

        if (counter == teilnehmer.size()) {
            auswertenBilder();
            return true;
        } else
            return false;

    }
}
