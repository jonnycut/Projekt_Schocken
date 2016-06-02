package Datenbank;

/**
 * Created by U.F.O. on 23.05.2016.
 *
 * @author EHampel
 */


import spiel.Spieler;
import spiel.Wuerfel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;


public class Datenbank {
    private static Datenbank datenbank;
    private static Connection verbindung;
    private static String ip = "localhost";

    public Datenbank() {

    }

    /**
     * Diese Methode fragt per JDialog ab ob ein Datenbank angelegt werden soll oder ob mann sich mit
     * ein Datenbank verbinden möchte.
     * Beim anlegen einer Datenbank kommt eine fiktive ProgressBar als Information das die DB erstellt wird.
     * Beim verbinden wird die IP-Addresse der Datenbank gefordert mit der man sich verbinden möchte.
     *
     * @author DFleuren
     */
    public static void dbErstellen() {
        Datenbank db = null;

        String[] optionen = {"Anlegen", "Verbinden", "Abbrechen"};

        int n = JOptionPane.showOptionDialog(null,
                "Möchten Sie eine Datenbank anlegen oder sich mit einer verbinden?",
                "Willkomen bei den Spiel Schocken",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, optionen, optionen[0]);

        if (n == JOptionPane.YES_OPTION) {

            Thread ladebalkenThread = new Thread(new Runnable() {
                public void run() {

                    JProgressBar pb = new JProgressBar(0, 100);
                    pb.setPreferredSize(new Dimension(300, 20));
                    pb.setString("Erstelle DB ...");
                    pb.setStringPainted(true);
                    pb.setValue(0);
                    pb.setIndeterminate(true);

                    JPanel JPCenter = new JPanel();
                    JPCenter.add(pb);

                    JDialog dialog = new JDialog((JFrame) null, "Datenbank wird erstellt");
                    dialog.setModal(false);
                    dialog.add(JPCenter, BorderLayout.CENTER);
                    dialog.pack();
                    dialog.setVisible(true);

                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    dialog.setLocationRelativeTo(null);
                    dialog.setLocation((dim.width - dialog.getSize().width) / 2,
                            (dim.height - dialog.getSize().height) / 2);
                    dialog.toFront();

                    int counter = 0;

                    while (!Thread.currentThread().interrupted()) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            dialog.dispose();
                            Thread.currentThread().interrupt();
                        }
                        pb.repaint();
                        counter++;
                        if (counter == 40) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    dialog.dispose();
                }
            });

            ladebalkenThread.start();

            try {
                db = Datenbank.getInstance();
            } catch (ClassNotFoundException e) {
                System.out.println("Datenbanktreiber nicht gefunden");
            } catch (SQLException e) {
                if (e.getMessage().startsWith("Datenbank existiert nicht"))
                    try {
                        db = Datenbank.getInstance("db_schocken2");
                    } catch (SQLException e1) {
                        System.out.println(e1.getMessage());
                        e1.printStackTrace();
                    }
            }
        }

        if (n == JOptionPane.NO_OPTION) {
            JLabel jLText = new JLabel("IP Adresse des DB Servers");
            JTextField jTipAdd = new JTextField();
            Object[] ipAdd = {jLText, jTipAdd};
            int eingabe = JOptionPane.showConfirmDialog(null, ipAdd, "Bitte IP Adresse eingeben", JOptionPane.OK_CANCEL_OPTION);

            if (eingabe == JOptionPane.OK_OPTION) {
                ip = jTipAdd.getText();
                System.out.println(ip);
                try {
                    getInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Auf diesem Server existiert keine Datenbank", "Fehler", JOptionPane.ERROR_MESSAGE);
                    ip = "localhost";
                    dbErstellen();
                }
            }

            if (eingabe == JOptionPane.CANCEL_OPTION) {
                System.exit(0);
            }
        }

        if (n == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }
    }


