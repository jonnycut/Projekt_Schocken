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
 * Diese Klasse ist für den Administrator um Passwörter zu ändern.
 */
public class Administration extends JPanel{

    /**
     * Der Konstruktor erzeugt ein neues JPanel, als Administrationsbereich, um Passwörter bei Spielern zu ändern.
     * @param gui GUI ist die aktuelle GUI.
     */
    public Administration(GUI gui) {
        super();


        //-------------------------------------GRUND-PANEL-------------------------------------------------------------

        JPanel jp = new JPanel(new BorderLayout());
        jp.setBackground(Color.BLACK);


        //-------------------------------------OBEN-PANEL--------------------------------------------------------------

        JPanel oben = new JPanel();
        oben.setBackground(Color.BLACK);
        JLabel jLoben = new JLabel("Administration");
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
        temp.setPreferredSize(new Dimension(0, 180));

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
        JLabel jLPasswort = new JLabel("Neues Passwort");
        jLPasswort.setForeground(Color.WHITE);
        jPPasswort.add(jLPasswort);

        JPanel jPTPasswort = new JPanel();
        jPTPasswort.setBackground(Color.DARK_GRAY);
        JPasswordField jTPasswort = new JPasswordField(20);
        jTPasswort.setDocument(new JTextFieldLimit(30));
        jPTPasswort.add(jTPasswort);

        JPanel jBPok = new JPanel(new FlowLayout());
        jBPok.setBackground(Color.DARK_GRAY);
        JButton ok = new JButton("OK");
        jBPok.setBackground(Color.DARK_GRAY);
        jBPok.setPreferredSize(new Dimension(0, 80));

        jBPok.add(ok);

        obenLinks.add(temp);
        obenLinks.add(jPName);
        obenLinks.add(jPTName);
        obenLinks.add(jPPasswort);
        obenLinks.add(jPTPasswort);
        obenLinks.add(jBPok);

        links.add(obenLinks, BorderLayout.NORTH);

        JPanel untenLinks = new JPanel(new BorderLayout());
        untenLinks.setBackground(Color.DARK_GRAY);

        // Liest das neue Passwort aus und führt die Methode passwortReset() aus.
        ActionListener okButton = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                char[] zeichen = jTPasswort.getPassword();
                String passwort = new String(zeichen);
                passwortReset(jTName.getText(),passwort);
            }
        };

        ok.addActionListener(okButton);

        JButton jBZurueck = new JButton("ZURÜCK");

        // Schaltet die Ansicht zurück auf den Anmeldebildschirm
        ActionListener zurueck = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.setZustand(1);
                gui.updateView(e);
            }
        };

        jBZurueck.addActionListener(zurueck);
        JPanel jBPweiter = new JPanel(new FlowLayout());
        jBPweiter.setBackground(Color.DARK_GRAY);
        jBPweiter.add(jBZurueck);

        untenLinks.add(jBPweiter);

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

    /**
     * Setzt das Passwort eines Spielers zurück.
     * @param name String ist der Name vom Spieler, bei dem das Passwort geändert wird.
     * @param passwort String ist das neue Passwort.
     */
    public void passwortReset(String name, String passwort){
        try {
            if(Datenbank.getInstance().selectNutzerKennungReg(name)) {
                Datenbank.getInstance().updatePasswort(name, passwort);
                JOptionPane.showMessageDialog(null, "Neues Passwort wurde erstellt", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Benutzername wurde nicht gefunden", "Fehler", JOptionPane.ERROR_MESSAGE);
            }

            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Datenbank wurde nicht gefunden", "Fehler", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                System.out.println(e);
            }
    }


    //----------------------------------------INNEREKLASSEN-----------------------------------------------------------


    /**
     * Diese Klasse ist für die Beschänkung der JTextfield verandwortlich.
     * So dass nur eine Maxanzahl von Zeichen eingetragen werden dürfen / können.
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
