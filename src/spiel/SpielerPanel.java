package spiel;

import javax.swing.*;

/**
 * Created by KNapret on 23.05.2016.
 */
public class SpielerPanel extends JPanel {
    private Becher becher;
    private int strafpunkte;
    private int haelfte;
    //private Grafik profilBild;
    private String letztesBild;
    private String name;

    public SpielerPanel(String name){
        super(/*profilBild*/);
        this.becher = new Becher();
        this.strafpunkte = 0;
        this.haelfte = 0;
        this.letztesBild = null;
        this.name = name;
    }

}
