import Datenbank.Datenbank;
import gui.GUI;
import Grafik.Grafik;
import gui.SpielerPanel;
import netzwerk.Client;
import spiel.Runde;
import spiel.Spieler;
import spiel.Stock;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by U.F.O. on 23.05.2016.
 *
 * @author  KNapret(Uwe) / DFleuren(Flurry) / EHampel(Opa)
 */
public class Main {
    //testAttribute
    static List<Spieler> teilnehmer = new ArrayList<>();


    public static void main(String[] args) {






        Datenbank.dbErstellen();
        new GUI();
//
//
//        try {
//            teilnehmer.add(Datenbank.getInstance().selectSpieler("Jochen"));
//            teilnehmer.add(Datenbank.getInstance().selectSpieler("uwe"));
//            teilnehmer.add(Datenbank.getInstance().selectSpieler("Alex"));
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
////        teilnehmer.add(new Spieler("Alex", Grafik.AVATAR_C3PO));
////        teilnehmer.add(new Spieler("Alex", Grafik.AVATAR_C3PO));
////        teilnehmer.add(new Spieler("Alex", Grafik.AVATAR_C3PO));
//
//
//        for(Spieler s : teilnehmer) {
//            s.beginnerWuerfeln();
//            s.wuerfeln();
//            s.getBecher().resetWurf();
//        }
//
//        Collections.sort(teilnehmer,Spieler.START_ORDER);
//
//
//        JFrame outline = new JFrame();
//        JPanel grundPanel = new JPanel(new GridLayout(1, 8));
//
//        Runde runde = new Runde(new Stock(),null);
//        grundPanel.add(new SpielerPanel(teilnehmer.get(0),runde));
//        grundPanel.add(new SpielerPanel(teilnehmer.get(1),runde));
//        grundPanel.add(new SpielerPanel(teilnehmer.get(2),runde));
////        grundPanel.add(new SpielerPanel(teilnehmer.get(3),runde));
////        grundPanel.add(new SpielerPanel(teilnehmer.get(4),runde));
////        grundPanel.add(new SpielerPanel(teilnehmer.get(5),runde));
//
//
//
//        runde.setTeilnehmer(teilnehmer);
//
//
//
//        outline.add(grundPanel);
//        outline.setVisible(true);
//        outline.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        outline.setSize(new Dimension(1024, 768));
    }

}
