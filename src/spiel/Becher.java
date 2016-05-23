package spiel;

/**
 * Created by KNapret on 23.05.2016.
 */
public class Becher implements Comparable <Becher> {
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

        //wuerfel[] vielleicht besser als ArrayList? --> Collections.sort...?

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

        if(wuerfel[0].getWert() ==1 && wuerfel[1].getWert()==1 && wuerfel[2].getWert()==1){
            bild = "Schock aus";
        }else if(wuerfel[1].getWert() ==1 && wuerfel[2].getWert()==1){
            bild = "Schock";
        }else if(wuerfel[0].getWert() == wuerfel[1].getWert() && wuerfel[1].getWert()== wuerfel[2].getWert()){
            bild = "General";
        }else if(wuerfel[0].getWert() == wuerfel[1].getWert()+1 && wuerfel[1].getWert()== wuerfel[2].getWert()+1){
            bild = "Straße";
        } else {
            bild = "Zahl";
        }

    }



    public void aufdecken(){

        for(Wuerfel w : this.wuerfel){
            w.setDraussen(true);

        }
    }

    @Override
    public int compareTo(Becher b2) {

        Wuerfel[] w1 = this.wuerfel;
        Wuerfel[] w2 = b2.getWuerfel();

        if(w1[1].getWert()==1 && w1[2].getWert() ==1){
            if(w2[1].getWert()==1 && w2[2].getWert() ==1){
                return w1[0].getWert()-w2[0].getWert();
            }else {
                return w1[0].getWert();
            }


        }else if(w1[0].getWert()==w1[1].getWert() && w1[1].getWert()==w1[2].getWert()){
            if(w2[0].getWert()==w2[1].getWert() && w2[1].getWert()==w2[2].getWert()){
                return w1[0].getWert()-w2[0].getWert();
            }else{
                return w1[0].getWert();
            }


        } else if(w1[0].getWert()==w1[1].getWert()-1 && w1[1].getWert()==w1[2].getWert()-1){
            if(w2[0].getWert()==w2[1].getWert()+1 && w2[1].getWert()==w2[2].getWert()+1){
                return w1[0].getWert()-w2[0].getWert();
            }else{
                return w1[0].getWert();
            }
        } else if (w2[1].getWert() == 1 && w2[2].getWert()==1){
            return 0-w2[0].getWert();
        } else if (w2[0].getWert()== w2[1].getWert() && w2[1].getWert()==w2[2].getWert()){
            return -30;
        } else if (w2[0].getWert() == w2[1].getWert()+1 && w2[1].getWert() == w2[2].getWert()+1 ){
            return -20;
        } else {
            return w1[0].getWert()-w2[0].getWert();
        }
    }
}
