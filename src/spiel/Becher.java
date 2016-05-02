package spiel;

/**
 * Created by KNapret on 02.05.2016.
 */
public class Becher {

    private Wuerfel wuerfel[];
    private String bild ="";

    public Becher(){

        for(int i =0; i<3;i++){
            this.wuerfel[i] = new Wuerfel();
        }

        this.bild = null;
    }

    public void wuerfeln(){

        for(Wuerfel w : this.wuerfel){
            if(!w.getDraussen()){
                w.getRandomNumber();
            }

        }

    }



}
