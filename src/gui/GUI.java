package gui;

import Grafik.Grafik;
import netzwerk.Client;
import netzwerk.Server;
import spiel.SpielerPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class GUI extends JFrame{

    private int zustand = 1;
    private SpielerPanel[] spieler = new SpielerPanel[8];
    private Infobereich infobereich = new Infobereich();
    private Anmeldung anmeldung = new Anmeldung(this);
    private Registrierung registrierung = new Registrierung(this);
    private Administration administration = new Administration(this);
    //private Statistik statistik = new Statistik();
    private JPanel jp = new JPanel(new CardLayout());
    private Client client;
    private Server server;


    public GUI(){

        super("Schocken, das Würfelspiel für zwischendurch!");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 768);
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

        //jp.add(administrationP, "Administration");
        jp.add(start,"Start");
        jp.add(anmeldungP, "Anmeldung");
        jp.add(registrierungP, "Registrierung");
        jp.add(administrationP, "Administration");

        add(jp);

        pack();
        setVisible(true);
    }


    public void updateView(ActionEvent e){

        switch (zustand){

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

        }
    }


    public void setServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }



    public SpielerPanel getSpieler(){

        SpielerPanel spieler = null;

        return spieler;
    }


    public SpielerPanel[] getAlleSpieler(){

        return this.spieler;
    }


    public void startPosition(){

    }

    public void setZustand(int zustand) {
        this.zustand = zustand;
    }
}
