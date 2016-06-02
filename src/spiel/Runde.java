package spiel;


import Datenbank.Datenbank;
import gui.GUI;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

/**
 * Created by KNapret on 23.05.2016.
 */
public class Runde {

    private GUI gui;
    private Stock stock;
    private Spieler beginner;
    private List<Spieler> teilnehmer;

    /**
     * Constructor Runde
     *  @param stock    - Der Stock der jeweiligen Runde
     * @param beginner - Wird initial durch Auswuerfeln festgelegt. Danach immer der Verlierer der letzten Runde
     * @param gui
     */
    public Runde(Stock stock, Spieler beginner, GUI gui) {
        this.gui = gui;
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
        return this.beginner.getBecher().getAnzahlWuerfe();
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
                verteileStrafpunkte(gewinner, verlierer, gewinner.getBecher().getSortierteWuerfel()[0].getWert());
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
        String wuerfelG = "" + gewinner.getBecher().getSortierteWuerfel()[0].getWert() + "-" + gewinner.getBecher().getSortierteWuerfel()[1].getWert() + "-" + gewinner.getBecher().getSortierteWuerfel()[2].getWert();
        String wuerfelV = "" + verlierer.getBecher().getSortierteWuerfel()[0].getWert() + "-" + verlierer.getBecher().getSortierteWuerfel()[1].getWert() + "-" + verlierer.getBecher().getSortierteWuerfel()[2].getWert();
        String wuerfelM = "" +teilnehmer.get(1).getBecher().getSortierteWuerfel()[0].getWert()+"-"+teilnehmer.get(1).getBecher().getSortierteWuerfel()[1].getWert()+"-"+teilnehmer.get(1).getBecher().getSortierteWuerfel()[2].getWert();

        String ausgabe = "Gewinner: " + gewinner.getName() + " mit: " + gewinner.getLetztesBild() + "(" + wuerfelG + ")" +
                "\n Mitte: " +teilnehmer.get(1).getName() + "mit: " +teilnehmer.get(1).getLetztesBild()+"("+wuerfelM+")"+
                "\n Verlierer: " + verlierer.getName() + " mit: " + verlierer.getLetztesBild() + "(" + wuerfelV + ")"+
                "\n Verlierer hat "+verlierer.getStrafpunkte()+" bekommen";
        JOptionPane.showMessageDialog(null, ausgabe);



        //Resetten der würfe, damit weitergespielt werden kann, wird später vond er GUI gemacht!
        for(Spieler s: teilnehmer){
            s.getBecher().resetWurf();
        }
        //ToDO: Richtige Anzahl an kassierten Strafpunkten ausgeben
        try {
            int spielID = Datenbank.getInstance().selectSpielID(verlierer.getName());
            gui.sendeUpdateCounter(teilnehmer.size());
            Datenbank.getInstance().insertRundenergebnis(spielID, verlierer.getName(), gewinner.getName());
            Datenbank.getInstance().insertRunde(spielID);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        gui.sendeUpdateSignal(gewinner.getName()+" mit "+gewinner.getLetztesBild()+" || "+ verlierer.getName()+" mit "+verlierer.getLetztesBild()+". Bekommt: "+verlierer.getStrafpunkte());

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
            try {

                Datenbank.getInstance().updateSpieler(gewinner.getName(),gewinner.getStrafpunkte());
                Datenbank.getInstance().updateSpieler(verlierer.getName(),verlierer.getStrafpunkte());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            verlierer.pushStrafpunkte(stock.popStrafpunkt(anzahl));
            try {
                int spielID = Datenbank.getInstance().selectSpielID(verlierer.getName());
                Datenbank.getInstance().updateStock(anzahl,spielID);
                Datenbank.getInstance().updateSpieler(verlierer.getName(),verlierer.getStrafpunkte());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
