package gui;

import grafik.Grafik;
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

        JPanel jp = new JPanel(new BorderLayout());
        ActionListener acl = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateView(e);
                zustand = 2;
            }
        };
        JButton jb = new JButton();
        jb.addActionListener(acl);
        jb.setIcon(Grafik.TISCH_LOGO);
        jp.add(jb,BorderLayout.CENTER);



        add(jb);
        setVisible(true);

    }


    public void updateView(ActionEvent e){

        switch (zustand){

            case 1:

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
