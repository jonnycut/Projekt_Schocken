package gui;

import Grafik.Grafik;
import spiel.SpielerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class GUI extends JFrame{

    private int zustand = 1;
    private SpielerPanel[] spieler = new SpielerPanel[8];
    private Infobereich infobereich = new Infobereich();
    private Anmeldung anmeldung = new Anmeldung();
    private Registrierung registrierung = new Registrierung();
    private Statisktik statistik = new Statistik();
    private Client client = new Client();
    private Server server = new Server();


    public GUI(){

        super("Schocken");

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);

        JPanel jp = new JPanel(new CardLayout());
        JLabel jl = new JLabel();
        jl.setIcon(Grafik.TISCH_LOGO);
        jp.add(jl);




        setVisible(true);

    }


    public void updateView(ActionEvent e){

        switch (zustand){


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
