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

    public Registrierung() {
        super();

        Icon[] avatare = {Grafik.AVATAR_R2D2, Grafik.AVATAR_BB8, Grafik.AVATAR_BOBA, Grafik.AVATAR_C3PO, Grafik.AVATAR_TROOPER, Grafik.AVATAR_VADER, Grafik.AVATAR_WUKI, Grafik.AVATAR_YODA, Grafik.AVATAR_BATMAN,  Grafik.AVATAR_C_AMERICA, Grafik.AVATAR_DEADPOOL, Grafik.AVATAR_FLASH, Grafik.AVATAR_IRONMAN, Grafik.AVATAR_SPIDERMAN, Grafik.AVATAR_SUPERMAN, Grafik.AVATAR_THOR};

        JPanel jp = new JPanel(new BorderLayout());

        JPanel oben = new JPanel();
        oben.setBorder(new LineBorder(Color.BLACK, 2));
        JLabel jLoben = new JLabel("REGISTRIERUNG");
        jLoben.setForeground(Color.WHITE);
        oben.setBackground(Color.BLACK);
        jLoben.setHorizontalAlignment(SwingConstants.CENTER);
        oben.add(jLoben);
        jp.add(oben, BorderLayout.NORTH);

        JPanel links = new JPanel();
        links.setPreferredSize(new Dimension(400, 500));
        links.setBackground(Color.DARK_GRAY);
        links.setLayout(new BoxLayout(links, BoxLayout.Y_AXIS));

        JLabel jLName = new JLabel("Benutzername");
        jLName.setPreferredSize(new Dimension(0,50));
        jLName.setForeground(Color.WHITE);
        jLName.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField jTName = new JTextField(20);
        JLabel jLPasswort = new JLabel("Passwort");
        jLPasswort.setPreferredSize(new Dimension(0,50));
        jLPasswort.setForeground(Color.WHITE);
        jLPasswort.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField jTPasswort = new JTextField(20);
        JLabel jLPasswortW = new JLabel("Passwort wiederholen");
        jLPasswortW.setPreferredSize(new Dimension(0,50));
        jLPasswortW.setForeground(Color.WHITE);
        jLPasswortW.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField jTPasswortW = new JTextField(20);
        JLabel jLProfil = new JLabel("Profilbild");
        jLProfil.setPreferredSize(new Dimension(0,50));
        jLProfil.setForeground(Color.WHITE);
        jLProfil.setHorizontalAlignment(SwingConstants.CENTER);
        JButton jBProfil = new JButton();
        jBProfil.setBackground(Color.WHITE);
        jBProfil.setPreferredSize(new Dimension(130, 130));

        links.add(jLName);
        links.add(jTName);
        links.add(jLPasswort);
        links.add(jTPasswort);
        links.add(jLPasswortW);
        links.add(jTPasswortW);
        links.add(jLProfil);
        links.add(jBProfil);

        jp.add(links, BorderLayout.WEST);

        JPanel mitte = new JPanel();
        //JLabel jLMitte = new JLabel("<========>");
        //jLMitte.setForeground(Color.DARK_GRAY);
        JButton ok = new JButton("WEITER");

        //mitte.add(jLMitte);
        mitte.add(ok, BorderLayout.CENTER);
        mitte.setBackground(Color.DARK_GRAY);

        jp.add(mitte, BorderLayout.CENTER);



        JPanel rechts = new JPanel(new GridLayout(4,4));

        ActionListener acl = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                JButton temp = (JButton)e.getSource();
                jBProfil.setIcon(temp.getIcon());
                jBProfil.setPreferredSize(new Dimension(130, 130));
                links.add(jBProfil);
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
            jb.addActionListener(acl);
            rechts.add(jb);
        }

        jp.add(rechts, BorderLayout.EAST);

        JPanel unten = new JPanel();
        unten.setBackground(Color.BLACK);
        JLabel jLInfos = new JLabel();
        String fehler = "Bla Bla Bla";
        jLInfos.setText(fehler);
        jLInfos.setForeground(Color.RED);


        unten.add(jLInfos);
        jp.add(unten, BorderLayout.SOUTH);
        add(jp, BorderLayout.CENTER);
    }


    public void spielerAnlegen(String name, String passwort) {

    }

    public boolean pruefeSpielerKennung(String name) {

        return true;
    }


}
