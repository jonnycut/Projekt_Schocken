package gui;

import Datenbank.Datenbank;
import Grafik.Grafik;
import netzwerk.Client;
import netzwerk.Server;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by dfleuren on 23.05.2016.
 * Diese Klasse ist der Anmeldebildschirm vom Spiel.
 */
public class Anmeldung extends JPanel {

    GUI gui;

    /**
     * Der Konstruktor erzeugt ein neues JPanel, als Anmeldebereich, um sich am Spiel an zu melden oder sich zu registrieren.
     * @param gui GUI ist die aktuelle GUI.
     */
    public Anmeldung(GUI gui){
        super();
        this.gui = gui;

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
        JPasswordField jTPasswort = new JPasswordField(20);
        jTPasswort.setDocument(new JTextFieldLimit(30));
        jPTPasswort.add(jTPasswort);

        JButton jBStart = new JButton("START");

        // Nur drückbar wenn die DB Abfrage erfolgreich war
        jBStart.setEnabled(false);
        JPanel jBPStart = new JPanel(new FlowLayout());
        jBPStart.setBackground(Color.DARK_GRAY);
        jBPStart.add(jBStart);

        /* Führt die Methode starteSpiel() aus und schaltet die Ansicht auf das Spielfeld um.
         * Fügt den Spieler in der Datenbank als Teilnehmer hinzu.
         */
        ActionListener startButton = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //ToDo: Schaltet weiter und gibt das Spiel frei

                starteSpiel();
                gui.setZustand(6);
                gui.updateView(e);

                try {
                    Datenbank.getInstance().insertTeilnehmer(jTName.getText());
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }

            }
        };
        jBStart.addActionListener(startButton);

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

        untenLinks.add(jBPStart);

        /* Prüft in der Datenbank ob der Name und das Passwort übereinstimmen.
         * Wenn NEIN: Kommt eine Fehlermeldeung.
         * Wenn JA: Wird das passende Profilbild aus der Datenbank geholt und angezeigt.
         *          Der StartButton wird freigegeben.
         */
        ActionListener okButton = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jTName.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Bitte Benutzername eingeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                }

                char[] zeichen = jTPasswort.getPassword();
                String passwort = new String(zeichen);
                if (passwort.equals("")) {
                    JOptionPane.showMessageDialog(null, "Bitte Passwort eingeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                }

                try {
                    if (Datenbank.getInstance().selectNutzerkennung(jTName.getText(), passwort)) {
                        jBProfil.setIcon(Datenbank.getInstance().selectProfilBild(jTName.getText()));
                        gui.updateSpielerListe(Datenbank.getInstance().selectSpieler(jTName.getText()));
                        jBPProfil.add(jBProfil);
                        jBStart.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Benutzername oder Passwort falsch", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                } catch (ClassNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, "Datenbank wurde nicht gefunden", "Fehler", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e1) {
                    e1.printStackTrace();
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
        JPanel rechts = new JPanel(new BorderLayout());
        rechts.setPreferredSize(new Dimension(240, 0));
        rechts.setBackground(Color.DARK_GRAY);


        JPanel obenRechts = new JPanel();
        obenRechts.setBackground(Color.DARK_GRAY);
        JLabel jLObenRechts = new JLabel("");
        jLObenRechts.setPreferredSize(new Dimension(0, 200));
        obenRechts.add(jLObenRechts);
        obenRechts.setLayout(new BoxLayout(obenRechts, BoxLayout.Y_AXIS));

        JPanel untenRechts = new JPanel();
        untenRechts.setBackground(Color.DARK_GRAY);
        untenRechts.setLayout(new BoxLayout(untenRechts, BoxLayout.Y_AXIS));

        JPanel register = new JPanel(new FlowLayout());
        register.setBackground(Color.DARK_GRAY);
        JButton jBReg = new JButton("Registrieren");

        // schaltet die Ansicht auf den Registrierungsbildschirm weiter.
        ActionListener reg = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.setZustand(3);
                gui.updateView(e);
            }
        };

        jBReg.addActionListener(reg);
        register.add(jBReg);

        JPanel reset = new JPanel(new FlowLayout());
        reset.setBackground(Color.DARK_GRAY);
        JButton pwReset = new JButton("Passwort reseten");

        /* Es öffnet sich ein Abfragefenster für das Passwort.
         * Ist das Passwort FALSCH: Kommt eine Fehlermeldung.
         * Ist das Passwort RICHTIG: Schaltet die Ansicht auf den Administrationssbildschirm weiter.
         */
        ActionListener pwReseten = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String richtigesPWD = "root";

                JLabel jPassword = new JLabel("Passwort");
                JTextField password = new JPasswordField();
                Object[] ob = {jPassword, password};
                int result = JOptionPane.showConfirmDialog(null, ob, "Bitte Passwort eingeben", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String pwdEingabe = password.getText();
                    if(pwdEingabe.equals(richtigesPWD)){
                        gui.setZustand(5);
                        gui.updateView(e);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Passwort falsch", "fehler",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        pwReset.addActionListener(pwReseten);
        reset.add(pwReset);

        obenRechts.add(register);
        untenRechts.add(reset);


        rechts.add(obenRechts, BorderLayout.CENTER);
        rechts.add(untenRechts, BorderLayout.SOUTH);

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

    public void starteSpiel() {

        gui.setSpielfeld(new Spielfeld(gui));

//        try {
//            if(Datenbank.getInstance().selectOffenesSpiel() !=1){
//                gui.setSpielfeld(new Spielfeld(gui));
//            }
//            else{
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public void pruefeSpiel() {

    }

//    public void erstelleServer() {
//        if (gui.getServer() == null) {
//            gui.setServer(new Server());
//            gui.setClient(new Client(""/*ToDo: Client IP Adress*/));
//        }else{
//            gui.setClient(new Client(""/*ToDo: Client IP Adress*/));
//        }
//    }

    //----------------------------------------MINNEREKLASSEN-----------------------------------------------------------


    /**
     * Diese Klasse ist für die Beschänkung der JTextfield verandwortlich.
     * So dass nur eine Maxanzahl von Zeichen eingetragen werden dürfen / können.
     */
    public class JTextFieldLimit extends PlainDocument {
        private int limit;

        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null)
                return;

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }
}


