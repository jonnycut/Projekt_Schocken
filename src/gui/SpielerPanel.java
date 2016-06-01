package gui;

import Datenbank.Datenbank;
import Grafik.Grafik;
import spiel.Runde;
import spiel.Spieler;
import spiel.Wuerfel;

import javax.smartcardio.CardChannel;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;


/**
 * Created by U.F.O. on 23.05.2016.
 *
 * <pre>
 * Spielerpanel
 *
 * Sorgt fuer die grafische Darstellung des Spielers
 * Enthaelt die noetigen Listener fuer den Spielverlauf
 * (wuerfeln, aufdecken, fertig)
 * </pre>
 *
 * @author KNapret / DFleuren
 */
public class SpielerPanel extends JPanel {

    private Runde runde; //TestAttribut


    private Spielfeld spielfeld;
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

    public SpielerPanel(Spieler spieler , Runde runde, Spielfeld spielfeld){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        if(spieler.getAktiv() == true){
            setBorder(new LineBorder(Color.RED, 3));
            buttonEnable();
        }
        else{
            setBorder(new LineBorder(Color.BLACK, 2));
        }
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(180, 530));

        this.runde = runde;
        this.spieler = spieler;
        this.spielfeld = spielfeld;


        this.auslage = new JPanel(new GridLayout(1,3));
        auslage.setBackground(Color.BLACK);

        //Platzhalter in Auslage einfuegen
        for (int i = 0; i <3 ; i++) {
            JButton auslage1 = new JButton();
            auslage1.setEnabled(false);
            auslage1.setBackground(Color.DARK_GRAY);
            auslage1.setPreferredSize(new Dimension(100,100));
            auslage1.setBorder(new LineBorder(Color.BLACK, 1));
            auslage.add(auslage1);
        }

        this.wuerfel = new JPanel(new CardLayout());

        JButton becherBtn = new JButton(Grafik.WUERFELBECHER);
        becherBtn.setBackground(Color.BLACK);
        becherBtn.setBorder(new LineBorder(Color.BLACK, 1));
        this.becher = becherBtn;
        becher.setEnabled(false);


        //Wuerfelansicht bauen
        this.wuerfelAnsicht = new JPanel(new GridLayout(1,3));
        wuerfelAnsicht.setBackground(Color.BLACK);
        wuerfelAnsicht.setBorder(new LineBorder(Color.BLACK,1));

        //WuerfelButtons mit dem entsprechenden Bild und Text versehen
        this.w1 = new JButton(spieler.getBecher().getWuerfelArray()[0].getGrafik());
        w1.setBackground(Color.LIGHT_GRAY);
        w1.setBorder(new LineBorder(Color.BLACK, 1));
        w1.setName("0");
        this.w2 = new JButton(spieler.getBecher().getWuerfelArray()[1].getGrafik());
        w2.setBackground(Color.LIGHT_GRAY);
        w2.setBorder(new LineBorder(Color.BLACK, 1));
        w2.setName("1");
        this.w3 = new JButton(spieler.getBecher().getWuerfelArray()[2].getGrafik());
        w3.setBackground(Color.LIGHT_GRAY);
        w3.setBorder(new LineBorder(Color.BLACK, 1));
        w3.setName("2");

        //WuerfelListener um einen Wuerfel vom Becher in die Auslage zu legen
        //-> Entsprechender Wuerfel wird mit leerem Button ersetzt (Design)

        ActionListener wuerfelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton pufferBtn = (JButton) e.getSource();
                int btnIndex = Integer.parseInt(pufferBtn.getName());

