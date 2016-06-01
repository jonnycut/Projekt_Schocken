package Datenbank;

/**
 * Created by U.F.O. on 23.05.2016.
 *
 * @author EHampel
 */


import spiel.Spieler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class Datenbank {
    private static Datenbank datenbank;
    private static Connection verbindung;
    private static String ip = "localhost";

    public Datenbank() {

    }

    public static void dbErstellen() {
        Datenbank db = null;

        String[] optionen = {"Anlegen", "Verbinden", "Abbrechen"};

        int n = JOptionPane.showOptionDialog(null,
                "Möchten Sie eine Datenbank anlegen oder sich mit einer verbinden?",
                "Willkomen bei den Spiel Schocken",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, optionen, optionen[0]);

        if (n == JOptionPane.YES_OPTION)
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
                    e.printStackTrace();
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
     * @param kennung
     * @param passwort
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

    /**
     * Methode zum anlegen einer Hälfte wobei der Stock initial in der DB auf 13 gesetzt wird
     * wird eine neue Hälfte erstellt werden automatisch die Strafpunkte der Mitspieler auf 0 gesetzt
     *
     * @param spielID zur Inendifizierung des Spiels
     * @throws SQLException
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
            insertersteRunde(spielID, neueHaelfte);
            stmt.executeUpdate("Update t_runde SET rundennr=1 WHERE fk_t_spiel_spiel_id=" + spielID + " AND fk_t_hälfte_art=" + neueHaelfte);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reduziert die Zahl der Chips auf dem Stock um die Anzahl der Strafpunkte die ein Spieler erhält
     *
     * @param strafpunkte - abzuziehende Punkte vom Stock
     * @param spielID     - Hälftenzugehörigkeit
     * @param haelftenArt - aktuelle Hälfte
     * @throws SQLException
     */
    public void updateStock(int strafpunkte, int spielID, int haelftenArt) throws SQLException {
        Statement stmt = verbindung.createStatement();
        try {
            stmt.executeUpdate("Update t_hälfte set Stock= Stock - " + strafpunkte + " Where (fk_t_spiel_spiel_id = " + spielID + " AND art = " + haelftenArt + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * prüft in welchem Spiel sich ein Spieler befindet
     *
     * @param kennung
     * @return spielID
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
     * @param spiel_ID
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
     * @param kennung
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
     * @param spielID
     * @return
     * @throws SQLException
     */
    public String selectAktiverSpieler(int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        String aktiverSpieler = null;
        ResultSet r = stmt.executeQuery("SELECT Kennung FROM  (SELECT fk_t_spieler_kennung FROM t_ist_client Where fk_t_spiel_spiel_id= " +
                "'" + spielID + "') AS " + "Spieler im Spiel" + " INNER JOIN t_spieler ON kennung=fk_t_spieler_kennung WHERE Aktiv=TRUE ");
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
     * Liefert eine Liste von allen Spielern die sich in dem selben Spiel befinden
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

    /**
     * Fügt der Relation t_spieler die statistik des Spielers als Object hinzu
     *
     * @param kennung
     * @param statistik
     * @throws SQLException
     * @throws IOException
     */
    public void insertStatistik(String kennung, Map statistik) throws SQLException, IOException {
        Statement stmt = verbindung.createStatement();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("statistik.ser"));
        oos.writeObject(statistik);
        oos.close();
        System.out.println("Serialisierung der Statistik");

        InputStream is = new FileInputStream("statistik.ser");
        ResultSet r = stmt.executeQuery("SELECT kennung FROM t_spieler WHERE kennung='" + kennung + "'");

        if (r.next()) {
            PreparedStatement ps = verbindung.prepareStatement("UPDATE t_spieler" +
                    " SET statistik= ?" +
                    " WHERE kennung= '" + kennung + "'");
            ps.setBinaryStream(1, is);
            ps.executeUpdate();

        }

    }

    /**
     * Liefert die Statistik der Spieler aus der DB
     *
     * @param kennung
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Map selectStatistik(String kennung) throws SQLException, IOException, ClassNotFoundException {
        Statement stmt = verbindung.createStatement();
        Map<String, Integer> statistik = null;
        ResultSet r = stmt.executeQuery(
                "SELECT statistik" +
                        " FROM t_spieler" +
                        " WHERE kennung='" + kennung + "'"
        );
        if (r.next()) {
            InputStream fis = r.getBinaryStream(1);
            ObjectInputStream ois = new ObjectInputStream(fis);
            statistik = (Map) ois.readObject();
            ois.close();
            fis.close();
        }
       /* Set set = statistik.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            System.out.print("key: " + mentry.getKey() + " & Value: ");
            System.out.println(mentry.getValue());
        }*/
        return statistik;
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
        //insertRundenVerlierer(spielID,);

        //weitere Runden erstellen
        try {
            stmt.executeUpdate("INSERT INTO t_runde (rundennr,fk_t_spiel_spiel_id,fk_t_hälfte_art,beginner) VALUES('" + neueRunde + "','" + spielID + "','" + haelfte + "','" + nächsterbeginner + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * erstellt die erste Runde in einer neuen Hälfte
     *
     * @param spielID
     * @param haelfte
     * @throws SQLException
     */
    public void insertersteRunde(int spielID, int haelfte) throws SQLException {
        Statement stmt = verbindung.createStatement();
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
     * Liefert die Spielleiterkennung eines Spiels
     *
     * @param spielID
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

    public void updateAktivZuFalse(String kennung) throws SQLException {
        Statement stmt = verbindung.createStatement();
        stmt.executeUpdate("Update t_spieler SET aktiv = false Where kennung = "+kennung);

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

        stmt.executeUpdate("UPDATE t_spiel SET status=" + status + " WHERE spiel_id=" + spielID);
        if (status == 2) {
            insertAktiverSpieler(selectersterBeginner(spielID));
            System.out.println("Das Spiel " + spielID + " wird nun gespielt");
        }
        if (status == 3) {
            updateAktivZuFalse(selectAktiverSpieler(spielID));

            System.out.println("Das Spiel " + spielID + " ist nun geschlossen");
        }

    }

    /**
     * prüft ob sich bereits 2 Spieler im Spiel befinden
     *
     * @return
     * @throws SQLException
     */
    public boolean selectStarteSpiel() throws SQLException {
        Statement stmt = verbindung.createStatement();
        boolean check = false;
        ResultSet r = stmt.executeQuery("SELECT count(*) from t_ist_client WHERE fk_t_spiel_spiel_id=" + selectOffenesSpiel());

        if (r.next()) {
            if (r.getInt(1) < 1) {
                check = true;
                System.out.println("Dein Spiel kann ab jetzt gestartet werden");
            }
        }
        return check;
    }

    public void insertRundenVerlierer(int spielID, String verlierer) throws SQLException {
        Statement stmt = verbindung.createStatement();
        int rundennr = selectAktuelleRunde(spielID);
        int art = selectAktuelleHaelfte(spielID);

        stmt.executeUpdate("UPDATE  t_runde SET  verlierer = '" + verlierer + "' WHERE rundennr = " + rundennr + " AND fk_t_spiel_spiel_id = " + spielID + " AND fk_t_hälfte_art=" + art);
    }
//    public void updateStrafpunkte

//    public void updateStrafpunkte

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

            if (r.getBytes(8) != null) {
                InputStream fis = r.getBinaryStream(8);
                ObjectInputStream ois = new ObjectInputStream(fis);
                HashMap<String, Integer> statistik = (HashMap<String, Integer>) ois.readObject();
                ois.close();
                fis.close();
                spieler.setStatistik(statistik);
            }


            return spieler;
        }
        return null;
    }

    /**
     * Fuerht ein Update der Attribute des Spielers mit der uebergebenen Kennung aus.
     * Nutzt die insertStatistik(Map) Methode
     *
     * @param kennung     String - Kennung des zu aktualisierenden Spielers
     * @param strafpunkte Int - Wert der zu setzenden Strafpunkte
     * @param statistik   Map[String, Integer] - Die zu setzende Statistik
     * @throws SQLException
     * @throws IOException
     */
    public void updateSpieler(String kennung, int strafpunkte, Map<String, Integer> statistik) throws SQLException, IOException {

        PreparedStatement ps = verbindung.prepareStatement(
                "UPDATE t_spieler" +
                        "SET strafpunkte= ?" +
                        "WHERE kennung= '" + kennung + "'");
        ps.setInt(1, strafpunkte);
        ps.setString(2, kennung);
        ps.executeUpdate();

        insertStatistik(kennung, statistik);


    }

   /* public void updateSpielerWuerfel(Wuerfel[] wuerfel){

        PreparedStatement ps = verbindung.createStatement(
                "UPDATE t_spieler"+
                        "SET w1= ?"+
                        "SET w2= ?"+
                        "SET w3"=
        );
    }*/

    public void updatePasswort(String name, String passwort) {
        //ToDo Machen
    }
//-------------------------------------------Private Methoden-----------------------------------------------------------

    /**
     * @param spielID --> in welchem SPiel befindet sich die Hälfte
     * @param haelfte --> welche Hälfte
     * @return Kennung des Verlierers der vorherigen Hälfte
     * @throws SQLException
     */
    private String selectHaelftenVerlierer(int spielID, int haelfte) throws SQLException {
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
        try (BufferedReader br = new BufferedReader(new FileReader(
                Datenbank.class.getResource("init.sql").getFile()))) {
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


}
//    //---------------------------------------------------------------------------------------------------------------------
//
//    /**
//     * Diese Methode l�scht eine Tabelle, falls sie existiert, andernfalls tut sie nichts.
//     *
//     * @param tabellenName Der Name der Tabelle, die gel�scht werden soll.
//     * @throws SQLException Wenn beim Erstellen der Verbindung ein Fehler passiert.
//     */
//    public void dropIfExist(String tabellenName) throws SQLException {
//        Statement stmt = verbindung.createStatement();
//
//        try {
//            stmt.executeUpdate("DROP TABLE " + tabellenName
//            );
//        } catch (SQLException e) {
//            if (!e.getSQLState().equals("42P01"))
//                e.printStackTrace();
//        }
//    }
//
//    public void insertOrUpdate(String name, int number) throws SQLException {
//        Statement stmt = verbindung.createStatement();
//
//        try {
//            stmt.executeUpdate(
//                    "INSERT INTO test " + " VALUES ('" + name + "', " + number + ")"
//            );
//        } catch (SQLException e) {
//            stmt.executeUpdate(
//                    "UPDATE  test" +
//                            " SET number=" + number +
//                            " WHERE name='" + name + "'"
//            );
//        }
//
//    }
//
//    public void printTable(String tableName) throws SQLException {
//        Statement stmt = verbindung.createStatement();
//        ResultSet r = stmt.executeQuery(
//                "SELECT * FROM " + tableName
//        );
//        ResultSetMetaData rm = r.getMetaData();
//        //Tabellenkopf->Spaltennamen
//        int col = rm.getColumnCount();
//        int[] max = new int[col];
//        List<List<String>> tabelle = new ArrayList<>(col);
//        for (int i = 0; i < col; i++) {
//            List<String> spalte = new ArrayList<>();
//            String s = rm.getColumnLabel(i + 1);
//            spalte.add(s);
//            tabelle.add(spalte);
//            max[i] = s.length();
//
//        }
//        //Tabelleneintr�ge
//        if (r.next())
//            do {
//                for (int i = 0; i < col; i++) {
//                    String s = r.getString(i + 1);
//                    if (s == null) s = "null";
//                    tabelle.get(i).add(s);
//                    max[i] = Math.max(max[i], s.length());
//                }
//            } while (r.next());
//        r.close();
//        //Ausgabe
//        for (int i = 0; i < tabelle.get(0).size(); i++) {
//            String s = "";
//            for (int j = 0; j < col; j++)
//                s += "|" + String.format("%-" + max[j] + "s", tabelle.get(j).get(i));
//
//            System.out.println(s + "|");
//        }
//
//    }
//
//    public void createTableMitBlob(String tableName) throws SQLException {
//        Statement stmt = verbindung.createStatement();
//        //Anweisung an die datenbank schicken
//        stmt.executeUpdate("CREATE TABLE " + tableName + " (" +
//                        " name VARCHAR," +
//                        " blob BYTEA," +
//                        " PRIMARY KEY (name)," +
//                        " FOREIGN KEY (name) REFERENCES test(name)" +
//                        " )"
//        );
//    }
//
//    public void insertOrUpdateBlob(String name, InputStream is) throws SQLException {
//        Statement stmt = verbindung.createStatement();
//        ResultSet r = stmt.executeQuery(
//                "SELECT name" +
//                        " FROM blob" +
//                        " WHERE name='" + name + "'"
//
//        );
//
//        if (r.next()) {
//            PreparedStatement ps = verbindung.prepareStatement(
//                    "UPDATE blob" +
//                            " SET blob= ?" +
//                            " WHERE name= '" + name + "'");
//            ps.setBinaryStream(1, is);
//            ps.executeUpdate();
//        } else {
//            PreparedStatement ps = verbindung.prepareStatement(
//                    "INSERT INTO blob VALUES (?,?)"
//            );
//            ps.setString(1, name);
//            ps.setBinaryStream(2, is);
//            ps.executeUpdate();
//        }
//
//    }
//
//    public InputStream getBlob(String name) throws SQLException {
//        Statement stmt = verbindung.createStatement();
//        ResultSet r = stmt.executeQuery(
//                "SELECT blob" +
//                        " FROM blob" +
//                        " WHERE name='" + name + "'"
//        );
//        if (r.next())
//            return r.getBinaryStream(1);
//        return null;
//    }
//
