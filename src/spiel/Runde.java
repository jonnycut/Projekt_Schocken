package spiel;

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

    public void auswertenBilder(SpielerPanel[] spielerPanels){

        String hoechstesBild = spielerPanels[0].getLetztesBild();



    }
}
