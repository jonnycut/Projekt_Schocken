package gui;

import Grafik.Grafik;
import netzwerk.Client;
import netzwerk.Netzwerk;
import netzwerk.Server;
import spiel.Spieler;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class GUI extends JFrame {

    private int zustand = 1;
    private JPanel jp = new JPanel(new CardLayout());

    private List<Spieler> spieler = new ArrayList<>();
    private Anmeldung anmeldung;
    private Registrierung registrierung;
    private Administration administration;
    //private Statistik statistik = new Statistik();
    private Spielfeld spielfeld;
    private Netzwerk netzwerk;



    public GUI() {

        super("Schocken, das Würfelspiel für zwischendurch!");

        spielfeld = new Spielfeld(this);
        anmeldung = new Anmeldung(this);
        registrierung = new Registrierung(this);
        administration = new Administration(this);

        netzwerk = new Netzwerk();


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel start = new JPanel(new BorderLayout());

        JButton jb = new JButton();
        jb.setBackground(Color.BLACK);
        jb.setBorder(new LineBorder(null));

        ActionListener acl = new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                updateView(e);
            }
        };

        jb.addActionListener(acl);
        jb.setIcon(Grafik.TISCH_LOGO);
        start.add(jb, BorderLayout.CENTER);

        JPanel anmeldungP = anmeldung;
        JPanel registrierungP = registrierung;
        JPanel administrationP = administration;
        JPanel spielfeldP = spielfeld;


        jp.add(start, "Start");
        jp.add(anmeldungP, "Anmeldung");
        jp.add(registrierungP, "Registrierung");
        jp.add(administrationP, "Administration");
        jp.add(spielfeldP, "Spielfeld");

        add(jp);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void updateView(ActionEvent e) {

        switch (zustand) {

            case 1:
                ((CardLayout) jp.getLayout()).show(jp, "Anmeldung");
                break;
            case 2:
                //ToDo: Dinge die in der Anmeldung stattfinden
                System.out.println("Test Anmeldung");
                break;
            case 3:
                ((CardLayout) jp.getLayout()).show(jp, "Registrierung");
                zustand = 4;
                break;
            case 4:
                //ToDo: Dinge die bei Registrierung laufen
                break;
            case 5:
                ((CardLayout) jp.getLayout()).show(jp, "Administration");
                break;
            case 6:
                spielfeld.setSpielerPanel();
                jp.add(spielfeld, "Spielfeld");
                ((CardLayout) jp.getLayout()).show(jp, "Spielfeld");
                break;

        }
    }


    public void setServer(Server server) {

        this.server = server;
    }

    public Server getServer() {

        return server;
    }

    public Spielfeld getSpielfeld() {

        return spielfeld;
    }

    public void setSpielfeld(Spielfeld spielfeld) {

        this.spielfeld = spielfeld;
    }

    public void updateSpielerListe(Spieler spieler) {

        this.spieler.add(spieler);
    }


    public SpielerPanel getSpieler() {

        SpielerPanel spieler = null;

        return spieler;
    }


    public List<Spieler> getAlleSpieler() {

        return this.spieler;
    }


    public void startPosition() {

    }

    public void setZustand(int zustand) {

        this.zustand = zustand;
    }

}
