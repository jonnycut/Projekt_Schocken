package gui;

import Datenbank.Datenbank;
import Grafik.Grafik;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class Anmeldung extends JPanel {

    public Anmeldung(GUI gui){
        super();

        //-------------------------------------GRUND-PANEL-------------------------------------------------------------
        JPanel jp = new JPanel(new BorderLayout());
        jp.setBackground(Color.BLACK);


        //-------------------------------------OBEN-PANEL--------------------------------------------------------------
        JPanel oben = new JPanel();
        oben.setBackground(Color.BLACK);
        JLabel jLoben = new JLabel("Herzlich Willkommen");
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

        JPanel jPName = new JPanel(new FlowLayout());
        jPName.setBackground(Color.DARK_GRAY);
        JLabel jLName = new JLabel("Benutzername");
        jLName.setForeground(Color.WHITE);
        jPName.add(jLName);

        JPanel jPTName = new JPanel(new FlowLayout());
        jPTName.setBackground(Color.DARK_GRAY);
        JTextField jTName = new JTextField(20);
        jTName.setDocument(new JTextFieldLimit(30));
        jPTName.add(jTName);

        JPanel jPPasswort = new JPanel(new FlowLayout());
        jPPasswort.setBackground(Color.DARK_GRAY);
        JLabel jLPasswort = new JLabel("Passwort");
        jLPasswort.setForeground(Color.WHITE);
        jPPasswort.add(jLPasswort);

        JPanel jPTPasswort = new JPanel();
        jPTPasswort.setBackground(Color.DARK_GRAY);
        JTextField jTPasswort = new JTextField(20);
        jTPasswort.setDocument(new JTextFieldLimit(30));
        jPTPasswort.add(jTPasswort);


        JButton jBWeiter = new JButton("START");
        // Nur drückbar wenn die DB Abfrage erfolgreich war
        jBWeiter.setEnabled(false);
        JPanel jBPweiter = new JPanel(new FlowLayout());
        jBPweiter.setBackground(Color.DARK_GRAY);
        jBPweiter.add(jBWeiter);

        ActionListener weiterButton = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //ToDo: Schaltet weiter und gibt das Spiel frei
            }
        };
        jBWeiter.addActionListener(weiterButton);

        JPanel jBPok = new JPanel(new FlowLayout());
        jBPok.setBackground(Color.DARK_GRAY);
        JButton ok = new JButton("OK");
        jBPok.setBackground(Color.DARK_GRAY);
        jBPok.setPreferredSize(new Dimension(0, 80));

        jBPok.add(ok);

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
        obenLinks.add(jBPok);
        obenLinks.add(jPProfil);
        obenLinks.add(jBPProfil);

        links.add(obenLinks, BorderLayout.NORTH);

        JPanel untenLinks = new JPanel(new BorderLayout());
        untenLinks.setBackground(Color.DARK_GRAY);

        untenLinks.add(jBPweiter);

        ActionListener okButton = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //ToDo: Prüfung in der DB auf Name, Passwort und Profiblid
                try {
                    if(Datenbank.getInstance().selectNutzerkennung(jTName.getText(),jTPasswort.getText())){
                        jBProfil.setIcon(Datenbank.getInstance().selectProfilBild(jTName.getText()));
                        jBPProfil.add(jBProfil);
                        jBWeiter.setEnabled(true);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Benutzername oder Passwort falsch", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                } catch (ClassNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, "Datenbank wurde nicht gefunden","Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        ok.addActionListener(okButton);

        links.add(untenLinks, BorderLayout.SOUTH);
        jp.add(links, BorderLayout.WEST);


        //-------------------------------------MITTE-PANEL-------------------------------------------------------------
        JPanel mitte = new JPanel();
        mitte.setBackground(Color.DARK_GRAY);
        JLabel jLMitte = new JLabel(Grafik.LOGO1);
        mitte.add(jLMitte);

        jp.add(mitte, BorderLayout.CENTER);


        //-------------------------------------RECHTS-PANEL------------------------------------------------------------
        JPanel rechts = new JPanel();
        rechts.setPreferredSize(new Dimension(240,0));
        rechts.setBackground(Color.DARK_GRAY);
        rechts.setLayout(new BoxLayout(rechts, BoxLayout.Y_AXIS));

        JPanel obenRechts = new JPanel(new FlowLayout());
        obenRechts.setBackground(Color.DARK_GRAY);
        JLabel jLObenRechts = new JLabel("");
        obenRechts.add(jLObenRechts);

        JPanel untenRechts = new JPanel(new FlowLayout());
        untenRechts.setBackground(Color.DARK_GRAY);
        JButton jBReg = new JButton("Registrieren");

        ActionListener reg = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.setZustand(3);
                gui.updateView(e);

            }
        };

        jBReg.addActionListener(reg);
        untenRechts.add(jBReg);

        rechts.add(obenRechts);
        rechts.add(untenRechts);

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

        add(jp,BorderLayout.CENTER);
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

    public class JTextFieldLimit extends PlainDocument {
        private int limit;

        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
            if (str == null) return;

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }
}


