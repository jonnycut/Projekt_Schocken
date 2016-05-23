package gui;

import spiel.SpielerPanel;

import javax.swing.*;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class GUI extends JFrame{

    private SpielerPanel[] spieler = new SpielerPanel[];
    private Infobereich infobereich = new Infobereich;
    private Anmeldung anmeldung = new Anmeldung;
    private Registrierung registrierung = new Registrierung;
    private Statisktik statistik = new Statistik;
    private Client client = new Client();
    private Server server = new Server();



}
