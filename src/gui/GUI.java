package gui;

import Grafik.Grafik;
import spiel.SpielerPanel;

import javax.swing.*;
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
    private Anmeldung anmeldung = new Anmeldung();
    private Registrierung registrierung = new Registrierung();
    private Statistik statistik = new Statistik();
//    private Client client = new Client("");
//    private Server server = new Server();


    public GUI(){

        super("Schocken");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);

        JPanel jp = new JPanel();

        JPanel start = new JPanel(new BorderLayout());
        ActionListener acl = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateView(e, jp);
            }
        };
        JButton jb = new JButton();
        jb.addActionListener(acl);
        jb.setIcon(Grafik.TISCH_LOGO);
        start.add(jb, BorderLayout.CENTER);

        //jp.add(anmeldung);
        jp.add(registrierung);


        add(jp);
        setVisible(true);

    }


    public void updateView(ActionEvent e, JPanel jp){

        switch (zustand){

            case 1:
                ((CardLayout) jp.getLayout()).show(jp, "Anmeldung");
                zustand = 2;
                updateView(null,null);
                break;
            case 2:
                break;
        }


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
}