                //Wegen Mehrfachnutzung in Methode ausgelagert
                wuerfelRaus(pufferBtn,btnIndex,this);


            }



        };

        w1.addActionListener(wuerfelListener);
        w2.addActionListener(wuerfelListener);
        w3.addActionListener(wuerfelListener);

        //WuerfelButtons in die Ansicht einfuegen

        wuerfelAnsicht.add(w1, 0);
        wuerfelAnsicht.add(w2, 1);
        wuerfelAnsicht.add(w3, 2);



        wuerfel.add(becher,"becher");
        wuerfel.add(wuerfelAnsicht, "wuerfel");


        this.strafpunkte = new JPanel();
        strafpunkte.setLayout(new FlowLayout());
        strafpunkte.setBackground(Color.DARK_GRAY);

        JLabel jLStrafpunkteTitel = new JLabel("Strafpunkte:  ");
        jLStrafpunkteTitel.setForeground(Color.RED);
        strafpunkte.add(jLStrafpunkteTitel);

        JLabel jLStrafpunkte = new JLabel(""+this.spieler.getStrafpunkte());
        jLStrafpunkte.setForeground(Color.WHITE);
        strafpunkte.add(jLStrafpunkte);

        this.buttons = new JPanel();
        buttons.setBackground(Color.BLACK);
        this.wuerfeln = new JButton("WÜRFELN");
        wuerfeln.setEnabled(false);
        this.fertig = new JButton("FERTIG");
        fertig.setEnabled(false);

        becher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (spieler.getBecher().getWurf() < 2) {
                    //ToDo: MaxWuerfe anhand des Beginners begrenzen
                    ((CardLayout) wuerfel.getLayout()).show(wuerfel, "wuerfel");
                    System.out.println(spieler.getLetztesBild());
                    System.out.println(spieler.getBecher().getWuerfel()[0]+"-"+spieler.getBecher().getWuerfel()[1]+"-"+spieler.getBecher().getWuerfel()[2]);
                    aufgedeckt = true;
                } else {

                    JOptionPane.showMessageDialog(null, "Keine Wuerfe mehr verfügbar, bitte drücken Sie Fertig");
                }

            }
        });

        wuerfeln.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (spieler.getBecher().getWurf() < 3) {
                    //ToDo: MaxWuerfe anhand des Beginners begrenzen

                    ((CardLayout) wuerfel.getLayout()).show(wuerfel, "becher");
                    aufgedeckt = false;

                    spieler.wuerfeln();
                    w1.setIcon(spieler.getBecher().getWuerfelArray()[0].getGrafik());
                    w2.setIcon(spieler.getBecher().getWuerfelArray()[1].getGrafik());
                    w3.setIcon(spieler.getBecher().getWuerfelArray()[2].getGrafik());
                    if (spieler.getBecher().getWurf() == 3)
                        wuerfeln.setEnabled(false);
                } else {

                    JOptionPane.showMessageDialog(null, "Keine Wuerfe mehr verfügbar, bitte drücken Sie Fertig");

                }


            }
        });

        fertig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println(spieler.getLetztesBild());
                spieler.setFertig(true);
                wuerfeln.setEnabled(false);
                fertig.setEnabled(false);
                if(aufgedeckt == true){
                   for(int i=0; i<3;i++){

                    if(!wuerfelAnsicht.getComponent(i).getName().equals("leer")){
                        wuerfelRaus((JButton) wuerfelAnsicht.getComponent(i), i, wuerfelListener);
                    }

                   }
                }

                try {
                    Datenbank.getInstance().insertStatistik(spieler.getName(), spieler.getStatistik());
                    Datenbank.getInstance().selectStatistik(spieler.getName());
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }

                spielfeld.pruefeFertig();




            }
        });

        buttons.add(wuerfeln);
        buttons.add(fertig);

        this.name = new JPanel(new FlowLayout());
        name.setBackground(Color.DARK_GRAY);

        JLabel jLSpielerName = new JLabel(this.spieler.getName());
        jLSpielerName.setForeground(Color.WHITE);

        name.add(jLSpielerName);


        this.profilbild = new JPanel();
        profilbild.setLayout(new BoxLayout(profilbild, BoxLayout.Y_AXIS));
        profilbild.setBackground(Color.DARK_GRAY);

        JPanel jPProfilbild = new JPanel(new FlowLayout());
        JLabel jLProfilbild = new JLabel(spieler.getProfilBild());
        jPProfilbild.setBackground(Color.DARK_GRAY);
        jPProfilbild.add(jLProfilbild);

        profilbild.add(name);
        profilbild.add(jPProfilbild);
        profilbild.add(new JLabel());

        JPanel jPTemp = new JPanel();
        jPTemp.setPreferredSize(new Dimension(0, 20));
        jPTemp.setBackground(Color.DARK_GRAY);

        JPanel jPTemp1 = new JPanel();
        jPTemp1.setPreferredSize(new Dimension(0, 20));
        jPTemp1.setBackground(Color.DARK_GRAY);

        JPanel jPBox = new JPanel();
        jPBox.setLayout(new BoxLayout(jPBox, BoxLayout.Y_AXIS));
        jPBox.setBackground(Color.DARK_GRAY);
        jPBox.setBorder(new LineBorder(Color.BLACK, 2));

        jPBox.add(strafpunkte);
        jPBox.add(jPTemp);
        jPBox.add(profilbild);
        jPBox.add(jPTemp1);

        add(auslage);
        add(wuerfel);
        add(jPBox);
        add(buttons);









    }

    public Spieler getSpieler() {
        return spieler;
    }

    /**
     * Aktualisiert das SpielerPanel
     * Loescht die Wuerfel in der Auslage und im Wuerfelbereich, sowie die Strafpunkte
     * und schreibt diese neu.
     */


    public void updatePanel(){

        strafpunkte.removeAll();
        strafpunkte.add(new JLabel("" + this.spieler.getStrafpunkte()));
        this.revalidate();
        Wuerfel[] wuerfelArray = spieler.getBecher().getWuerfelArray();
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

    /**
     * Wechselt die Ansicht des Wuerfelfeldes
     * @param view String {wuerfel | becher}
     * @throws IllegalArgumentException
     */
    public void changeView(String view) throws IllegalArgumentException{
        if(view.equals("wuerfel")||wuerfel.equals("becher"))
            ((CardLayout) wuerfel.getLayout()).show(wuerfel,view);
        else
            throw new IllegalArgumentException("Argument darf nur {wuerfel || becher} sein");
            return;
    }

    /**
     *
     * <pre>
     * Legt die Wuerfel vom Wuerfelbereich in die Auslage.
     * Setzt das Attribut {draussen} auf true, damit diese Wuerfel nicht mehr gewuerfelt werden
     * und entfernt den Action Listener
     *
     * </pre>
     * @param pufferBtn JButton - Der Button, der bewegt wird
     * @param btnIndex int - an welche Stelle der Button gelegt wird
     * @param wuerfelListener - ActionListener - Damit die Wuerfel nicht mehr angeklickt werden koennen
     * @see JButton# MTHODE(Comparable)
     * @see ActionListener
     *
     */

    public void wuerfelRaus(JButton pufferBtn, int btnIndex, ActionListener wuerfelListener){

        auslage.remove(btnIndex);
        auslage.add(pufferBtn, btnIndex);

        wuerfelAnsicht.remove(pufferBtn);
        pufferBtn.removeActionListener(wuerfelListener);

        JButton pufferBtn2 = new JButton();
        pufferBtn2.setName("leer");
        pufferBtn2.setBackground(Color.BLACK);
        pufferBtn2.setBorder(new LineBorder(Color.BLACK,1));
        wuerfelAnsicht.add(pufferBtn2, btnIndex);
        wuerfelAnsicht.revalidate();

        //Rausgelegten wuerfel auf draussen= true, damit dieser nicht mehr gewuerfelt wird

        if(pufferBtn.equals(w1)){
            spieler.getBecher().getWuerfelArray()[0].setDraussen(true);

        }else if(pufferBtn.equals(w2)){
            spieler.getBecher().getWuerfelArray()[1].setDraussen(true);
        }else{
            spieler.getBecher().getWuerfelArray()[2].setDraussen(true);
        }
        spieler.getBecher().sortiere();
        auslageCount++;


        if(auslageCount==3)
            wuerfeln.setEnabled(false);

    }

    public void buttonEnable(){
        becher.setEnabled(true);
        wuerfeln.setEnabled(true);
        fertig.setEnabled(true);
    }

    public JButton getBecher() {
        return becher;
    }

    public void setBecher(JButton becher) {
        this.becher = becher;
    }


}
