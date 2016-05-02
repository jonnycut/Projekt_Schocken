package spiel;

/**
 * Created by KNapret on 02.05.2016.
 */
public class Spieler {

    private Becher becher;
    private int chips;
    private boolean admin;
    //private Grafik profilbild;
    private String lastBild;

    public  Spieler(){
        this.becher = new Becher();
        this.chips = 0;
        this.admin = false;
        this.lastBild = null;


    }

    public void wuerfeln(){
        this.becher.wuerfeln();
    }


}
