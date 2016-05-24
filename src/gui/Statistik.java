package gui;



        import Datenbank.Datenbank;
        import spiel.Spieler;

        import javax.swing.*;
        import java.awt.*;

/**
 * Created by ehampel on 23.05.2016.
 */
public class Statistik extends JPanel {

    private String spielername ="";
    private int gewonneneRunden;
    private int verloreneHaelften;
    private int verloreneSpiele;
    private int anzahlSchock;
    private int anzahlStrasse;
    private int anzahlGenaral;
    private int anzahlEinfacherWurf;
    private int anzahlSchockAus;



    private Statistik(Spieler spieler) {
        super();
        spielername=spieler.getName();

        setLayout(new BorderLayout());

        JPanel north = new JPanel(new BorderLayout(1, 2));
        north.setPreferredSize(new Dimension(300, 50));
        JLabel jLname = new JLabel(spieler.getName());
        JLabel jLBild = new JLabel(spieler.getProfilBild());
        north.add(jLname);
        north.add(jLBild);
        add(north, BorderLayout.NORTH);


        JPanel center = new JPanel(new GridLayout(8, 2));
        JLabel jLgRunden = new JLabel("gewonnene Runden");
        center.add(jLgRunden);
        center.add(new JLabel());



    }}









