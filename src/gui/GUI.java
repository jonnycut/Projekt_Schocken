package gui;

import Grafik.Grafik;
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
    private Statistik statistik = new Statistik();
    private JPanel jp = new JPanel(new CardLayout());
//    private Client client = new Client("");
//    private Server server = new Server();


    public GUI(){

        super("Schocken, das Würfelspiel für zwischendurch!");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);

        //JPanel jp = new JPanel(new CardLayout());

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

        jp.add(start,"Start");
        jp.add(anmeldungP, "Anmeldung");
        jp.add(registrierungP, "Registrierung");



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
                System.out.println("Pimmellim");
                break;
            case 3:
                ((CardLayout) jp.getLayout()).show(jp, "Registrierung");
                zustand = 3;
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

    public void setZustand(int zustand) {
        this.zustand = zustand;
    }
}
