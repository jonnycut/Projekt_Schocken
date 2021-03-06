package gui;

import Datenbank.Datenbank;
import Grafik.Grafik;
import netzwerk.Netzwerk;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

/**<pre>
 * Created by U.F.O. on 23.05.2016.
 * Diese Klasse ist der Anmeldebildschirm vom Spiel.
 * </pre>
 *
 * @author DFleuren
 */
public class Anmeldung extends JPanel {

    /**
     *  Die aktuelle GUI
     */
    GUI gui;

    /**
     * Das JTextfield um den Spielername zu speichern.
     */
    JTextField jTName;

    /**<pre>
     * Der Konstruktor erzeugt ein neues JPanel, als Anmeldebereich, um sich am Spiel an zu melden oder sich zu registrieren.
     * Dort besteht auch die Möglichkeit die Datenbank zu Löschen oder ein Spielerpasswort neu zu vergeben.
     * @param gui GUI ist die aktuelle GUI.
     * </pre>
     */
    public Anmeldung(GUI gui) {
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
        jTName = new JTextField(20);
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

        // Nur drückbar wenn die DB Abfrage nach dem Spieler erfolgreich war
        jBStart.setEnabled(false);
        JPanel jBPStart = new JPanel(new FlowLayout());
        jBPStart.setBackground(Color.DARK_GRAY);
        jBPStart.add(jBStart);

        // Erzeugt ein neues Spielfeld und schaltet dann die Ansicht auf das Spielfeld um.
        ActionListener startButton = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.setSpielfeld(new Spielfeld(gui));
                gui.setZustand(6);
                gui.updateView();
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
         *          Fürt die Methode pruufeSpiel()aus.
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
                    // JA der Spieler ist in der Datenbank
                    if (Datenbank.getInstance().selectNutzerkennung(jTName.getText(), passwort)) {
                        jBProfil.setIcon(Datenbank.getInstance().selectProfilBild(jTName.getText()));
                        jBPProfil.add(jBProfil);
                        jBStart.setEnabled(true);
                        ok.setEnabled(false);
                        gui.setBesitzerName(jTName.getText());
                        pruefeSpiel();
                        gui.setStatistik(jTName.getText());
                        gui.setZustand(2);
                        gui.updateView();

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

        JPanel mitteRechts = new JPanel();
        mitteRechts.setBackground(Color.DARK_GRAY);
        JPanel loesche = new JPanel(new FlowLayout());
        loesche.setBackground(Color.DARK_GRAY);
        JButton jBdbLoeschen = new JButton("Datenbank löschen");

        /* Es öffnet sich ein Abfragefenster für das Passwort("root") vom DBAdmin.
         * Prüft aus einem Dialogfeld ob das eingegeben Passwort übereinstimmt.
         *      Wenn NEIN: Kommt eine Fehlermeldung.
         *      Wenn JA: Wird die Methode der Datenbank dropDB() aausgeführt und die Datenbank wird gelöscht.
         */
        ActionListener loescheDB = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String richtigesPWD = "root";

                JLabel jPassword = new JLabel("Passwort");
                JTextField password = new JPasswordField(10);
                password.setDocument(new JTextFieldLimit(10));
                Object[] ob = {jPassword, password};
                int result = JOptionPane.showConfirmDialog(null, ob, "Bitte Passwort eingeben", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String pwdEingabe = password.getText();
                    if (pwdEingabe.equals(richtigesPWD)) {
                        if(Datenbank.dropDB("db_schocken2")){
                            JOptionPane.showMessageDialog(null, "Die Datenbank db_schocken2 wurde gelöscht.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Die Datenbank wurde NICHT gelöscht.", "Fehler", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Passwort falsch", "fehler", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        jBdbLoeschen.addActionListener(loescheDB);
        loesche.add(jBdbLoeschen);
        mitteRechts.add(loesche);

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
                gui.updateView();
            }
        };

        jBReg.addActionListener(reg);
        register.add(jBReg);

        JPanel reset = new JPanel(new FlowLayout());
        reset.setBackground(Color.DARK_GRAY);
        JButton pwReset = new JButton("Passwort reseten");

        /* Es öffnet sich ein Abfragefenster für das Passwort("root") vom DBAdmin.
         * Ist das Passwort FALSCH: Kommt eine Fehlermeldung.
         * Ist das Passwort RICHTIG: Schaltet die Ansicht auf den Administrationssbildschirm weiter.
         */
        ActionListener pwReseten = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String richtigesPWD = "root";

                JLabel jPassword = new JLabel("Passwort");
                JTextField password = new JPasswordField(10);
                password.setDocument(new JTextFieldLimit(10));
                Object[] ob = {jPassword, password};
                int result = JOptionPane.showConfirmDialog(null, ob, "Bitte Passwort eingeben", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String pwdEingabe = password.getText();
                    if (pwdEingabe.equals(richtigesPWD)) {
                        gui.setZustand(5);
                        gui.updateView();
                    } else {
                        JOptionPane.showMessageDialog(null, "Passwort falsch", "fehler", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        pwReset.addActionListener(pwReseten);
        reset.add(pwReset);

        obenRechts.add(register);
        untenRechts.add(reset);

        rechts.add(mitteRechts, BorderLayout.NORTH);
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

    /**<pre>
     * Prüft über die Datenbank, ob bereits ein Spiel existiert, oder ob eine neues erstellt werden soll.
     * Mit der SpielID wird der Spielleiter ermittelt und es wird eine neues Netzwerk erstellt mit Client oder Server mit Client.
     * </pre>
     */
    public void pruefeSpiel() {

        try {
            Datenbank.getInstance().insertTeilnehmer(jTName.getText());

            int spielID = Datenbank.getInstance().selectOffenesSpiel();
            String spielleiter = Datenbank.getInstance().selectSpielleiterKennung(spielID);
            String[] serverIP = {""};

            if (spielleiter.equals(jTName.getText())) {

                new Thread() {
                    public void run() {

                        gui.setNetzwerk(new Netzwerk(serverIP,gui));
                    }
                }.start();

            } else {
                serverIP[0] = Datenbank.getInstance().selectServerIP();
                new Thread() {
                    public void run() {

                        gui.setNetzwerk(new Netzwerk(serverIP,gui));
                    }
                }.start();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------MINNEREKLASSEN-----------------------------------------------------------


    /** <pre>
     * Diese Klasse ist für die Beschänkung der JTextfields verandwortlich.
     * So dass nur eine Maxanzahl von Zeichen eingetragen werden darf / kann.
     *
     * Diese Klasse basiert auf einer Vorlage von http://www.java2s.com/Tutorial/Java/0260__Swing-Event/LimitJTextFieldinputtoamaximumlength.htm
     * </pre>
     */
    private class JTextFieldLimit extends PlainDocument {
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


