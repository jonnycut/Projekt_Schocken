package gui;

import Datenbank.Datenbank;
import spiel.Haelfte;
import spiel.Spieler;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

/**
 * Created by U.F.O on 25.05.2016.
 *
 * @author DFleuren
 */
public class Spielfeld extends JPanel {


    private GUI gui;
    private Infobereich infobereich;
    private Haelfte haelfte;
    private List<SpielerPanel> teilnehmer;

    private JPanel jPOben; // BorderLayout.NORTH beinhaltet den Spielstart Button
    private JPanel jPMitte; // BoderLayout.CENTER beinhaltet den Infobereich
    private JPanel jPUnten; // BorderLayout.SOUUTH beinhaltet die SpielerPanel

    private JPanel jPStock; //
    private JLabel jLStock;


    public Spielfeld(GUI gui) {
        super();
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        this.gui = gui;
        infobereich = new Infobereich(this);
        haelfte = new Haelfte();
        teilnehmer = new ArrayList<>();

        jPOben = new JPanel(new FlowLayout());
        //jPOben.setPreferredSize(new Dimension(1024, 50));
        jPOben.setBackground(Color.BLACK);
        jPOben.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        JButton jBStart = new JButton("Starte Spiel");
        jBStart.setEnabled(false);

        ActionListener starteSpielButton = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                jPOben.setVisible(false);
                infobereich.setPreferredSize(new Dimension(810, 200));
                try {
                    System.out.println("Spielfeld: Setze aktiv");
                    Datenbank.getInstance().updateAktiv(gui.getBesitzerName());
                    System.out.println("Spielfeld: Habe aktiv gesetzt");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                gui.sendeUpdateSignal(" Das Spiel wurde gestartet und es wir der Beginner ausgewürfelt");


                try {
                    Datenbank.getInstance().updateSpielstatus(Datenbank.getInstance().selectOffenesSpiel(), 2);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        };

        jBStart.addActionListener(starteSpielButton);

        try {
            if (Datenbank.getInstance().selectServerIP() == null) {
                jPOben.add(jBStart);
            } else {
                InetAddress ia[] = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());

                String ip = ia[1].getHostAddress();
                if (Datenbank.getInstance().selectServerIP().equals(ip)) {
                    jPOben.add(jBStart);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        jPMitte = new JPanel(new FlowLayout());
        jPMitte.setPreferredSize(new Dimension(1020, 150));
        jPMitte.setBackground(Color.DARK_GRAY);


        jPStock = new JPanel(new BorderLayout());
        jPStock.setPreferredSize(new Dimension(180, 140));
        jPStock.setBackground(Color.DARK_GRAY);
        JPanel jPText = new JPanel(new FlowLayout());
        jPText.setBackground(Color.DARK_GRAY);
        JLabel jLText = new JLabel("Strafpunkte auf dem Stock");
        jLText.setForeground(Color.WHITE);
        jPText.add(jLText);
        jPStock.add(jPText, BorderLayout.NORTH);

        jLStock = new JLabel();
        jLStock.setFont(new Font("Arial", Font.BOLD, 50));
        jLStock.setForeground(Color.WHITE);
        jPStock.add(jLStock, BorderLayout.CENTER);
        istSpielleiter();

        jPMitte.add(infobereich);
        jPMitte.add(jPStock);


        jPUnten = new JPanel(new FlowLayout());
        jPUnten.setPreferredSize(new Dimension(1024, 535));
        jPUnten.setBackground(Color.DARK_GRAY);
        jPUnten.setBorder(BorderFactory.createMatteBorder(8, 0, 0, 0, Color.BLACK));

        infobereich.setInfos("Herzlich Willkommen beim SCHOCKEN");
        updateInfo(infobereich);
        updateStock();

        add(jPOben, BorderLayout.NORTH);
        add(jPMitte, BorderLayout.CENTER);
        add(jPUnten, BorderLayout.SOUTH);
    }

    public void istSpielleiter() {
        int spielID = 0;
        try {
            spielID = Datenbank.getInstance().selectSpielID(gui.getBesitzerName());
            System.out.println(spielID);
            JLabel spielleiter = new JLabel("Spielleiter ist:  " + Datenbank.getInstance().selectSpielleiterKennung(spielID));
            spielleiter.setForeground(Color.RED);
            spielleiter.setBackground(Color.BLACK);
            jPStock.add(spielleiter,BorderLayout.SOUTH);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void updateView() {

        remove(jPOben);
        remove(jPMitte);
        remove(jPUnten);
        add(jPOben, BorderLayout.NORTH);
        add(jPMitte, BorderLayout.CENTER);
        add(jPUnten, BorderLayout.SOUTH);
        repaint();
    }

    public void updateStock() {

        jLStock.setText("" + haelfte.getStock().getStrafpunkte());
        JPanel jPCenter = new JPanel(new FlowLayout());
        jPCenter.setBackground(Color.DARK_GRAY);
        jPCenter.add(jLStock);
        jPStock.add(jPCenter);
        jPMitte.add(jPStock);

        updateView();
    }

    public void updateInfo(Infobereich infobereich) {

        jPMitte.remove(this.infobereich);
        jPMitte.add(infobereich);
        jPMitte.add(jPStock);
        jPMitte.revalidate();
        updateStock();
        updateView();
    }


    public void updateTeilnehmerListe() {
        teilnehmer = new ArrayList<>();

        jPUnten.removeAll();
        try {

            List<String> kennungListe = Datenbank.getInstance().selectSpielerImSpiel(Datenbank.getInstance().selectOffenesSpiel());

            for (String s : kennungListe) {
                SpielerPanel tmpPanel = new SpielerPanel(Datenbank.getInstance().selectSpieler(s), this);

                if(s.equals(gui.getBesitzerName())){
                    tmpPanel.getBecher().setEnabled(true);
                    tmpPanel.setBecher(tmpPanel.getBecher());
                }
                teilnehmer.add(tmpPanel);
                jPUnten.add(tmpPanel);
            }

            if(teilnehmer.size() >=2 ){
                if(jPOben.getComponents().length!=0)
                    jPOben.getComponent(0).setEnabled(true);
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<Spieler> spielerListeTmp = new ArrayList<>();

        for (SpielerPanel s : teilnehmer) {
            spielerListeTmp.add(s.getSpieler());
        }

        haelfte.getRunde().setTeilnehmer(spielerListeTmp);

        updateView();
    }


    public void netzwerkUpdate(String info){

        updateTeilnehmerListe();

        infobereich.setInfos(info);
        updateInfo(infobereich);
        updateStock();
        updateView();

        System.out.println("recieved updateSignal...");
    }



    /**
     * <pre>
     * Wird vom SpielerPanel aus aufgerufen und prueft alle Spieler, ob sie fertig sind.
     * Wenn alle Spieler fertig sind, wird die Ansicht des Wuerfelfeldes aufgedeckt (wuerfel angezeigt)
     * und Runde.Auswerten() wertet die Bilder aus und verteilt die Chips.
     * </pre>
     *
     * @see spiel.Runde
     */
    public void pruefeFertig() {
        int counter = 0;
        for (SpielerPanel s : teilnehmer) {
            if (s.getSpieler().getFertig())
                counter++;
        }

        if (counter == teilnehmer.size()) {
            for (SpielerPanel s : teilnehmer)
                s.changeView("wuerfel");

            haelfte.getRunde().auswertenBilder();
        }
    }

    public List<SpielerPanel> getTeilnehmer() {
        return teilnehmer;
    }



    public GUI getGui() {
        return gui;
    }
}
