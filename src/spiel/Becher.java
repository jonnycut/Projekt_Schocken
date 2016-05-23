package spiel;

/**
 * Created by KNapret on 23.05.2016.
 */
public class Becher {
    private Wuerfel[] wuerfel;
    private String bild;
    private int wurf;


    public Becher(){

        for(int i =0; i<3;i++){
            wuerfel[i] = new Wuerfel();
        }

        this.bild = null;
        this.wurf = 0;
    }

    public String getBild() {
        return bild;
    }

    public Wuerfel[] getWuerfel(){
        return  this.wuerfel;
    }


    public void wuerfeln() {
        this.wurf++;

        for(Wuerfel w : this.wuerfel){
            w.wuerfeln();
        }

        Wuerfel pufferWuerfel;
        for(int i=0; i<1;i++){
            if(wuerfel[i].compareTo(wuerfel[i+1])<0){
                pufferWuerfel = wuerfel[i];
                wuerfel[i] = wuerfel[i+1];
                wuerfel[i+1] = pufferWuerfel;

            }
        }

    }



    public void aufdecken(){

        for(Wuerfel w : this.wuerfel){
            w.setDraussen(true);

        }
    }

}