//    public static void dbErstellen() {
//        Datenbank db = null;
//        try {
//            db = Datenbank.getInstance();
//        } catch (ClassNotFoundException e) {
//            System.out.println("Datenbanktreiber nicht gefunden");
//        } catch (SQLException e) {
//            if (e.getMessage().startsWith("Datenbank existiert nicht"))
//                try {
//                    db = Datenbank.getInstance("db_schocken2");
//                } catch (SQLException e1) {
//                    System.out.println(e1.getMessage());
//                    e1.printStackTrace();
//                }
//
//        }
//    }

    public static Datenbank getInstance() throws ClassNotFoundException, SQLException {
        //wenn die datenbank das erstemal geladen wird
        if (datenbank == null) {
            /*Datenbanktreiber laden
            Treiber in eine Directory(ExterneJARS) im Projekt ablegen
            danach rechtsKlick Add as Libary damit IntellJ den Treiber findet
             */
            Class.forName("org.postgresql.Driver");
            datenbank = new Datenbank();
        }

        //Prüfen ob eine Verbindung zum DB Server aufgebaut ist
        boolean renew = verbindung == null;
        //wenn keine Verbindung besteht soll diese hergestellt werden
        if (!renew)
            try {
                if (verbindung.isClosed()) {
                    renew = true;
                }
            } catch (SQLException e) {
                renew = true;
            }
        //wenn Verbindung besteht werden
        if (renew) {
            String host = ip;
            int port = 5432;
            String database = "db_schocken2";

            try {
                Socket socket = new Socket(host, port);
                socket.close();
            } catch (IOException e) {
                throw new SQLException("Server nicht erreichbar", "08001", e);
            }
            String url = "jdbc:postgresql://" + host + ":" + port + "/";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "root");
            try {
                verbindung = DriverManager.getConnection(url, props);
                verbindung.close();
            } catch (SQLException e) {
                throw new SQLException(
                        "Zugriff verweigert", e.getSQLState(), e);
            }
            try {
                verbindung = DriverManager.getConnection(url + database, props);
                System.out.println("Verbindung aufgebaut zur datenbank:" + database);

            } catch (SQLException e) {
                throw new SQLException(
                        "Datenbank existiert nicht", e.getSQLState(), e);
            }
        }
        return datenbank;
    }

    public static Datenbank getInstance(String database) throws SQLException {
        String host = ip;
        int port = 5432;

        String url = "jdbc:postgresql://" + host + ":" + port + "/";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "root");

        try {
            verbindung = DriverManager.getConnection(url, props);
            verbindung.createStatement().executeUpdate(
                    "CREATE DATABASE " + database +
                            "  WITH OWNER = postgres " +
                            "  Encoding = 'UTF-8' " +
                            "  LC_COLLATE = 'German_Germany.1252' " +
                            "  LC_CTYPE = 'German_Germany.1252' " +
                            "  CONNECTION LIMIT = -1; "

            );
            verbindung.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Zugriff verweigert", e.getSQLState());
        }
        try {
            getInstance();
        } catch (ClassNotFoundException e) {
        }
        einlesenScript();
        return datenbank;
    }

    public static boolean dropDB(String dbName) {
        String host = ip;
        int port = 5432;

        String url = "jdbc:postgresql://" + host + ":" + port + "/";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "root");
        try {
            verbindung.close();
            verbindung = DriverManager.getConnection(url, props);
            verbindung.createStatement().executeUpdate(
                    "DROP DATABASE " + dbName);
            verbindung.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "" + e, "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

//----------------------------------------------------------------------------------------------------------------------

    /**
     * Methode zum Anmelden
     *
     * @param kennung  = der Spielername der sich anmelden möchte
     * @param passwort = Kennwort des anzumeldenden Spielers
     * @return liefert falls ein Eintrag dieser Kombination match ein true ansonsten false
     * @throws SQLException
     */
    public boolean selectNutzerkennung(String kennung, String passwort) throws SQLException, UnknownHostException {
        Statement stmt = verbindung.createStatement();
        InetAddress ia[] = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
        String ip = ia[1].getHostAddress();


        try {
            stmt.executeUpdate("Update t_spieler SET ip = '" + ip + "' WHERE kennung = '" + kennung + "'");
            System.out.println("update IP :" + ip);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet r = stmt.executeQuery(
                String.format("SELECT * FROM t_Spieler WHERE Kennung ='%s' AND Passwort ='%s'", kennung, passwort));
        System.out.println("access");
        return r.next();

    }

    /**
     * Diese Methode prüft bei der Registrierung ob ein Nutzer mit diesem Namen bereits vorhanden ist
     *
     * @param kennung = der gewünschte Nutzermname
     * @return wenn der Nutzername bereits vergeben ist liefert die Methode false, sonst true
     * @throws SQLException
     */
    public boolean selectNutzerKennungReg(String kennung) throws SQLException {
        Statement stmt = verbindung.createStatement();
        ResultSet r = stmt.executeQuery(
                "SELECT * FROM " + "t_Spieler" + " WHERE " + "Kennung = " + "'" + kennung + "'");

        return r.next();
    }

    /**
     * Diese Methode legt einen neuen Spieler in der Relation t_Spieler an.
     *
     * @param kennung Spielername max. 30 Zeichen
     * @param passwort Kennwort max. 30 Zeichen
     * @throws SQLException
     */
    public void insertNutzerKennung(String kennung, String passwort) throws SQLException {

        Statement stmt = verbindung.createStatement();

        try {
            stmt.executeUpdate(String.format("INSERT INTO t_Spieler (Kennung, Passwort) VALUES ('%s','%s')", kennung, passwort));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ermöglicht die Teilnahme an einem offenen Spiel beim hinzufügen des 8 Spielers wird
     * die Spalte Status in der Relation t_Spiel automatisch auf 2(begonnen) gesetzt. Ist kein oddenes Spiel vorhanden
     * wird dieser Teilnehmer zum Spielleiter und öffnet ein neues Spiel
     *
     * @param teilnehmer = Kennung des Spielers
     * @throws SQLException
     */
    public void insertTeilnehmer(String teilnehmer) throws SQLException {
        int spielID = selectOffenesSpiel();
        if (spielID == 0)
            insertSpiel(teilnehmer);
        else {
            Statement stmt = verbindung.createStatement();
            ResultSet r = stmt.executeQuery("SELECT count(*) from t_ist_client WHERE fk_t_spiel_spiel_id= " + spielID);

            if (r.next())
                System.out.println("Sie sind Spieler Nummer: " + (r.getInt(1) + 1));
            if (r.getInt(1) < 7) {

                try {
                    stmt.executeUpdate(String.format("INSERT INTO t_ist_client  VALUES ('%s','%d')", teilnehmer, spielID));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    stmt.executeUpdate(String.format("INSERT INTO t_ist_client  VALUES ('%s','%d')", teilnehmer, spielID));
                    stmt.executeUpdate("UPDATE t_spiel SET status = 2 WHERE spiel_id=" + spielID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    /**
     * Sucht in der Relation t_Spiel nach dem derzeitigen offenen Spiel und verknüpft die Relation t_Spieler über die
     * Spielleiterkennung
     *
     * @return Liefert die IP Adresse des Serverrechner
     * @throws SQLException
     */
    public String selectServerIP() throws SQLException {
        Statement stmt = verbindung.createStatement();

        ResultSet r = stmt.executeQuery("SELECT IP FROM t_Spieler LEFT JOIN t_Spiel ON Kennung=fk_t_spielleiter_kennung WHERE status=1 ");

        String ip = null;
        while (r.next())
            ip = r.getString(1);

        return ip;
    }

    /**<pre>
     * Diese Methode holt sich die Würfel eines Durchgangs aus der Datenbank,
     * erstellt daraus neue Würfel Objekte und liefert diese in einem Array zurück
     * </pre>
     *
     *
     * @param kennung
     * @return wuerfel Wuerfel[] - Array mit den 3 Wüfelobjekten (wuerfel{w1,w2,w3})
     */
    public Wuerfel[] selectDurchgang(String kennung) throws SQLException, IOException, ClassNotFoundException {
        Statement stmt = verbindung.createStatement();

        Wuerfel[] wuerfelArray = new Wuerfel[3];
        int stelle = 0;

        int spielID = selectSpielID(kennung);
        int haelfte = selectAktuelleHaelfte(spielID);
        int rundenNr = selectAktuelleRunde(spielID);

        ResultSet rS = stmt.executeQuery("SELECT wuerfel1, wuerfel2, wuerfel3 FROM t_durchgang WHERE fk_t_spieler_kennung ='"+kennung+
                "' AND fk_t_runde_rundennr ="+rundenNr+
                " AND fk_t_hälfte_art="+haelfte+
                " AND fk_t_spiel_spiel_id="+spielID);


        while(rS.next()){
            ObjectInputStream oIS = new ObjectInputStream(new ByteArrayInputStream(rS.getBytes(stelle+1)));
            wuerfelArray[stelle] = (Wuerfel) oIS.readObject();
            System.out.println("Wuerfel "+stelle+"ist erstellt: "+wuerfelArray[stelle]);
            stelle++;
        }
        System.out.println("Bin aus der Schleife raus!");
        return wuerfelArray;
    }
    /**
     * <pre>
     * Methode zum anlegen einer Hälfte wobei der Stock initial in der DB auf 13 gesetzt wird,
     * wird eine neue Hälfte erstellt werden automatisch die Strafpunkte der Mitspieler auf 0 gesetzt
     * Es wird ermittelt welche Hälfte im Moment in der DB vorhanden ist -->nutzt selectAktuelleHaelfte
     *     - Dieser Wert wird genutz um den letzten Rundenverlierer zu bekommen und als Hälftenverlierer eingesetzt -->nutzt insertHälftenVerlierer
     *     - Dieser wird dann automatisch der Beginner der neuen Hälfte
     * Die erste Runde in dieser Hälfte wird direkt mit angelegt
     * @param spielID zur Inendifizierung des Spiels
     * @see Datenbank#selectAktuelleHaelfte(int)
     * @see Datenbank#inserthaelftenVerlierer(String, int, int)
     * @throws SQLException
     * </pre>
     */
    public void insertHaelfte(int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        int abgeschlosseneHaelfte = selectAktuelleHaelfte(spielID);
        int neueHaelfte = selectAktuelleHaelfte(spielID) + 1;
        if (neueHaelfte > 1) {
            ResultSet r = stmt.executeQuery("SELECT verlierer FROM t_runde WHERE fk_t_spiel_spiel_id = " + spielID + " AND fk_t_hälfte_art=" + abgeschlosseneHaelfte);
            if (r.next()) {
                inserthaelftenVerlierer(r.getString(1), spielID, abgeschlosseneHaelfte);
            }
        }
        try {
            stmt.executeUpdate("Update t_spieler SET Strafpunkte = 0 Where Kennung IN(Select fk_t_spieler_kennung from " +
                    "((t_Spiel INNER JOIN t_hälfte ON t_hälfte.fk_t_spiel_spiel_id=" + spielID + ")INNER JOIN t_ist_client " +
                    "ON t_ist_client.fk_t_spiel_spiel_id=" + spielID + "))");

            stmt.executeUpdate("INSERT INTO t_hälfte (fk_t_spiel_spiel_id,art) VALUES (" + spielID + "," + neueHaelfte + ")");
            insertersteRunde(spielID);
            stmt.executeUpdate("Update t_runde SET rundennr=1 WHERE fk_t_spiel_spiel_id=" + spielID + " AND fk_t_hälfte_art=" + neueHaelfte);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reduziert die Zahl der Chips auf dem Stock um die Anzahl der Strafpunkte die ein Spieler erhält
     *
     * @param strafpunkte - abzuziehende Punkte vom Stock
     * @param spielID     - in welchem spiel soll der Stock geupdatet  werden
     * @throws SQLException
     */
    public void updateStock(int strafpunkte, int spielID) throws SQLException {
       int haelftenArt=selectAktuelleHaelfte(spielID);
        Statement stmt = verbindung.createStatement();
        try {
            stmt.executeUpdate("Update t_hälfte set Stock= Stock - " + strafpunkte + " Where (fk_t_spiel_spiel_id = " + spielID + " AND art = " + haelftenArt + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int selectStockStatus(int spielID) throws SQLException {
        int haelfte = selectAktuelleHaelfte(spielID);
        Statement stmt = verbindung.createStatement();
        ResultSet r = stmt.executeQuery("SELECT stock FROM t_hälfte WHERE fk_t_spiel_spiel_id="+spielID+" AND art = "+haelfte);

        if(r.next())
            return r.getInt(1);
        else
            return -1;

    }

    /**
     * prüft in welchem Spiel sich ein Spieler befindet
     *
     * @param kennung des Spielers
     * @return spielID des Spiels in dem sich der Spieler befindet
     * @throws SQLException
     */
    public int selectSpielID(String kennung) throws SQLException {
        Statement stmnt = verbindung.createStatement();

        ResultSet r = stmnt.executeQuery("SELECT fk_t_spiel_spiel_id from t_ist_client WHERE fk_t_spieler_kennung = '" + kennung + "'");
        int spielID = 0;
        if (r.next()) {
            spielID = r.getInt(1);
        }
        return spielID;
    }

    /**
     * über die SpielID kann abgefragt werden in welcher Hälfte sich das Spiel aktuell befindet
     *
     * @param spiel_ID welches Spiel soll nach der aktuellen Hälfte gefragt werden
     * @return 1 für erste Hälfte , 2 für zweite Hälfte ,3 für Finale
     * @throws SQLException
     */
    public int selectAktuelleHaelfte(int spiel_ID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        int aktuelleHaelfte = 0;

        ResultSet r = stmt.executeQuery("Select art from t_hälfte where fk_t_spiel_spiel_id = " + spiel_ID + " Order By art DESC limit 1");

        if (r.next()) {
            aktuelleHaelfte = r.getInt(1);

        }

        return aktuelleHaelfte;
    }

    /**
     * einen Spieler auf aktiv setzen erst dann ist der Spieler dran
     *
     * @param kennung vom Spieler der Aktiv geschalten werden soll
     * @throws SQLException
     */
    public void insertAktiverSpieler(String kennung) throws SQLException {
        Statement stmt = verbindung.createStatement();

        try {
            stmt.executeUpdate("UPDATE t_spieler SET Aktiv=TRUE WHERE Kennung='" + kennung + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Methode um den Aktiven Spieler zu ermitteln. Erst werden alle Spieler die zu einem Spiel gehöhren ermittelt.danach
     * wird der aktive spieler aus der Relation gefiltert ( es kann immer nur einen pro spiel geben)
     *
     * @param spielID des Spiels aus dem man den aktiven Spieler erfahren möchte
     * @return
     * @throws SQLException
     */
    public String selectAktiverSpieler(int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        String aktiverSpieler = null;
        ResultSet r = stmt.executeQuery("SELECT Kennung FROM  (SELECT fk_t_spieler_kennung FROM t_ist_client Where fk_t_spiel_spiel_id= '" + spielID +
                                        "') AS \"Spieler im Spiel\" INNER JOIN t_spieler ON kennung=fk_t_spieler_kennung WHERE Aktiv=TRUE ");
        if (r.next())
            aktiverSpieler = r.getString(1);
        System.out.println("Spieler " + aktiverSpieler + " ist nun würfelberechtigt");
        return aktiverSpieler;
    }

    /**
     * Fügt in die Relation t_spieler das Ergebniss des auswürfeln in die Spalte Startwurf ein. Hierdurch
     * wird im weiteren ermöglicht eine Spielerreihenfolge zugenerieren
     *
     * @param kennung   Spielerkennung dessen startwurf eingetragen werden soll
     * @param startwurf Augenzahl der 3 Würfel
     * @throws SQLException
     */
    public void insertStartwurf(String kennung, int startwurf) throws SQLException {
        Statement stmt = verbindung.createStatement();
        try {
            stmt.executeUpdate("UPDATE t_spieler SET startwurf='" + startwurf + "' WHERE kennung='" + kennung + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Liefert eine Liste von allen Spielern die sich in dem selben Spiel befinden diese ist aber unortiert
     * und nur wichtig bis eine Sitzreihenfolge gefundet wurde
     *
     * @param spielID
     * @return
     * @throws SQLException
     */
    public ArrayList<String> selectSpielerImSpiel(int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT fk_t_spieler_kennung FROM t_ist_client Where fk_t_spiel_spiel_id = '" + spielID + "'");
        ArrayList<String> spielerImSpiel = new ArrayList<String>();
        ResultSetMetaData metadata = resultSet.getMetaData();
        int numberOfColumns = metadata.getColumnCount();

        while (resultSet.next()) {
            int i = 1;
            while (i <= numberOfColumns) {
                spielerImSpiel.add(resultSet.getString(i++));
            }
            System.out.println("Spieler " + resultSet.getString(1) + " zum Spiel" + spielID + " hinzugefügt");
        }
        return spielerImSpiel;
    }

    /** Diese Methode liefert die Sitzreihenfolge nach dem Auswürfeln diese ändert sich das gesamte Spiel über nicht mehr
     *
     * @param spielID um alle spieler des Spiels zu erfassen
     * @return
     * @throws SQLException
     */
    public ArrayList<String> selectStartAufstellung(int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT kennung From t_spieler where kennung IN(SELECT fk_t_spieler_kennung FROM t_ist_client where fk_t_spiel_spiel_id = " + spielID + ")order by startwurf DESC");

        ArrayList<String> startaufstellung = new ArrayList<String>();
        ResultSetMetaData metadata = resultSet.getMetaData();
        int numberOfColumns = metadata.getColumnCount();

        while (resultSet.next()) {
            int i = 1;
            while (i <= numberOfColumns) {
                startaufstellung.add(resultSet.getString(i++));
            }
            System.out.println("Der Spieler " + resultSet.getString(1) + "spielt an Platz" + i);
        }
        return startaufstellung;
    }

   /**
     * fügt der aktuellen Hälfte im Spiel eine neue Runde hinzu und fügt dieser automatisch den Beginner zu
     *
     * @param spielID zu welchem Spiel soll die Runde hinzugefügt werden
     * @throws SQLException
     */
    public void insertRunde(int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        int aktuelleRunde = selectAktuelleRunde(spielID);
        int neueRunde = aktuelleRunde + 1;
        String nächsterbeginner = selectnaechsterBeginner(spielID);
        int haelfte = selectAktuelleHaelfte(spielID);
        // insertRundenergebnis(spielID, );

        //weitere Runden erstellen
        try {
            stmt.executeUpdate("INSERT INTO t_runde (rundennr,fk_t_spiel_spiel_id,fk_t_hälfte_art,beginner) VALUES('" + neueRunde + "','" + spielID + "','" + haelfte + "','" + nächsterbeginner + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Diese Methode sucht ein noch offenes Spiel es kann immer nur ein offenes Spiel geben
     *
     * @return Liefert die Spiel_ID des offenen Spiels
     * @throws SQLException
     */
    public int selectOffenesSpiel() throws SQLException {
        Statement stmt = verbindung.createStatement();
        int offenesSpiel = 0;
        ResultSet r = stmt.executeQuery("SELECT Spiel_ID FROM t_Spiel WHERE Status =1"
        );
        if (r.next()) {
            offenesSpiel = r.getInt(1);
            System.out.println("Das aktuelle offene Spiel hat die Spiel_ID:   " + offenesSpiel);
        }

        return offenesSpiel;
    }

    /**
     * Liefert die Spielleiterkennung des eigenen Spiels
     *
     * @param spielID  um den richtigen Spielleiter zu selektieren
     * @return
     * @throws SQLException
     */
    public String selectSpielleiterKennung(int spielID) throws SQLException {
        String spielleiter = null;
        Statement stmt = verbindung.createStatement();

        ResultSet r = stmt.executeQuery("SELECT fk_t_Spielleiter_Kennung FROM t_spiel WHERE spiel_id='" + spielID + "'");
        if (r.next()) {
            spielleiter = r.getString(1);
            System.out.println("Das Spiel mit der ID " + spielID + " hat die Spielleiterkennung : " + spielleiter);
        }
        return spielleiter;
    }

    /**
     * Methode zum aktualisieren des Spielstatus
     *
     * @param spielID--> des Spiels welches akzualisiert werden soll
     * @param status-->  2 für es wird gespielt; 3 für Spiel beendet
     * @throws SQLException
     */
    public void updateSpielstatus(int spielID, int status) throws SQLException {
        Statement stmt = verbindung.createStatement();
        if(status==1){
            stmt.executeUpdate("UPDATE t_spiel SET status=2 WHERE spiel_id=" + spielID);
        }else{
            stmt.executeUpdate("UPDATE t_spiel SET status=" + status + " WHERE spiel_id=" + spielID);

            if (status == 2) {
                insertAktiverSpieler(selectersterBeginner(spielID));
                System.out.println("Das Spiel " + spielID + " wird nun gespielt");
            }
            if (status == 3) {
                updateAktiv(selectAktiverSpieler(spielID));

                System.out.println("Das Spiel " + spielID + " ist nun geschlossen");
            }
        }



    }

    /**
     * Setzt nach durchspielen einer Runde den Verlierer und den Gewinner dieser Runde
     *
     * @param spielID   aktuelles SPiel in der sich diese Runde befindet
     * @param verlierer der Spieler mit dem niedrigsten Wurf
     * @param gewinner  der Spieler mit dem höchsten Wurf
     * @throws SQLException
     */
    public void insertRundenergebnis(int spielID, String verlierer, String gewinner) throws SQLException {
        Statement stmt = verbindung.createStatement();
        int rundennr = selectAktuelleRunde(spielID);
        int art = selectAktuelleHaelfte(spielID);

        stmt.executeUpdate("UPDATE  t_runde SET  verlierer = '" + verlierer + "' gewinner = '" + gewinner + "' WHERE rundennr = " + rundennr + " AND fk_t_spiel_spiel_id = " + spielID + " AND fk_t_hälfte_art=" + art);
    }

    /**
     * Sucht sich alle Spieler die sich aktuell in einem Spiel befinden zieht sich die Reihenfolge wie die Spieler am
     * Tisch sitzen und schaltet bei diesen das Boolean Aktiv in der Datenbank weiter. Hierdurch wird eine Würfelreihenfolge
     * generiert ohne das die Panels der Spiler verändert wird
     *
     * @param spielID des aktuellen spiels um nur die Sitzreihenfolge eines Spiels zu bekommen
     * @throws SQLException
     */
    public void schalteWeiter(int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        java.util.List<String> spieler = new ArrayList<>();
        java.util.List<Boolean> spielerAktiv = new ArrayList<>();


        ResultSet rs = stmt.executeQuery("SELECT aktiv,kennung,startwurf FROM t_spieler WHERE kennung IN(SELECT fk_t_spieler_kennung FROM t_ist_client WHERE fk_t_spiel_spiel_id =7)  ORDER BY startwurf DESC");
        while (rs.next()) {
            spieler.add(rs.getString("kennung"));
            spielerAktiv.add(rs.getBoolean("aktiv"));
        }
        System.out.println(spieler.toString());
        while (!spielerAktiv.get(0)) {
            Boolean tempBool = spielerAktiv.remove(0);
            String temp = spieler.remove(0);
            spieler.add(spieler.size(), temp);
            spielerAktiv.add(spielerAktiv.size(), tempBool);
        }
        System.out.println(spieler.toString());
        updateAktiv(spieler.get(0));
        updateAktiv(spieler.get(1));
    }

    /**
     * Fügt einen Wurf des Spielers in die Datenbank ein
     * über die Kennung des Spielers wird die SspielID des Spiels in dem er sich befindet gesucht -->nutzt selectSpielID
     * Mit dieser SpielID wird die aktuelle Hälfte und RundeNr. gesucht --> nutzt selectAktuelleHaelfte und select AktuelleRunde
     * Die Würfel werden aus dem tempoären Spieler als einzelne Objekte in jeweils einen Stream geschrieben und als BYTEA in die Datenbank geschrieben
     * Im ResultSet werden die Zeilen gezählt die sich für den Spieler im aktuellen Durchgang befinden.
     *
     * @param kennung Spielerkennung vom Spieler der Würfelt
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void insertDurchgang(String kennung,Wuerfel[] wuerfelArray) throws SQLException, IOException, ClassNotFoundException {
        Statement stmt = verbindung.createStatement();
        int spielID = selectSpielID(kennung);
        int haelfte = selectAktuelleHaelfte(spielID);
        int rundennr = selectAktuelleRunde(spielID);

        Object wuerfel1 = wuerfelArray[0];
        Object wuerfel2 = wuerfelArray[1];
        Object wuerfel3 = wuerfelArray[2];


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(wuerfel1);
        oos.close();

        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        ObjectOutputStream oos2 = new ObjectOutputStream(baos2);
        oos2.writeObject(wuerfel2);
        oos2.close();

        InputStream is2 = new ByteArrayInputStream(baos2.toByteArray());

        ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
        ObjectOutputStream oos3 = new ObjectOutputStream(baos3);
        oos3.writeObject(wuerfel3);
        oos3.close();

        InputStream is3 = new ByteArrayInputStream(baos2.toByteArray());


        ResultSet r = stmt.executeQuery("SELECT count(zaehler) FROM t_durchgang" +
                " WHERE fk_t_spieler_kennung = '" + kennung + "' AND fk_t_spiel_spiel_id = " + spielID +
                " AND fk_t_hälfte_art = " + haelfte +
                " AND fk_t_runde_rundennr = " + rundennr);


        if (r.next()) {
            if (r.getInt(1) == 0) {

                PreparedStatement ps = verbindung.prepareStatement(
                        "INSERT INTO t_durchgang(fk_t_spieler_kennung, fk_t_spiel_spiel_id, fk_t_hälfte_art, fk_t_runde_rundennr, wuerfel1, wuerfel2, wuerfel3,zaehler)" +
                                " VALUES(?,?,?,?,?,?,?,?)");

                ps.setString(1, kennung);
                ps.setInt(2, spielID);
                ps.setInt(3, haelfte);
                ps.setInt(4, rundennr);
                ps.setBinaryStream(5, is);
                ps.setBinaryStream(6, is2);
                ps.setBinaryStream(7, is3);
                ps.setInt(8, 1);
                ps.executeUpdate();
            }
            if (r.getInt(1) == 1) {
                PreparedStatement ps = verbindung.prepareStatement(
                        "INSERT INTO t_durchgang(fk_t_spieler_kennung, fk_t_spiel_spiel_id, fk_t_hälfte_art, fk_t_runde_rundennr, wuerfel1, wuerfel2, wuerfel3,zaehler)" +
                                " VALUES(?,?,?,?,?,?,?,?)");

                ps.setString(1, kennung);
                ps.setInt(2, spielID);
                ps.setInt(3, haelfte);
                ps.setInt(4, rundennr);
                ps.setBinaryStream(5, is);
                ps.setBinaryStream(6, is2);
                ps.setBinaryStream(7, is3);
                ps.setInt(8, 2);
                ps.executeUpdate();
            }
            if (r.getInt(1) == 2) {
                PreparedStatement ps = verbindung.prepareStatement(
                        "INSERT INTO t_durchgang(fk_t_spieler_kennung, fk_t_spiel_spiel_id, fk_t_hälfte_art, fk_t_runde_rundennr, wuerfel1, wuerfel2, wuerfel3,zaehler)" +
                                " VALUES(?,?,?,?,?,?,?,?)");

                ps.setString(1, kennung);
                ps.setInt(2, spielID);
                ps.setInt(3, haelfte);
                ps.setInt(4, rundennr);
                ps.setBinaryStream(5, is);
                ps.setBinaryStream(6, is2);
                ps.setBinaryStream(7, is3);
                ps.setInt(8, 3);
                ps.executeUpdate();
            }
        }
    }

    //-------------------------------------------Private Methoden-----------------------------------------------------------
    /**
     * erstellt die erste Runde in einer neuen Hälfte überpüft hierbei um welche Hälfte es sich handelt um
     * automatisch den Beginner dieser Runde hinzuzufügen. Handelt es sich um die ertse Hälfte wird der Spieler der das
     * Auswürfeln gewonnen hat der Rundenbeginner.--> Nutzt hierfür die Methode selectersterBeginner .
     * Andernfalls immer der Spieler der die vorherige Runde verloren hat-->Nutzt hierfür die Methode selectnaechsterBeginner
     *
     *
     * @param spielID um die die Runde zu indentifizieren
     * @see Datenbank#selectersterBeginner(int)
     * @see Datenbank#selectnaechsterBeginner(int)
     * @throws SQLException
     */
    private void insertersteRunde(int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        int haelfte=selectAktuelleHaelfte(spielID);
        int neueRunde = selectAktuelleRunde(spielID) + 1;
        String ersterBeginner = selectersterBeginner(spielID);
        String haelftenverlierer = selectHaelftenVerlierer(spielID, haelfte - 1);

        //1.Runde erstellen
        if (haelfte == 1) {
            try {
                stmt.executeUpdate("INSERT INTO t_runde (rundennr,fk_t_spiel_spiel_id,fk_t_hälfte_art,beginner) VALUES('" + neueRunde + "','" + spielID + "','" + haelfte + "','" + ersterBeginner + "')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                stmt.executeUpdate("INSERT INTO t_runde (rundennr,fk_t_spiel_spiel_id,fk_t_hälfte_art,beginner) VALUES('" + neueRunde + "','" + spielID + "','" + haelfte + "','" + haelftenverlierer + "')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * dreht das Aktiv Boolean in der Datenbank wird
     *
     * @param kennung
     * @throws SQLException
     */
    public void updateAktiv(String kennung) throws SQLException {
        Statement stmt = verbindung.createStatement();
        boolean flag;
        ResultSet r = stmt.executeQuery("Select aktiv from t_spieler WHERE kennung = '" + kennung + "'");
        if (r.next()) {
            flag = (!r.getBoolean(1));
            stmt.executeUpdate("Update t_spieler SET aktiv = " + flag + " Where kennung = '" + kennung + "'");
        }
    }

    /**
     * @param spielID --> in welchem SPiel befindet sich die Hälfte
     * @param haelfte --> welche Hälfte
     * @return Kennung des Verlierers der vorherigen Hälfte
     * @throws SQLException
     */
    public String selectHaelftenVerlierer(int spielID, int haelfte) throws SQLException {
        String verlierer = null;
        Statement stmt = verbindung.createStatement();

        ResultSet r = stmt.executeQuery("SELECT verlierer FROM t_hälfte WHERE fk_t_spiel_spiel_id =" + spielID + " AND art=" + haelfte);

        if (r.next()) {
            verlierer = r.getString(1);
        }
        return verlierer;
    }

    /**
     * Liefert die aktuelle Runde im jeweiligen Spiel
     *
     * @param spielID
     * @return derzeitige Rundennummer
     * @throws SQLException
     */
    private int selectAktuelleRunde(int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        int rundennr = 0;
        int haelfte = selectAktuelleHaelfte(spielID);


        ResultSet r = stmt.executeQuery("SELECT max(rundennr) FROM t_runde WHERE fk_t_spiel_spiel_id= " + spielID + " AND fk_t_hälfte_art=" + haelfte);

        if (r.next()) {
            rundennr = r.getInt(1);

        }
        return rundennr;
    }

    /**
     * Liefert den Verlierer der letzten Runde
     *
     * @param spielID
     * @return den Beginner der Folgerunde
     * @throws SQLException
     */
    private String selectnaechsterBeginner(int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        int aktuelleRunde = selectAktuelleRunde(spielID);
        String kennungBeginner = null;

        ResultSet r = stmt.executeQuery("SELECT verlierer FROM t_runde WHERE fk_t_spiel_spiel_id='" + spielID + "'AND rundennr=" + aktuelleRunde);

        if (r.next()) {
            kennungBeginner = r.getString(1);
        }
        return kennungBeginner;
    }

    /**
     * liefert anhand des auswürfeln den Beginner der ersten Runde
     *
     * @param spielID
     * @return Kennung des spielers mit der höchsten Augenzahl nach dem auswürfeln
     * @throws SQLException
     */
    private String selectersterBeginner(int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        String kennungBeginner = null;
        ResultSet r = stmt.executeQuery("SELECT kennung FROM (SELECT fk_t_spieler_kennung FROM t_ist_client Where fk_t_spiel_spiel_id ='" + spielID + "' ) AS \"Spieler im Spiel\" INNER JOIN t_spieler ON fk_t_spieler_kennung=kennung ORDER by startwurf DESC limit 1");
        if (r.next()) {

            kennungBeginner = r.getString(1);
        }
        return kennungBeginner;
    }

    /**
     * Liest das Script zur Erstellung der Relationen an
     *
     * @throws SQLException
     */
    private static void einlesenScript() throws SQLException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Datenbank.class.getResourceAsStream("init.sql")))) {
            String sqlInstruction = "";
            String zeile;
            while ((zeile = br.readLine()) != null) {
                zeile = zeile.split("--")[0];
                sqlInstruction += zeile + " ";
                if (sqlInstruction.trim().endsWith(";")) {
                    Statement stmt = verbindung.createStatement();
                    stmt.execute(sqlInstruction);
                    sqlInstruction = "";
                }
            }
        } catch (IOException e) {
        }
    }

    /**
     * prüft ob der Spieler bereits berechtigt ist ein Spiel zu eröffnen
     *
     * @param kennung -> Spielleiter
     * @return true wenn der Spieler berechtigt ist ein neues Spiel anzulgen
     * @throws SQLException
     */
    private boolean prüfeSpielleiterStatus(String kennung) throws SQLException {
        Statement stmt = verbindung.createStatement();
        ResultSet r = stmt.executeQuery("SELECT * From t_spiel where fk_t_spielleiter_kennung= " + "'" + kennung + "'");

        if (r.next()) {
            ResultSet rs = stmt.executeQuery("SELECT SUM(status) FROM t_spiel WHERE fk_t_spielleiter_kennung=" + "'" + kennung + "'");

            if (rs.next()) {
                if (rs.getInt(1) % 3 == 0) {
                    return true;
                } else
                    return false;
            }
        }
        return true;
    }

    /**
     * Diese Methode legt ein neues Spiel in der Relation t_Spiel an.Die Spiel_ID wird durch den Datentyp SERIAL
     * automatisch hochgezählt.Die Kennung des Spielers wird in die Relation t_spielleiter geschrieben.
     * Der Status des Spiels wird initial auf 1 gesetzt und die Zeit des Anlegens wird mittels TIMESTAMP DEFAULT Current
     * Timestamp auf die aktuelle Zeit gesetzt.
     *
     * @param spielleiter
     * @throws SQLException
     */
    private void insertSpiel(String spielleiter) throws SQLException {

        if (prüfeSpielleiterStatus(spielleiter)) {

            Statement stmt = verbindung.createStatement();

            try {
                System.out.println("Spielleiter anlegen");
                stmt.executeUpdate("INSERT  INTO  t_spielleiter(fk_t_spieler_kennung) VALUES ('" + spielleiter + "')");


            } catch (SQLException e) {
                stmt.executeUpdate(
                        "UPDATE  t_spielleiter" +
                                " SET  Geleitete_Spiele = Geleitete_Spiele+1" +
                                " WHERE fk_t_spieler_kennung ='" + spielleiter + "'"
                );
            }
            try {
                stmt.executeUpdate(

                        String.format("INSERT  INTO t_Spiel (fk_t_Spielleiter_Kennung)  VALUES ('%s')", spielleiter));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                stmt.executeUpdate(
                        String.format("INSERT INTO t_ist_client  VALUES ('%s',(SELECT Spiel_ID from t_Spiel Where fk_t_Spielleiter_Kennung ='%s' AND status=1))", spielleiter, spielleiter));

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("is nicht");
        }
    }

    /**
     * Methode zum Einfügrn des Spielerd der die Hälfte veroren hat
     *
     * @param verlierer -->Kennung des Spielers der die letzte Runde der vorherigen Hälfte verloren hat
     * @param spielID   -->Zugehörigkeit der Hälfte
     * @param art-->1   für erste Hälfte ;2 für zweite Hälfte ;3 für Finale
     * @throws SQLException
     */
    private void inserthaelftenVerlierer(String verlierer, int spielID, int art) throws SQLException {
        Statement stmt = verbindung.createStatement();

        stmt.executeUpdate("UPDATE t_hälfte SET verlierer = '" + verlierer + "' WHERE fk_t_spiel_spiel_id = " + spielID + " AND art=" + art);

    }


    //-----------------------------------------Kai seine Methoden-------------------------------------------------------

    /**
     * <pre>
     *  Waehlt das Profilbild des Spielers mit der Uebergebenen Spielerkennung aus.
     *  Bekommt einen BinaryStream aus der Datenbank und wandelt diesen in ein Icon um,
     *  welches dann zurueckgegeben wird.
     * </pre>
     *
     * @param text - String Spielerkennung
     * @return Icon Das Profilbild des Spielers
     * @throws SQLException
     * @throws IOException
     * @see ImageIO
     */

    public Icon selectProfilBild(String text) throws SQLException, IOException {
        Statement stmt = verbindung.createStatement();
        ResultSet r = stmt.executeQuery("SELECT profilbild" +
                        " FROM t_spieler" +
                        " WHERE kennung='" + text + "'"
        );
        if (r.next()) {
            Icon icon = new ImageIcon(ImageIO.read(r.getBinaryStream(1)));
            return icon;
        }


        return null;
    }

    /**
     * <pre>
     * Fuegt ein Grafik.ICON in die Datenbank ein.
     * Wandelt das uebergebene Icon in ein BufferedImage und zeichnet das uebergebene Icon
     * mithilfe der Graphics Klasse.
     * Dieses BufferedImage wird dann als ByteArray in die Datenbank geschrieben.
     * </pre>
     *
     * @param text Name des Bildes
     * @param icon Object Grafik.ICON
     * @throws SQLException Because Fuck u
     * @throws IOException  Because Fuck u more
     * @see Graphics
     * @see BufferedImage
     */

    public void insertProfilbild(String text, Icon icon) throws SQLException, IOException {

        // Im Internet gefunden... StackOverflow...
        BufferedImage image = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
// paint the Icon to the BufferedImage.
        icon.paintIcon(null, g, 0, 0);
        g.dispose();


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Statement stmt = verbindung.createStatement();
        ResultSet r = stmt.executeQuery(
                "SELECT kennung" +
                        " FROM t_spieler" +
                        " WHERE kennung='" + text + "'"

        );

        if (r.next()) {
            PreparedStatement ps = verbindung.prepareStatement(
                    "UPDATE t_spieler" +
                            " SET profilbild= ?" +
                            " WHERE kennung= '" + text + "'");
            ps.setBinaryStream(1, is);
            ps.executeUpdate();
        } else {
            PreparedStatement ps = verbindung.prepareStatement(
                    "INSERT INTO t_spieler VALUES (?,?)"
            );
            ps.setString(1, text);
            ps.setBinaryStream(2, is);
            ps.executeUpdate();
        }

    }


    /**
     * Holt den Spieler mit der uebergebenen Spielerkennung aus der Datenbank, erstellt
     * ein neues Spieler Object, setzt Strafpunkte und die Statistik und gibt dieses zurueck.
     *
     * @param kennung Spielerkennung des gewuenschten Spielers
     * @return Spieler Object
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     * @see Spieler
     */
    public Spieler selectSpieler(String kennung) throws SQLException, IOException, ClassNotFoundException {

        int spielID = selectSpielID(kennung);
        int haelfte = selectAktuelleHaelfte(spielID);
        int rundenNr = selectAktuelleRunde(spielID);

        Statement stmt = verbindung.createStatement();
        ResultSet r = stmt.executeQuery(
                "SELECT *" +
                        " FROM t_spieler" +
                        " WHERE kennung='" + kennung + "'"
        );
        if (r.next()) {
            Spieler spieler = new Spieler(r.getString(1), new ImageIcon(ImageIO.read(r.getBinaryStream(4))));
            if (r.getString(7) != null)
                spieler.setStrafpunkte(Integer.parseInt(r.getString(7)));

            if (r.getBoolean(5))
                spieler.setAktiv(r.getBoolean(5));

            ResultSet rS = stmt.executeQuery("SELECT COUNT(*) FROM t_durchgang WHERE fk_t_spieler_kennung ='"+kennung+
                    "' AND fk_t_runde_rundennr ="+rundenNr+
                    " AND fk_t_hälfte_art="+haelfte+
                    " AND fk_t_spiel_spiel_id="+spielID);
            if(rS.next()){
                if(rS.getInt(1)!=0){
                    spieler.setWurf(selectDurchgang(kennung));
                    spieler.getBecher().setAnzahlWuerfe(r.getInt(1));
                }


            }



            return spieler;
        }
        return null;
    }

    /**
     * Fuerht ein Update der Attribute des Spielers mit der uebergebenen Kennung aus.
     *
     * @param kennung     String - Kennung des zu aktualisierenden Spielers
     * @param strafpunkte Int - Wert der zu setzenden Strafpunkte
     * @throws SQLException
     * @throws IOException
     */
    public void updateSpieler(String kennung, int strafpunkte) throws SQLException, IOException {

        PreparedStatement ps = verbindung.prepareStatement(
                "UPDATE t_spieler" +
                        "SET strafpunkte= ?" +
                        "WHERE kennung= '" + kennung + "'");
        ps.setInt(1, strafpunkte);
        //ps.setString(2, kennung);
        ps.executeUpdate();



    }

   /* public void updateSpielerWuerfel(Wuerfel[] wuerfel){

        PreparedStatement ps = verbindung.createStatement(
                "UPDATE t_spieler"+
                        "SET w1= ?"+
                        "SET w2= ?"+
                        "SET w3"=
        );
    }*/

    public void updatePasswort(String name, String passwort) throws SQLException {
        Statement stmt = verbindung.createStatement();

        stmt.executeUpdate("UPDATE t_spieler SET passwort = '" + passwort + "' WHERE kennung = '" + name + "'");
    }
}

