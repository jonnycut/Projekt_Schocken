package gui;



import Grafik.Grafik;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by U.F.O. on 23.05.2016.
 *
 * @author EHampel / DFleuren
 */
public class Statistik extends JPanel {

    private String spielername = "";
    private JLabel jLProfilbild;
    private JLabel jLName;
    private JLabel jLGewonneneRunden;
    private JLabel jLVerloreneHaelften;
    private JLabel jLVerloreneSpiele;
    private JLabel jLSchock;
    private JLabel jLStrasse;
    private JLabel jLGenaral;
    private JLabel jLEinfacherWurf;
    private JLabel jLSchockAus;


    public Statistik(GUI gui) {

        //-------------------------------------GRUND-PANEL-------------------------------------------------------------

        JPanel jp = new JPanel(new BorderLayout());
        jp.setBackground(Color.BLACK);


        //-------------------------------------OBEN-PANEL--------------------------------------------------------------

        JPanel oben = new JPanel();
        oben.setBackground(Color.BLACK);
        JLabel jLoben = new JLabel("Statistik");
        jLoben.setForeground(Color.WHITE);
        oben.add(jLoben);

        jp.add(oben, BorderLayout.NORTH);


        //-------------------------------------MITTE-PANEL-------------------------------------------------------------

        JPanel mitte = new JPanel();
        mitte.setLayout(new BoxLayout(mitte, BoxLayout.Y_AXIS));
        mitte.setBackground(Color.DARK_GRAY);
        mitte.setPreferredSize(new Dimension(1020, 600));

        JPanel jPName = new JPanel(new FlowLayout());
        jPName.setBackground(Color.DARK_GRAY);
        jLName = new JLabel("TEST NAME");
        jLName.setBackground(Color.BLACK);
        jLName.setFont(new Font("Arial", Font.BOLD, 25));
        jLName.setForeground(Color.RED);

        jPName.add(jLName);
        mitte.add(jPName);

        JPanel jPProfilbild = new JPanel(new FlowLayout());
        jPProfilbild.setBackground(Color.BLACK);
        jLProfilbild = new JLabel();
        jLProfilbild.setIcon(Grafik.AVATAR_BB8);
        jLProfilbild.setBackground(Color.WHITE);
        jLProfilbild.setPreferredSize(new Dimension(150, 150));

        jPProfilbild.add(jLProfilbild);
        mitte.add(jPProfilbild);

        JPanel jPGewonneneRunden = new JPanel(new FlowLayout());
        jPGewonneneRunden.setBackground(Color.DARK_GRAY);
        JLabel jLGew = new JLabel("Gewonnene Runden:  ");
        jLGew.setBackground(Color.BLACK);
        jLGew.setForeground(Color.WHITE);
        jLGew.setFont(new Font("Arial", Font.BOLD, 15));
        jPGewonneneRunden.add(jLGew);

        jLGewonneneRunden = new JLabel("666");
        jLGewonneneRunden.setBackground(Color.DARK_GRAY);
        jLGewonneneRunden.setForeground(Color.RED);
        jLGewonneneRunden.setFont(new Font("Arial", Font.BOLD, 15));

        jPGewonneneRunden.add(jLGewonneneRunden);
        mitte.add(jPGewonneneRunden);

        JPanel jPVerloreneHaelften = new JPanel(new FlowLayout());
        jPVerloreneHaelften.setBackground(Color.DARK_GRAY);
        JLabel jLHaelfte = new JLabel("Gewonnene Hälften:  ");
        jLHaelfte.setBackground(Color.BLACK);
        jLHaelfte.setForeground(Color.WHITE);
        jLHaelfte.setFont(new Font("Arial", Font.BOLD, 15));
        jPVerloreneHaelften.add(jLHaelfte);

        jLVerloreneHaelften = new JLabel("666");
        jLVerloreneHaelften.setBackground(Color.DARK_GRAY);
        jLVerloreneHaelften.setForeground(Color.RED);
        jLVerloreneHaelften.setFont(new Font("Arial", Font.BOLD, 15));

        jPVerloreneHaelften.add(jLVerloreneHaelften);
        mitte.add(jPVerloreneHaelften);

        JPanel jPVerloreneSpiele = new JPanel(new FlowLayout());
        jPVerloreneSpiele.setBackground(Color.DARK_GRAY);
        JLabel jLSpiele = new JLabel("Verlorene Spiele:  ");
        jLSpiele.setBackground(Color.BLACK);
        jLSpiele.setForeground(Color.WHITE);
        jLSpiele.setFont(new Font("Arial", Font.BOLD, 15));
        jPVerloreneSpiele.add(jLSpiele);

        jLVerloreneSpiele = new JLabel("666");
        jLVerloreneSpiele.setBackground(Color.DARK_GRAY);
        jLVerloreneSpiele.setForeground(Color.RED);
        jLVerloreneSpiele.setFont(new Font("Arial", Font.BOLD, 15));

        jPVerloreneSpiele.add(jLVerloreneSpiele);
        mitte.add(jPVerloreneSpiele);

        JPanel jPSchock = new JPanel(new FlowLayout());
        jPSchock.setBackground(Color.DARK_GRAY);
        JLabel jLSch = new JLabel("Anzahl Schock:  ");
        jLSch.setBackground(Color.BLACK);
        jLSch.setForeground(Color.WHITE);
        jLSch.setFont(new Font("Arial", Font.BOLD, 15));
        jPSchock.add(jLSch);

        jLSchock = new JLabel("666");
        jLSchock.setBackground(Color.DARK_GRAY);
        jLSchock.setForeground(Color.RED);
        jLSchock.setFont(new Font("Arial", Font.BOLD, 15));

        jPSchock.add(jLSchock);
        mitte.add(jPSchock);

        JPanel jPStrasse = new JPanel(new FlowLayout());
        jPStrasse.setBackground(Color.DARK_GRAY);
        JLabel jPStr = new JLabel("Anzahl Strasse:  ");
        jPStr.setBackground(Color.BLACK);
        jPStr.setForeground(Color.WHITE);
        jPStr.setFont(new Font("Arial", Font.BOLD, 15));
        jPStrasse.add(jPStr);

        jLStrasse = new JLabel("666");
        jLStrasse.setBackground(Color.DARK_GRAY);
        jLStrasse.setForeground(Color.RED);
        jLStrasse.setFont(new Font("Arial", Font.BOLD, 15));

        jPStrasse.add(jLStrasse);
        mitte.add(jPSchock);

        JPanel jPGenaral = new JPanel(new FlowLayout());
        jPGenaral.setBackground(Color.DARK_GRAY);
        JLabel jPGen = new JLabel("Anzahl General:  ");
        jPGen.setBackground(Color.BLACK);
        jPGen.setForeground(Color.WHITE);
        jPGen.setFont(new Font("Arial", Font.BOLD, 15));
        jPGenaral.add(jPGen);

        jLGenaral = new JLabel("666");
        jLGenaral.setBackground(Color.DARK_GRAY);
        jLGenaral.setForeground(Color.RED);
        jLGenaral.setFont(new Font("Arial", Font.BOLD, 15));

        jPStrasse.add(jLGenaral);
        mitte.add(jPGenaral);

        JPanel jPEinfacherWurf = new JPanel(new FlowLayout());
        jPEinfacherWurf.setBackground(Color.DARK_GRAY);
        JLabel jPWurf = new JLabel("Anzahl Einfacherwurf:  ");
        jPWurf.setBackground(Color.BLACK);
        jPWurf.setForeground(Color.WHITE);
        jPWurf.setFont(new Font("Arial", Font.BOLD, 15));
        jPEinfacherWurf.add(jPWurf);

        jLEinfacherWurf = new JLabel("666");
        jLEinfacherWurf.setBackground(Color.DARK_GRAY);
        jLEinfacherWurf.setForeground(Color.RED);
        jLEinfacherWurf.setFont(new Font("Arial", Font.BOLD, 15));

        jPEinfacherWurf.add(jLEinfacherWurf);
        mitte.add(jPEinfacherWurf);

        JPanel jPSchockAus = new JPanel(new FlowLayout());
        jPSchockAus.setBackground(Color.DARK_GRAY);
        JLabel jPSchAus = new JLabel("Anzahl SchockAus:  ");
        jPSchAus.setBackground(Color.BLACK);
        jPSchAus.setForeground(Color.WHITE);
        jPSchAus.setFont(new Font("Arial", Font.BOLD, 15));
        jPSchockAus.add(jPSchAus);

        jLSchockAus = new JLabel("666");
        jLSchockAus.setBackground(Color.DARK_GRAY);
        jLSchockAus.setForeground(Color.RED);
        jLSchockAus.setFont(new Font("Arial", Font.BOLD, 15));

        jPSchockAus.add(jLSchockAus);
        mitte.add(jPSchockAus);










        JPanel jBPWeiter = new JPanel(new FlowLayout());
        jBPWeiter.setPreferredSize(new Dimension(1020,20));
        jBPWeiter.setBackground(Color.BLACK);
        JButton jBWeiter = new JButton("Weiter");

        ActionListener weiter = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                gui.setZustand(1);
                gui.updateView();
            }
        };

        jBWeiter.addActionListener(weiter);
        jBPWeiter.add(jBWeiter);
        mitte.add(jBPWeiter);



        jp.add(mitte, BorderLayout.CENTER);



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

        //ToDo: Hier kannst du die JLabel updaten mit Werten aus der DB

        return this;
    }

}

















