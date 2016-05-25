package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Flurry on 25.05.2016.
 */
public class Spielfeld extends JPanel {

    private JPanel anzeige;
    private JPanel spieler;

    public Spielfeld(GUI gui){
        super();
        setLayout(new BorderLayout());
        anzeige = new JPanel();
        anzeige.setPreferredSize(new Dimension(1024,155));
        anzeige.setBackground(Color.BLACK);
        spieler = new JPanel(new FlowLayout());
        spieler.setPreferredSize(new Dimension(1024,613));
        spieler.setBackground(Color.DARK_GRAY);

        add(anzeige, BorderLayout.NORTH);
        add(spieler, BorderLayout.SOUTH);
    }



    public void setAnzeige(JPanel anzeige) {
        this.anzeige = anzeige;
    }
}
