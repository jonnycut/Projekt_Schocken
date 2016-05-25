import Datenbank.Datenbank;
import gui.GUI;
import Grafik.Grafik;
import gui.SpielerPanel;
import spiel.Runde;
import spiel.Spieler;
import spiel.Stock;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class Main {
    //testAttribute
    static List<Spieler> teilnehmer = new ArrayList<>();


    public static void main(String[] args) {
//       new GUI();
//
//        Datenbank.dbErstellen();




        teilnehmer.add(new Spieler("jochen", Grafik.AVATAR_BATMAN));
        teilnehmer.add(new Spieler("Hans", Grafik.AVATAR_BB8));
        teilnehmer.add(new Spieler("Alex", Grafik.AVATAR_C3PO));
        teilnehmer.add(new Spieler("Alex", Grafik.AVATAR_C3PO));
        teilnehmer.add(new Spieler("Alex", Grafik.AVATAR_C3PO));
        teilnehmer.add(new Spieler("Alex", Grafik.AVATAR_C3PO));



        JFrame outline = new JFrame();
        JPanel grundPanel = new JPanel(new GridLayout(1, 8));

        Runde runde = new Runde(new Stock(),null);
        grundPanel.add(new SpielerPanel(teilnehmer.get(0),runde));
        grundPanel.add(new SpielerPanel(teilnehmer.get(1),runde));
        grundPanel.add(new SpielerPanel(teilnehmer.get(2),runde));
        grundPanel.add(new SpielerPanel(teilnehmer.get(3),runde));
        grundPanel.add(new SpielerPanel(teilnehmer.get(4),runde));
        grundPanel.add(new SpielerPanel(teilnehmer.get(5),runde));



        runde.setTeilnehmer(teilnehmer);



        outline.add(grundPanel);
        outline.setVisible(true);
        outline.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        outline.setPreferredSize(new Dimension(1024, 768));
        outline.pack();
    }

}
