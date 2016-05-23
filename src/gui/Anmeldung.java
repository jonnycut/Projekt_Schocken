package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class Anmeldung extends JPanel {

    public Anmeldung(){
        super();

        JPanel jp = new JPanel(new BorderLayout());

        JPanel oben = new JPanel();
        JLabel jLoben = new JLabel("Herzlich Willkommen");
        oben.add(jLoben);

        jp.add(oben, BorderLayout.NORTH);


        JPanel links = new JPanel(new GridLayout(8,1));
        JLabel jLName = new JLabel("Benutzername");
        JTextField jTName = new JTextField(30);
        JLabel jLPasswort = new JLabel("Passwort");
        JTextField jTPasswort = new JTextField(30);
        JButton ok = new JButton("OK");
        JLabel jLProfil = new JLabel("Profilbild");
        JButton jBProfil = new JButton();
        jBProfil.setPreferredSize(new Dimension(150, 150));
        JButton jBWeiter = new JButton("WEITER");

        links.add(jBWeiter);
        links.add(jBProfil);
        links.add(jLProfil);
        links.add(ok);
        links.add(jTPasswort);
        links.add(jLPasswort);
        links.add(jTName);
        links.add(jLName);

        jp.add(links, BorderLayout.EAST);

        JPanel mitte = new JPanel();
        //JLabel jLMitte = new JLabel(Grafik.LOGO);
        //mitte.add(jLMitte);

        jp.add(mitte, BorderLayout.CENTER);

        JPanel rechts = new JPanel();
        JButton jBReg = new JButton("Registrieren");

        rechts.add(jBReg);
        jp.add(rechts, BorderLayout.WEST);

        JPanel unten = new JPanel(new GridLayout(1,3));
        JPanel jPProjekt = new JPanel();
        JLabel jLProjekt = new JLabel("Projekt: SCHOCKEN");
        jPProjekt.add(jLProjekt);

        JPanel jPGruppe = new JPanel();
        JLabel jLPGruppe = new JLabel("Projektgruppe: U.F.O");
        jPGruppe.add(jLPGruppe);

        JPanel jPFsbw = new JPanel(new FlowLayout());
        JLabel jLFsbw = new JLabel("FSBwIT 2016");
        jPFsbw.add(jLFsbw);

        unten.add(jPFsbw);
        unten.add(jPGruppe);
        unten.add(jPProjekt);

        jp.add(unten, BorderLayout.SOUTH);

        add(jp);
    }





    public boolean pruefeNutzerkennung(String name, String passwort){



        return true;
    }

    public void starteSpiel(){

    }

    public void pruefeSpiel(){

    }

    public void erstelleServer(){

    }
}
