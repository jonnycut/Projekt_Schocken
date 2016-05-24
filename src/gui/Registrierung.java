package gui;

import Grafik.Grafik;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class Registrierung extends JPanel {

    private String fehler = "";

    public Registrierung() {
        super();
        Icon[] avatare = {Grafik.AVATAR_R2D2, Grafik.AVATAR_BB8, Grafik.AVATAR_BOBA, Grafik.AVATAR_C3PO, Grafik.AVATAR_TROOPER, Grafik.AVATAR_VADER, Grafik.AVATAR_WUKI, Grafik.AVATAR_YODA, Grafik.AVATAR_BATMAN,  Grafik.AVATAR_C_AMERICA, Grafik.AVATAR_DEADPOOL, Grafik.AVATAR_FLASH, Grafik.AVATAR_IRONMAN, Grafik.AVATAR_SPIDERMAN, Grafik.AVATAR_SUPERMAN, Grafik.AVATAR_THOR};


        //-------------------------------------GRUND-PANEL-------------------------------------------------------------
        JPanel jp = new JPanel(new BorderLayout());
        jp.setBackground(Color.BLACK);


        //-------------------------------------OBEN-PANEL--------------------------------------------------------------
        JPanel oben = new JPanel();
        oben.setBackground(Color.BLACK);
        JLabel jLoben = new JLabel("REGISTRIERUNG");
        jLoben.setForeground(Color.WHITE);
        oben.add(jLoben);

        jp.add(oben, BorderLayout.NORTH);


        //-------------------------------------LINKS-PANEL-------------------------------------------------------------

        JPanel links = new JPanel(new BorderLayout());
        links.setBackground(Color.DARK_GRAY);

        JPanel obenLinks = new JPanel();
        obenLinks.setBackground(Color.DARK_GRAY);
        obenLinks.setLayout(new BoxLayout(obenLinks, BoxLayout.Y_AXIS));

        JLabel temp = new JLabel("");
        temp.setPreferredSize(new Dimension(0, 50));
        JLabel temp1 = new JLabel("");
        temp1.setPreferredSize(new Dimension(0, 50));

        JPanel jPName = new JPanel(new FlowLayout());
        jPName.setBackground(Color.DARK_GRAY);
        JLabel jLName = new JLabel("Benutzername");
        jLName.setForeground(Color.WHITE);
        jPName.add(jLName);

        JPanel jPTName = new JPanel(new FlowLayout());
        jPTName.setBackground(Color.DARK_GRAY);
        JTextField jTName = new JTextField(20);
        jPTName.add(jTName);

        JPanel jPPasswort = new JPanel(new FlowLayout());
        jPPasswort.setBackground(Color.DARK_GRAY);
        JLabel jLPasswort = new JLabel("Passwort");
        jLPasswort.setForeground(Color.WHITE);
        jPPasswort.add(jLPasswort);

        JPanel jPTPasswort = new JPanel();
        jPTPasswort.setBackground(Color.DARK_GRAY);
        JTextField jTPasswort = new JTextField(20);
        jPTPasswort.add(jTPasswort);

        JPanel jPPasswortW = new JPanel(new FlowLayout());
        jPPasswortW.setBackground(Color.DARK_GRAY);
        JLabel jLPasswortW = new JLabel("Passwort wierderholen");
        jLPasswortW.setForeground(Color.WHITE);
        jPPasswortW.add(jLPasswortW);

        JPanel jPTPasswortW = new JPanel();
        jPTPasswortW.setBackground(Color.DARK_GRAY);
        JTextField jTPasswortW = new JTextField(20);
        jPTPasswortW.add(jTPasswortW);

        JPanel jPProfil = new JPanel(new FlowLayout());
        jPProfil.setBackground(Color.DARK_GRAY);
        JLabel jLProfil = new JLabel("Profilbild");
        jLProfil.setForeground(Color.WHITE);
        jPProfil.add(jLProfil);

        JPanel jBPProfil = new JPanel(new FlowLayout());
        jBPProfil.setBackground(Color.DARK_GRAY);
        JButton jBProfil = new JButton();
        jBProfil.setBackground(Color.WHITE);
        jBProfil.setPreferredSize(new Dimension(150, 150));
        jBPProfil.add(jBProfil);

        obenLinks.add(temp);
        obenLinks.add(jPName);
        obenLinks.add(jPTName);
        obenLinks.add(jPPasswort);
        obenLinks.add(jPTPasswort);
        obenLinks.add(jPPasswortW);
        obenLinks.add(jPTPasswortW);
        obenLinks.add(temp1);
        obenLinks.add(jPProfil);
        obenLinks.add(jBPProfil);

        links.add(obenLinks, BorderLayout.NORTH);

        JPanel untenLinks = new JPanel(new BorderLayout());
        untenLinks.setBackground(Color.DARK_GRAY);

        JButton jBWeiter = new JButton("WEITER");
        jBWeiter.setEnabled(false);
        JPanel jBPweiter = new JPanel(new FlowLayout());
        jBPweiter.setBackground(Color.DARK_GRAY);
        jBPweiter.add(jBWeiter);

        untenLinks.add(jBPweiter);

        links.add(untenLinks, BorderLayout.SOUTH);
        jp.add(links, BorderLayout.WEST);


        //-------------------------------------MITTE-PANEL-------------------------------------------------------------

        JPanel mitte = new JPanel();
        mitte.setBackground(Color.DARK_GRAY);
        JLabel jLMitte = new JLabel(Grafik.BLOCK_LOGO);
        mitte.add(jLMitte);

        jp.add(mitte, BorderLayout.CENTER);


        //-------------------------------------RECHTS-PANEL------------------------------------------------------------
        JPanel rechts = new JPanel(new GridLayout(4,4));

        ActionListener profilbild = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JButton tempB = (JButton)e.getSource();
                jBProfil.setIcon(tempB.getIcon());
                jBPProfil.add(jBProfil);
                jLMitte.setIcon(Grafik.BLOCK);
                mitte.add(jLMitte);
                //ToDo: Überarbeiten!!!
                if(jTName.getText().length() != 0 && jTPasswort.getText().length() != 0 && jTPasswortW.getText().length() != 0)
                jBWeiter.setEnabled(true);
            }
        };

        for (int i = 0; i <= 15; i++) {
            JButton jb = new JButton();
            jb.setIcon(avatare[i]);
            jb.setName("" + i);
            jb.setOpaque(true);
            jb.setPreferredSize(new Dimension(130, 130));
            jb.setBackground(Color.DARK_GRAY);
            jb.setBorder(new LineBorder(null));
            jb.addActionListener(profilbild);
            rechts.add(jb);
        }

        jp.add(rechts, BorderLayout.EAST);


        //-------------------------------------UNTEN-PANEL-------------------------------------------------------------
        JPanel unten = new JPanel(new GridLayout(1,3));
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


    public void spielerAnlegen(String name, String passwort) {

    }

    public boolean pruefeSpielerKennung(String name) {

        return true;
    }


}
