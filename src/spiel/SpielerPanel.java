package spiel;

import Grafik.Grafik;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by KNapret on 23.05.2016.
 *
 * <pre>
 * Spielerpanel
 *
 * Sorgt fuer die grafische Darstellung des Spielers
 * Enthaelt die noetigen Listener fuer den Spielverlauf
 * (wuerfeln, aufdecken, fertig)
 * </pre>
 */
public class SpielerPanel extends JPanel {

    private Spieler spieler;
    private JPanel auslage;
    private JPanel wuerfel;
    private JButton becher;
    private JPanel wuerfelAnsicht;
    private JButton w1;
    private JButton w2;
    private JButton w3;
    private JPanel strafpunkte;
    private JPanel buttons;
    private JButton wuerfeln;
    private JButton fertig;
    private JPanel name;
    private JPanel profilbild;

    public SpielerPanel(Spieler spieler){
        super(new GridLayout(6,1));
        this.spieler = spieler;
        this.auslage = new JPanel(new GridLayout(1,3));
        this.wuerfel = new JPanel(new CardLayout());

        this.becher = new JButton(Grafik.WUERFELBECHER);


        this.wuerfelAnsicht = new JPanel(new GridLayout(1,3));

        this.w1 = new JButton(spieler.getBecher().getWuerfel()[0].getGrafik());
        this.w2 = new JButton(spieler.getBecher().getWuerfel()[1].getGrafik());
        this.w3 = new JButton(spieler.getBecher().getWuerfel()[2].getGrafik());

        ActionListener wuerfelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auslage.add((JButton) e.getSource());

                wuerfelAnsicht.remove((JButton) e.getSource());
                ((JButton) e.getSource()).removeActionListener(this);
                wuerfelAnsicht.add(new JButton());
                wuerfelAnsicht.revalidate();

                if(e.getSource().equals(w1)){
                    spieler.getBecher().getWuerfel()[0].setDraussen(true);
                }else if(e.getSource().equals(w2)){
                    spieler.getBecher().getWuerfel()[1].setDraussen(true);
                }else{
                    spieler.getBecher().getWuerfel()[2].setDraussen(true);
                }
            }
        };

        w1.addActionListener(wuerfelListener);
        w2.addActionListener(wuerfelListener);
        w3.addActionListener(wuerfelListener);

        wuerfelAnsicht.add(w1);
        wuerfelAnsicht.add(w2);
        wuerfelAnsicht.add(w3);



        wuerfel.add(becher,"becher");
        wuerfel.add(wuerfelAnsicht,"wuerfel");


        this.strafpunkte = new JPanel();
        strafpunkte.add(new JLabel(""+this.spieler.getStrafpunkte()));

        this.buttons = new JPanel();
        this.wuerfeln = new JButton("Würfeln");
        this.fertig = new JButton("Fertig");

        becher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(spieler.getBecher().getWurf()<2){
                    //ToDo: MaxWuerfe anhand des Beginners begrenzen
                    ((CardLayout)wuerfel.getLayout()).show(wuerfel, "wuerfel");
                }else{

                    JOptionPane.showMessageDialog(null, "Keine Wuerfe mehr verfügbar, bitte drücken Sie Fertig");
                }

            }
        });

        wuerfeln.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(spieler.getBecher().getWurf() <3){
                    //ToDo: MaxWuerfe anhand des Beginners begrenzen

                    ((CardLayout)wuerfel.getLayout()).show(wuerfel,"becher");

                    spieler.getBecher().wuerfeln();
                    w1.setIcon(spieler.getBecher().getWuerfel()[0].getGrafik());
                    w2.setIcon(spieler.getBecher().getWuerfel()[1].getGrafik());
                    w3.setIcon(spieler.getBecher().getWuerfel()[2].getGrafik());
                }else{

                    JOptionPane.showMessageDialog(null,"Keine Wuerfe mehr verfügbar, bitte drücken Sie Fertig");
                }


            }
        });

        fertig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Term Signal from " + spieler);
            }
        });

        buttons.add(wuerfeln);
        buttons.add(fertig);

        this.name = new JPanel();
        name.add(new JLabel(this.spieler.getName()));

        this.profilbild = new JPanel();
        profilbild.add(new JLabel(spieler.getProfilBild()));

        add(auslage);
        add(wuerfel);
        add(strafpunkte);
        add(buttons);
        add(name);
        add(profilbild);




        /*ToDo:EventListener für die Buttons, äußerer Rahmen 1024*768*/



    }


}
