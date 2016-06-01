package gui;

import Grafik.Grafik;
import netzwerk.Client;
import netzwerk.Netzwerk;
import spiel.Spieler;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by U.F.O on 23.05.2016.
 *
 * @author DFleuren
 */
public class GUI extends JFrame {

    //ToDo fehlende Attribute und Konstruktor Kommentieren

    /**
     * Zustand der GUI um die verschiedenen Ansichen zu schalten
     */
    private int zustand = 1;

    /**
     * Das GrundPanel
     */
    private JPanel jp = new JPanel(new CardLayout());

    /**
     * Der Besitzer der GUI
     */
    private String besitzerName = "";

    /**
     * Der Anmeldebildschirm
     */
    private Anmeldung anmeldung;

    /**
     * Der Registrierungsbildschirm
     */
    private Registrierung registrierung;

    /**
     * Der Administrationsbildschirm
     */
    private Administration administration;

    //private Statistik statistik = new Statistik();

    /**
     * Der Spielbildschirm
     */
    private Spielfeld spielfeld;

    /**
     * Das Netzwerk der GUI
     */
    private Netzwerk netzwerk;

    /**
     * Der NetzwerkClient der GUI
     */
    private Client client;


    public GUI() {

        super("Schocken, das Würfelspiel für zwischendurch!");

        spielfeld = new Spielfeld(this);
        anmeldung = new Anmeldung(this);
        registrierung = new Registrierung(this);
        administration = new Administration(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel start = new JPanel(new BorderLayout());

        JButton jb = new JButton();
        jb.setBackground(Color.BLACK);
        jb.setBorder(new LineBorder(null));

        ActionListener acl = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                updateView();
            }
        };

        jb.addActionListener(acl);
        jb.setIcon(Grafik.TISCH_LOGO);
        start.add(jb, BorderLayout.CENTER);

        JPanel anmeldungP = anmeldung;
        JPanel registrierungP = registrierung;
        JPanel administrationP = administration;
        JPanel spielfeldP = spielfeld;


        jp.add(start, "Start");
        jp.add(anmeldungP, "Anmeldung");
        jp.add(registrierungP, "Registrierung");
        jp.add(administrationP, "Administration");
        jp.add(spielfeldP, "Spielfeld");

        add(jp);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //------------------------------------------METHODEN---------------------------------------------------------------

    /**
     * <pre>
     * Aktualisiert die Ansicht der GUI
     * Je nach Zustand werden die verschiedenen CardLayouts durchgeschaltet
     *
     * Im Zustand 6 wird das Spiel gestartet.
     * Hierzu wird die Spielfeld.updateTeilnehmerListe(); genutzt, das Spielfeld angezeigt und
     * über die Netzwerk Client das UpdateSignal mit der Information, welcher Spieler gerade hinzugekommen
     * ist gesendet
     * </pre>
     *
     * @param
     * @see Client#sendeUpdate(String)
     * @see Spielfeld#updateTeilnehmerListe()
     */

    public void updateView() {

        switch (zustand) {

            case 1:
                ((CardLayout) jp.getLayout()).show(jp, "Anmeldung");
                break;
            case 2:
                //ToDo: Dinge die in der Anmeldung stattfinden
                System.out.println("Test Anmeldung");
                break;
            case 3:
                ((CardLayout) jp.getLayout()).show(jp, "Registrierung");
                zustand = 4;
                break;
            case 4:
                //ToDo: Statistik vom Spieler
                break;
            case 5:
                ((CardLayout) jp.getLayout()).show(jp, "Administration");
                break;
            case 6:
                //spielfeld.updateTeilnehmerListe();
                sendeUpdateSignal(besitzerName + "  ist dem Spiel begetreten");
                jp.add(spielfeld, "Spielfeld");
                ((CardLayout) jp.getLayout()).show(jp, "Spielfeld");
                break;
            case 7:
                //ToDo: Statistik vom Gewinner und Verlierer oder vom Spiel
                break;
            case 666:
                jp.remove(spielfeld);
                jp.add(spielfeld, "Spielfeld");
                ((CardLayout) jp.getLayout()).show(jp, "Spielfeld");
                break;
        }
    }

    /**
     * Setter für das Netzwerk
     *
     * @param netzwerk Netzwerk (Object) der GUI
     * @see Netzwerk
     */
    public void setNetzwerk(Netzwerk netzwerk) {

        this.netzwerk = netzwerk;
    }

    /**
     * Getter für das Netzwerk der GUI
     * Für eine spätere Änderung.
     * @return Netzwerk Object
     * @see Netzwerk
     */
    public Netzwerk getNetzwerk() {

        return netzwerk;
    }

    /**
     * Getter für das Spielfeld der GUI
     *
     * @return Spielfeld Object
     * @see Spielfeld
     */
    public Spielfeld getSpielfeld() {

        return spielfeld;
    }

    /**
     * Setter für das Spielfeld der GUI
     *
     * @param spielfeld Object
     * @see Spielfeld
     */
    public void setSpielfeld(Spielfeld spielfeld) {

        this.spielfeld = spielfeld;
    }

    /**
     * Setzt den Zustand der GUI
     *
     * @param zustand int {1-7]}
     */
    public void setZustand(int zustand) {

        this.zustand = zustand;
    }

    /**
     * Liefert den Besitzer der GUI
     *
     * @return String - Kennung des Besitzers
     */
    public String getBesitzerName() {

        return besitzerName;
    }

    /**
     * Setzt den Besitzer der GUI
     *
     * @param besitzerName String - Kennung des Besitzers
     */
    public void setBesitzerName(String besitzerName) {

        this.besitzerName = besitzerName;
    }

    /**
     * <pre>
     * Sofern ein Client vorhanden ist, wird das Updatesignal mit einer Info
     * über das Netzwerk gesendet.
     *
     * Nutzt die Client.sendeUpdate(String)
     * </pre>
     *
     * @param info - String - Information, welche im Infobereich angezeigt wird
     * @see Client#sendeUpdate(String)
     */
    public void sendeUpdateSignal(String info) {
        if (this.client == null)
            System.out.println("client nicht gefunden");
        else
            this.client.sendeUpdate(info);
    }

    /**
     * Setzt den Client der GUI
     *
     * @param client Object
     * @see Client
     */
    public void setClient(Client client) {

        this.client = client;
    }
}
