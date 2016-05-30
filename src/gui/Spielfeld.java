package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Flurry on 25.05.2016.
 */
public class Spielfeld extends JPanel {

    private JPanel anzeige;
    private JPanel stock;
    private JPanel spieler;

    public Spielfeld(GUI gui){
        super();
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        anzeige = new JPanel(new FlowLayout());
        anzeige.setPreferredSize(new Dimension(1024, 155));
        anzeige.setBackground(Color.BLACK);

        stock = new JPanel();
        stock.setLayout(new BoxLayout(stock, BoxLayout.Y_AXIS));
        stock.setPreferredSize(new Dimension(250, 145));
        stock.setBackground(Color.DARK_GRAY);

        spieler = new JPanel(new FlowLayout());
        spieler.setPreferredSize(new Dimension(1024,613));
        spieler.setBackground(Color.DARK_GRAY);

        add(anzeige, BorderLayout.NORTH);
        add(spieler, BorderLayout.SOUTH);
    }



    public void setAnzeige(JPanel infobereich, int zahl) {

        JPanel jPStock = new JPanel(new FlowLayout());
        jPStock.setBackground(Color.DARK_GRAY);
        JLabel jLStock = new JLabel("Strafpunkte auf dem Stock");
        jLStock.setForeground(Color.WHITE);
        jPStock.add(jLStock);

        JPanel jPAnzahl = new JPanel(new FlowLayout());
        jPAnzahl.setBackground(Color.DARK_GRAY);
        JLabel jLAnzahl = new JLabel(""+zahl);
        jLAnzahl.setFont(new Font("Arial", Font.BOLD, 50));
        jLAnzahl.setForeground(Color.WHITE);
        jPAnzahl.add(jLAnzahl);
        this.stock.remove(jPStock);
        this.stock.remove(jPAnzahl);
        this.stock.add(jPStock);
        this.stock.add(jPAnzahl);

        this.anzeige.add(infobereich);
        this.anzeige.add(stock);

        this.add(anzeige, BorderLayout.NORTH);
    }

    public void setSpielerpanel(JPanel Spielerpanel){

    }
}
