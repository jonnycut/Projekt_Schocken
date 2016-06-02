package gui;



import Grafik.Grafik;
import javax.swing.*;
import java.awt.*;


/**
 * Created by U.F.O. on 23.05.2016.
 *
 * @author EHampel / DFleuren
 */
public class Statistik extends JPanel {

    private String spielername = "";
    private int gewonneneRunden;
    private int verloreneHaelften;
    private int verloreneSpiele;
    private int anzahlSchock;
    private int anzahlStrasse;
    private int anzahlGenaral;
    private int anzahlEinfacherWurf;
    private int anzahlSchockAus;


    public Statistik(GUI gui) {

        //-------------------------------------GRUND-PANEL-------------------------------------------------------------

        JPanel jp = new JPanel(new BorderLayout());
        jp.setBackground(Color.BLACK);


        //-------------------------------------OBEN-PANEL--------------------------------------------------------------

        JPanel oben = new JPanel();
        oben.setBackground(Color.BLACK);
        JLabel jLoben = new JLabel("Statistik von .." + spielername + "..");
        jLoben.setForeground(Color.WHITE);
        oben.add(jLoben);

        jp.add(oben, BorderLayout.NORTH);


        //-------------------------------------MITTE-PANEL-------------------------------------------------------------

        JPanel mitte = new JPanel();
        mitte.setBackground(Color.DARK_GRAY);
        JLabel jLMitte = new JLabel(Grafik.LOGO1);
        mitte.add(jLMitte);

        jp.add(mitte, BorderLayout.CENTER);


        //-------------------------------------RECHTS-PANEL------------------------------------------------------------

        JPanel rechts = new JPanel();
        rechts.setPreferredSize(new Dimension(240, 0));
        rechts.setBackground(Color.BLACK);

        jp.add(rechts, BorderLayout.EAST);


        //-------------------------------------UNTEN-PANEL-------------------------------------------------------------
        JPanel unten = new JPanel(new GridLayout(1, 3));
        unten.setBackground(Color.BLACK);

        JPanel jPProjekt = new JPanel();
        jPProjekt.setBackground(Color.BLACK);
        JLabel jLProjekt = new JLabel("Projekt: SCHOCKEN");
        jLProjekt.setForeground(Color.WHITE);
        jPProjekt.add(jLProjekt);

        JPanel jPGruppe = new JPanel();
        jPGruppe.setBackground(Color.BLACK);
        JLabel jLPGruppe = new JLabel("Projektgruppe: U.F.O");
        jLPGruppe.setForeground(Color.WHITE);
        jPGruppe.add(jLPGruppe);

        JPanel jPFsbw = new JPanel(new FlowLayout());
        jPFsbw.setBackground(Color.BLACK);
        JLabel jLFsbw = new JLabel("FSBwIT 2016");
        jLFsbw.setForeground(Color.WHITE);
        jPFsbw.add(jLFsbw);

        unten.add(jPProjekt);
        unten.add(jPGruppe);
        unten.add(jPFsbw);

        jp.add(unten, BorderLayout.SOUTH);


        //-------------------------------------SUPER-PANEL-------------------------------------------------------------

        add(jp, BorderLayout.CENTER);
    }

    //------------------------------------------METHODEN---------------------------------------------------------------

    public Statistik updateStatistik(String spieler){

        return this;
    }



    //        JPanel north = new JPanel(new BorderLayout(1, 2));
//        north.setPreferredSize(new Dimension(300, 50));
//        JLabel jLname = new JLabel(spieler.getName());
//        JLabel jLBild = new JLabel(spieler.getProfilBild());
//        north.add(jLname);
//        north.add(jLBild);
//        add(north, BorderLayout.NORTH);
//
//
//        JPanel center = new JPanel(new GridLayout(8, 2));
//        JLabel jLgRunden = new JLabel("gewonnene Runden");
//        center.add(jLgRunden);
//        center.add(new JLabel());

}

















