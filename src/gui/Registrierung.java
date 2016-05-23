package gui;

import grafik.Grafik;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class Registrierung extends JPanel {

    public Registrierung() {
        super();

        Icon[] avatare = {Grafik.AVATAR_BATMAN, Grafik.AVATAR_BB8, Grafik.AVATAR_BOBA, Grafik.AVATAR_C3PO, Grafik.AVATAR_C_AMERICA, Grafik.AVATAR_DEADPOOL, Grafik.AVATAR_FLASH, Grafik.AVATAR_IRONMAN, Grafik.AVATAR_R2D2, Grafik.AVATAR_SPIDERMAN, Grafik.AVATAR_SUPERMAN, Grafik.AVATAR_THOR, Grafik.AVATAR_TROOPER, Grafik.AVATAR_VADER, Grafik.AVATAR_WUKI, Grafik.AVATAR_YODA};

        JPanel oben = new JPanel();
        JLabel jLoben = new JLabel("REGISTRIERUNG");
        oben.add(jLoben);
        add(oben, BorderLayout.NORTH);

        JPanel links = new JPanel(new GridLayout(9,1));

        JLabel jLName = new JLabel("Benutzername");
        JTextField jTName = new JTextField(30);
        JLabel jLPasswort = new JLabel("Passwort");
        JTextField jTPasswort = new JTextField(30);
        JLabel jLPassortW = new JLabel("Passwort wiederholen");
        JTextField jTPasswortW = new JTextField(30);
        JLabel jLProfil = new JLabel("Profilbild");
        JButton jbProfil = new JButton();
        jbProfil.setPreferredSize(new Dimension(200, 200));

        JButton ok = new JButton("OK");

        links.add(ok);
        links.add(jbProfil);
        links.add(jLProfil);
        links.add(jTPasswortW);
        links.add(jLPassortW);
        links.add(jTPasswort);
        links.add(jLPasswort);
        links.add(jTName);
        links.add(jLName);

        add(links, BorderLayout.WEST);


        JPanel rechts = new JPanel(new GridLayout(4,4));

        ActionListener acl = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        };

        for (int i = 0; i <= 15; i++) {
            JButton jb = new JButton();
            jb.setIcon(avatare[i]);
            jb.setBorder(new LineBorder(Color.BLACK, 4));
            jb.setName("" + i);
            jb.setOpaque(true);
            jb.setPreferredSize(new Dimension(200, 200));
            jb.addActionListener(acl);
            rechts.add(jb);
        }

        add(rechts, BorderLayout.EAST);
    }


    public void spielerAnlegen(String name, String passwort) {

    }

    public boolean pruefeSpielerKennung(String name) {

        return true;
    }


}
