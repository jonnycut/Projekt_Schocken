package spiel;

import javax.swing.*;
import java.awt.*;


/**
 * Created by KNapret on 23.05.2016.
 */
public class SpielerPanel extends JPanel {

    private Spieler spieler;
    private JPanel auslage;
    private JPanel wuerfel;
    private JPanel strafpunkte;
    private JPanel buttons;
    private JButton wuerfeln;
    private JButton fertig;
    private JPanel name;
    private JPanel profilbild;

    public SpielerPanel(Spieler spieler){
        super(new GridLayout(6,1));
        this.spieler = spieler;
        this.auslage = new JPanel();
        this.wuerfel = new JPanel();
        this.strafpunkte = new JPanel();
        strafpunkte.add(new JLabel(""+this.spieler.getStrafpunkte()));

        this.buttons = new JPanel();
        this.wuerfeln = new JButton("Würfeln");
        this.fertig = new JButton("Fertig");
        buttons.add(wuerfeln);
        buttons.add(fertig);

        this.name = new JPanel();
        name.add(new JLabel(this.spieler.getName()));

        this.profilbild = new JPanel();
        profilbild.add(new JLabel(spieler.getProfilBild()));

        add(auslage);
        add(wuerfel);
        add(strafpunkte);
        add(buttons);
        add(name);
        add(profilbild);





    }


}
