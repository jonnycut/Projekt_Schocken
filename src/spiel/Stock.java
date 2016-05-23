package spiel;

/**
 * Created by KNapret on 23.05.2016.
 */
public class Stock {

    private int strafpunkte;

    public Stock(){

        this.strafpunkte = 13;
    }

    public int getStrafpunkte(){
        return this.strafpunkte;
    }

    public void popStrafpunkt(){
        this.strafpunkte--;
    }
}
