import grafik.Grafik;
import gui.GUI;
import spiel.Spieler;
import spiel.SpielerPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class Main {

    public static void main(String[] args) {
        //new GUI();

        List <Spieler> teilnehmer = new ArrayList<>();

        teilnehmer.add(new Spieler("jochen", Grafik.AVATAR_BATMAN));
        teilnehmer.add(new Spieler("Hans", Grafik.AVATAR_BB8));
        teilnehmer.add(new Spieler("Alex", Grafik.AVATAR_C3PO));

        JFrame outline = new JFrame();
        outline.add(new SpielerPanel(teilnehmer.get(0)));
        outline.add(new SpielerPanel(teilnehmer.get(1)));
        outline.add(new SpielerPanel(teilnehmer.get(2)));
        outline.pack();

        outline.setVisible(true);
    }
}
