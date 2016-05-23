package spiel;

import javax.swing.*;



/**
 * Created by KNapret on 23.05.2016.
 */
public class SpielerPanel extends JPanel {
    private Becher becher;
    private int strafpunkte;
    private int haelfte;

    private String letztesBild;
    private String name;

    public SpielerPanel(String name, Icon profilBild){
        super(/*profilBild*/);
        this.becher = new Becher();
        this.strafpunkte = 0;
        this.haelfte = 0;
        this.letztesBild = null;
        this.name = name;
    }

    public  String getLetztesBild(){
        return letztesBild;
    }

    public Becher getBecher(){
        return  this.becher;
    }

    public void wuerfeln(){
        this.becher.wuerfeln();
        this.letztesBild = this.becher.getBild();

    }

    public void popStrafpunkte(int anzahl){

        this.strafpunkte -= anzahl;

    }

    public void pushStrafpunkte(int anzahl){

        this.strafpunkte += anzahl;
    }

    public String beginnerWuerfeln(){

        this.becher.wuerfeln();
        this.letztesBild = this.becher.getBild();
        return this.letztesBild;

    }

    public int compareTo(SpielerPanel s){

        Wuerfel[] s1 = this.becher.getWuerfel();
        Wuerfel[] s2 = s.getBecher().getWuerfel();

        if(s1[1].getWert()==1 && s1[2].getWert() ==1){
            if(s2[1].getWert()==1 &&s2[2].getWert()==1){
                if(s1[0].getWert()>s2[0].getWert()){
                    return 1;
                }
            }
        }else if (s2[1].getWert()==1 && s2[2].getWert() ==1){
            return 1;
        }

return 1;
    }

}
