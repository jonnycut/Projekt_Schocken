package spiel;


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

    /**
     * Constructor Runde
     *
     * @param stock - Der Stock der jeweiligen Runde
     * @param beginner - Wird initial durch Auswuerfeln festgelegt. Danach immer der Verlierer der letzten Runde
     */
    public Runde(Stock stock, Spieler beginner){
        this.stock = stock;
        this.beginner = beginner;
    }

    /**
     * Liefert den Stock der Runde
     * @return Object Stock
     */

    public Stock getStock(){
        return this.stock;
    }

    /**
     * Liefert die Maximalanzahl der Würfe, die in der Runde gemacht werden dürfen <br></br>
     * Wird durch den Beginner der Runde vorgelegt
     * @return Int - {1 - 3}
     */
    public int maxWuerfe(){
        return this.beginner.getBecher().getWurf();
    }

    /**<pre>
     * Erwartet ein Spieler Array,
     * wandelt es in eine ArrayList um und sortiert diese Absteigend
     * nach Werigkeit der Bilder.
     * Der 1. der Liste ist der Gewinner -> Gibt Anzahl Strafsteine vor,
     * der letzte der Verlierer -> erhält Anzahl Strafsteine.
     * Verteilung der Steine wird durch Aufruf von
     * verteileStrafpunkte(Spieler, Spieler)
     * vorgenommen</pre>
     *
     * @param spieler  SpielerArray
     */
    public void auswertenBilder(Spieler[] spieler){
        Spieler gewinner = null;
        Spieler verlierer = null;

        List<Spieler> spielerList = new ArrayList<Spieler>();
        for(Spieler s : spieler){
            spielerList.add(s);
        }

        Collections.sort(spielerList);
        gewinner = spielerList.get(0);
        verlierer = spielerList.get(spielerList.size()-1);

        switch(gewinner.getLetztesBild()){
            case "Schock aus":
                //ToDo: Runde zuende!
            case "Schock":
                verteileStrafpunkte(gewinner,verlierer,gewinner.getBecher().getWuerfel()[0].getWert());
                break;
            case "General":
                verteileStrafpunkte(gewinner,verlierer,3);
                break;
            case "Straße":
                verteileStrafpunkte(gewinner,verlierer,2);
                break;
            case "Zahl":
                verteileStrafpunkte(gewinner,verlierer,1);
        }




    }

    /**
     * <pre>
     *     Verteilt die Strafchips der Runde.
     *     Wenn [ANZAHL] noch auf Stock, bekommt der Verlierer diese
     *     Wenn [ANZAHL] < StockChips: Verlierer bekommt übrige Chips
     *     Wenn Stock = Leer: Verlierer bekommt [ANZAHL] von Gewinner
     * </pre>
     * @param gewinner Object Spieler - Gewinner der Runde
     * @param verlierer Object Spieler Verlierer der Runde
     * @param anzahl Int Anzahl der Strafchips
     */
    public void verteileStrafpunkte(Spieler gewinner, Spieler verlierer, int anzahl){

        if(this.stock.getStrafpunkte()==0){
            gewinner.popStrafpunkte(anzahl);
            verlierer.pushStrafpunkte(anzahl);
        }else{
            verlierer.pushStrafpunkte(stock.popStrafpunkt(anzahl));
        }


    }
}
