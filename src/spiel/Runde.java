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


    public Runde(Stock stock){
        this.stock = stock;
    }

    public Stock getStock(){
        return this.stock;
    }

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

    public void verteileStrafpunkte(Spieler gewinner, Spieler verlierer, int anzahl){

        if(this.stock.getStrafpunkte()==0){
            gewinner.popStrafpunkte(anzahl);
            verlierer.pushStrafpunkte(anzahl);
        }else{
            verlierer.pushStrafpunkte(stock.popStrafpunkt(anzahl));
        }


    }
}
