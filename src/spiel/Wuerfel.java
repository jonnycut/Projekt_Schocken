package spiel;

/**
 * Created by KNapret on 02.05.2016.
 */
public class Wuerfel {
    private int wert;
    private boolean draussen;

    public Wuerfel(){
        this.wert = 0;
    }

    public int getRandomNumber(){

        this.wert = (int) (Math.random()*6+1);
        return this.wert;
    }

    public int getWert(){
        return this.wert;
    }

    public boolean getDraussen(){
        return this.draussen;
    }
    
    public void setDraussen(boolean draussen){
        this.draussen=draussen;
    }

}
