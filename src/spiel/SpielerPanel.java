package spiel;

import Datenbank.Datenbank;
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

    private Runde runde; //TestAttribut
    //ToDo: Datenhaltung und Wiederherstellung über die Datenbank
    private Spieler spieler;
    private boolean aufgedeckt = false;
    private JPanel auslage;
    private int auslageCount=0; //testzwecke, muss abgeändert werden
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

    public SpielerPanel(Spieler spieler, Runde runde){
        super(new GridLayout(6,1));

        this.runde = runde;
        this.spieler = spieler;
        this.auslage = new JPanel(new GridLayout(1,3));
        //Platzhalter in Auslage einfuegen
        auslage.add(new JButton());
        auslage.add(new JButton());
        auslage.add(new JButton());


        this.wuerfel = new JPanel(new CardLayout());

        this.becher = new JButton(Grafik.WUERFELBECHER);


        //Wuerfelansicht bauen
        this.wuerfelAnsicht = new JPanel(new GridLayout(1,3));

        //WuerfelButtons mit dem entsprechenden Bild und Text versehen
        this.w1 = new JButton(spieler.getBecher().getWuerfel()[0].getGrafik());
        w1.setText("0");
        this.w2 = new JButton(spieler.getBecher().getWuerfel()[1].getGrafik());
        w2.setText("1");
        this.w3 = new JButton(spieler.getBecher().getWuerfel()[2].getGrafik());
        w3.setText("2");

        //WuerfelListener um einen Wuerfel vom Becher in die Auslage zu legen
        //-> Entsprechender Wuerfel wird mit leerem Button ersetzt (Design)

        ActionListener wuerfelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton pufferBtn = (JButton) e.getSource();
                int btnIndex = Integer.parseInt(pufferBtn.getText());

                auslage.remove(btnIndex);
                auslage.add(pufferBtn,btnIndex);

                wuerfelAnsicht.remove(pufferBtn);
                pufferBtn.removeActionListener(this);
                wuerfelAnsicht.add(new JButton(), btnIndex);
                wuerfelAnsicht.revalidate();

                //Rausgelegten wuerfel auf draussen= true, damit dieser nicht mehr gewuerfelt wird

                if(pufferBtn.equals(w1)){
                    spieler.getBecher().getWuerfel()[0].setDraussen(true);

                }else if(e.getSource().equals(w2)){
                    spieler.getBecher().getWuerfel()[1].setDraussen(true);
                }else{
                    spieler.getBecher().getWuerfel()[2].setDraussen(true);
                }
                spieler.getBecher().sortiere();
                auslageCount++;
                System.out.println(auslageCount);

                if(auslageCount==3)
                    wuerfeln.setEnabled(false);

            }



        };

        w1.addActionListener(wuerfelListener);
        w2.addActionListener(wuerfelListener);
        w3.addActionListener(wuerfelListener);

        //WuerfelButtons in die Ansicht einfuegen

        wuerfelAnsicht.add(w1,0);
        wuerfelAnsicht.add(w2,1);
        wuerfelAnsicht.add(w3,2);



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
                    aufgedeckt = true;
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

                    ((CardLayout)wuerfel.getLayout()).show(wuerfel, "becher");
                    aufgedeckt = false;

                    spieler.wuerfeln();
                    w1.setIcon(spieler.getBecher().getWuerfel()[0].getGrafik());
                    w2.setIcon(spieler.getBecher().getWuerfel()[1].getGrafik());
                    w3.setIcon(spieler.getBecher().getWuerfel()[2].getGrafik());
                    if(spieler.getBecher().getWurf()==3)
                        wuerfeln.setEnabled(false);
                }else{

                    JOptionPane.showMessageDialog(null,"Keine Wuerfe mehr verfügbar, bitte drücken Sie Fertig");

                }


            }
        });

        fertig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("Term Signal from " + spieler);
                spieler.setFertig();
                wuerfeln.setEnabled(false);
                fertig.setEnabled(false);
                if(runde.pruefeFertig()){
                    ((CardLayout)wuerfel.getLayout()).show(wuerfel, "wuerfel");
                    updatePanel();

                    //ToDo: Aufdecken aller Becher muss durch die GUI erfolgen
                }


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








    }

    /**
     * Aktualisiert das SpielerPanel
     * Loescht die Wuerfel in der Auslage und im Wuerfelbereich, sowie die Strafpunkte
     * und schreibt diese neu.
     */

    public void updatePanel(){

        strafpunkte.removeAll();
        strafpunkte.add(new JLabel("" + this.spieler.getStrafpunkte()));
        strafpunkte.revalidate();
        Wuerfel[] wuerfelArray = spieler.getBecher().getWuerfel();
        JButton[] btnArray = {w1,w2,w3};



        for(int i =0; i<3;i++){

            btnArray[i].setIcon(wuerfelArray[i].getGrafik());

            if(wuerfelArray[i].getDraussen()){
                auslage.remove(i);
                auslage.add(btnArray[i],i);

            }else{
                wuerfelAnsicht.remove(i);
                wuerfelAnsicht.add(btnArray[i], i);

            }
        }

        auslage.revalidate();
        wuerfelAnsicht.revalidate();

        if(aufgedeckt)
            ((CardLayout)wuerfel.getLayout()).show(wuerfel, "wuerfel");
        else

            ((CardLayout)wuerfel.getLayout()).show(wuerfel, "becher");

    }


}
