package spiel;

import Datenbank.Datenbank;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by KNapret on 23.05.2016.
 */
public class Stock {

    private int strafpunkte;

    public Stock(){

        this.strafpunkte = 0;
    }

    public int getStrafpunkte(){
        return this.strafpunkte;
    }

    public int popStrafpunkt(int anzahl){

        if(this.strafpunkte>= anzahl){
            this.strafpunkte-=anzahl;
            return anzahl;

        }else if(this.strafpunkte<anzahl){
            int puffer = strafpunkte;
            strafpunkte = 0;
            return puffer;
        }else{
            return 0;
        }


    }

    public void updateStock(int spielID) {

        try {
            this.strafpunkte = Datenbank.getInstance().selectStockStatus(spielID);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
