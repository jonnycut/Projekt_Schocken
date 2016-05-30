package gui;

import Datenbank.Datenbank;
import spiel.Haelfte;
import spiel.Spieler;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

/**
 * Created by Flurry on 25.05.2016.
 */
public class Spielfeld extends JPanel {

    private GUI gui;
    private Haelfte haelfte;
    private List<SpielerPanel> teilnehmer;

    private JPanel jPOben;
    private JPanel jPMitte;
    private JPanel jPUnten;

    private JPanel jPStock;
    private JLabel jLStock;


    public Spielfeld(GUI gui){
        super();
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        this.gui = gui;
        haelfte = new Haelfte();
        teilnehmer = new ArrayList<>();

        jPOben = new JPanel(new FlowLayout());
        jPOben.setPreferredSize(new Dimension(1020, 150));
        jPOben.setBackground(Color.BLACK);

        jPStock = new JPanel();
        jPStock.setLayout(new BoxLayout(jPStock, BoxLayout.Y_AXIS));
        jPStock.setPreferredSize(new Dimension(180, 140));
        jPStock.setBackground(Color.DARK_GRAY);
        JPanel jPText = new JPanel(new FlowLayout());
        jPText.setBackground(Color.DARK_GRAY);
        JLabel jLText = new JLabel("Strafpunkte auf dem Stock");
        jLText.setForeground(Color.WHITE);
        jPText.add(jLText);
        jPStock.add(jPText);


        jLStock = new JLabel("13");
        jLStock.setFont(new Font("Arial", Font.BOLD, 50));
        jLStock.setForeground(Color.WHITE);
        jPStock.add(jLStock);

        jPOben.add(jPStock);

        jPMitte = new JPanel(new FlowLayout());
        jPMitte.setPreferredSize(new Dimension(1024,50));
        jPMitte.setBackground(Color.BLACK);
        jPMitte.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        JButton jBStart = new JButton("Starte Spiel");
        jBStart.setEnabled(true);

        ActionListener startButton = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                jPMitte.setVisible(false);
            }
        };

        jBStart.addActionListener(startButton);

        jPMitte.add(jBStart);



        jPUnten = new JPanel(new FlowLayout());
        jPUnten.setPreferredSize(new Dimension(1024, 535));
        jPUnten.setBackground(Color.DARK_GRAY);



        add(jPOben, BorderLayout.NORTH);
        add(jPMitte, BorderLayout.CENTER);
        add(jPUnten, BorderLayout.SOUTH);
    }



    public void updateStock() {

        jLStock.setText("" + haelfte.getStock().getStrafpunkte());
        jPStock.add(jLStock);
        jPOben.add(jPStock);

        updateView();
    }

    public void updateInfo(JPanel infobereich) {

        jPOben.add(infobereich);
        jPOben.add(jPStock);

        updateView();
    }

    public void setSpielerPanel(){

    List<Spieler> spielerList = gui.getAlleSpieler();

        for(Spieler s : spielerList){
            SpielerPanel x = new SpielerPanel(s,haelfte.getRunde(),this);
            teilnehmer.add(x);
            jPUnten.add(x);
        }

        updateView();
        updateTeilnehmerListe();
    }

    public void updateTeilnehmerListe(){
        try {

            List<String> kennungListe = Datenbank.getInstance().selectSpielerImSpiel(1);

            for (String s : kennungListe){
                SpielerPanel tmpPanel = new SpielerPanel(Datenbank.getInstance().selectSpieler(s),haelfte.getRunde(),this);
                teilnehmer.add(tmpPanel);
                jPUnten.add(tmpPanel);
            }

            updateView();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Spieler> spielerListeTmp = new ArrayList<>();

        for(SpielerPanel s : teilnehmer){
            spielerListeTmp.add(s.getSpieler());

        }

        haelfte.getRunde().setTeilnehmer(spielerListeTmp);
    }

    private void updateView(){

        add(jPOben, BorderLayout.NORTH);
        add(jPMitte, BorderLayout.CENTER);
        add(jPUnten, BorderLayout.SOUTH);
    }

    /**
     * 
     */
    public void pruefeFertig(){
        int counter = 0;
        for(SpielerPanel s: teilnehmer){
            if(s.getSpieler().getFertig())
                counter++;
        }

        if(counter == teilnehmer.size()){
            for(SpielerPanel s : teilnehmer)
                s.changeView("wuerfel");

            haelfte.getRunde().auswertenBilder();
        }
    }

    public GUI getGui() {
        return gui;
    }

}
