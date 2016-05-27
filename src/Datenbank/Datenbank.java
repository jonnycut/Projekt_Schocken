package Datenbank;

/**
 * Created by ehampel on 23.05.2016.
 */

import spiel.Spieler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Datenbank {
    private static Datenbank datenbank;
    private static Connection verbindung;
    private static String ip = "localhost";

    public Datenbank() {
        
    }

  public static void dbErstellen(){
      Datenbank db =null;
      try {
          db = Datenbank.getInstance();
      }
      catch (ClassNotFoundException e) {
          System.out.println("Datenbanktreiber nicht gefunden");
      }
      catch (SQLException e){
          if(e.getMessage().startsWith("Datenbank existiert nicht"))
              try {
                  db = Datenbank.getInstance("db_schocken2");
              }
              catch (SQLException e1){
                  System.out.println(e1.getMessage());
                  e1.printStackTrace();
              }

  }}
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

        //Pr�fen ob eine Verbindung zum DB Server aufgebaut ist
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
     * Diese Methode sucht ein noch offenes Spiel es kann immer nur ein offenes Spiel geben
     * @return  Liefert die Spiel_ID des offenen Spiels
     * @throws SQLException
     */
    public int selectOffenesSpiel() throws SQLException {
        Statement stmt = verbindung.createStatement();

        ResultSet r = stmt.executeQuery(
                String.format("SELECT Spiel_ID FROM t_Spiel WHERE Status ='1'")
        );
        return r.getInt(1);
    }
    /**
     * Diese Methode legt ein neues Spiel in der Relation t_Spiel an.
     * Die Spiel_ID wird durch den Datentyp SERIAL automatisch hochgez�hlt.
     * Die Kennung des Spielers wird in die Relation t_spielleiter geschrieben
     * Der Status des Spiels ird initial auf 1 gesetzt und die Zeit des Anlegens
     * wird mittels TIMESTAMP DEFAULT Current Timestamp auf die aktuelle Zeit gesetzt.
     * @param spielleiter
     * @throws SQLException
     */
    public void insertSpiel(String spielleiter) throws SQLException {
        Statement stmt = verbindung.createStatement();

        try {
            stmt.executeUpdate(
                    String.format("INSERT INTO t_Spielleiter  VALUES ('%s')", spielleiter));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.executeUpdate(

                    String.format("INSERT  INTO t_Spiel (Status,fk_t_Spielleiter_Kennung)  VALUES ('1','%s')", spielleiter));
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        try {
            stmt.executeUpdate(
                    String.format("INSERT INTO t_ist_client  VALUES ('%s',(SELECT Spiel_ID from t_Spiel Where fk_t_Spielleiter_Kennung ='%s'))", spielleiter, spielleiter));

        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    /**Methode zum Anmelden
     *
     * @param kennung = der Spielername der sich anmelden m�chte
     * @param passwort = Kennwort des anzumeldenden Spielers
     * @return liefert falls ein Eintrag dieser Kombination match ein true ansonsten false
     * @throws SQLException
     */
    public boolean selectNutzerkennung(String kennung,String passwort) throws SQLException {

        Statement stmt = verbindung.createStatement();

        ResultSet r =stmt.executeQuery(
                String.format("SELECT * FROM t_Spieler WHERE Kennung ='%s' AND Passwort ='%s'", kennung, passwort));

        return r.next();

    }
    /**
     * Diese Methode pr�ft bei der Registrierung ob ein Nutzer mit diesem Namen bereits vorhanden ist
     * @param kennung = der gew�nschte Nutzermname
     * @return wenn der Nutzername bereits vergeben ist liefert die Methode false, sonst true
     * @throws SQLException
     */
    public boolean selectNutzerKennungReg(String kennung) throws SQLException{
        Statement stmt = verbindung.createStatement();
        ResultSet r =stmt.executeQuery(
                "SELECT * FROM " + "t_Spieler" + " WHERE " + "Kennung = "+"'" + kennung+ "'" );

        return !r.next();
    }
    /**Diese Methode legt einen neuen Spieler in der Relation t_Spieler an.
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
     * Erm�glicht die Teilnahme an einem offenen Spiel beim hinzuf�gen des 8 Spielers wird
     * die Spalte Status in der Relation t_Spiel automatisch auf 2(begonnen) gesetzt
     * @param teilnehmer = Kennung des Spielers
     * @param spielID = ID des offenen Spiels , hier soll der Spieler hinzugef�gt werden
     * @throws SQLException
     */
    public void insertTeilnehmer(String teilnehmer,int spielID) throws SQLException {
        Statement stmt = verbindung.createStatement();
        ResultSet r =stmt.executeQuery("SELECT count(*) from t_ist_client WHERE fk_t_spiel_spiel_id= "+spielID);

        if (r.next())
            System.out.println(r.getInt(1));
        if (r.getInt(1) <7) {

            try {
                stmt.executeUpdate(String.format("INSERT INTO t_ist_client  VALUES ('%s','%d')", teilnehmer, spielID));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                stmt.executeUpdate(String.format("INSERT INTO t_ist_client  VALUES ('%s','%d')", teilnehmer, spielID));
                stmt.executeUpdate("UPDATE t_spiel SET status = 2 WHERE spiel_id=" + spielID);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }
    public String selectServerIP() throws SQLException {
        Statement stmt = verbindung.createStatement();

        ResultSet r = stmt.executeQuery("SELECT IP FROM t_Spieler LEFT JOIN t_Spiel ON Kennung=fk_t_spielleiter_kennung WHERE status=1 ");

        String ip = null;
        while (r.next())
            ip = r.getString(1);

        return ip;
    }











    //---------------------------------------------------------------------------------------------------------------------

    /**
     * Diese Methode l�scht eine Tabelle, falls sie existiert, andernfalls tut sie nichts.
     *
     * @param tabellenName Der Name der Tabelle, die gel�scht werden soll.
     * @throws SQLException Wenn beim Erstellen der Verbindung ein Fehler passiert.
     */
    public void dropIfExist(String tabellenName) throws SQLException {
        Statement stmt = verbindung.createStatement();

        try {
            stmt.executeUpdate("DROP TABLE " + tabellenName
            );
        } catch (SQLException e) {
            if (!e.getSQLState().equals("42P01"))
                e.printStackTrace();
        }
    }

    public void insertOrUpdate(String name, int number) throws SQLException {
        Statement stmt = verbindung.createStatement();

        try {
            stmt.executeUpdate(
                    "INSERT INTO test " + " VALUES ('" + name + "', " + number + ")"
            );
        } catch (SQLException e) {
            stmt.executeUpdate(
                    "UPDATE  test" +
                            " SET number=" + number +
                            " WHERE name='" + name + "'"
            );
        }

    }

    public void printTable(String tableName) throws SQLException {
        Statement stmt = verbindung.createStatement();
        ResultSet r = stmt.executeQuery(
                "SELECT * FROM " + tableName
        );
        ResultSetMetaData rm = r.getMetaData();
        //Tabellenkopf->Spaltennamen
        int col = rm.getColumnCount();
        int[] max = new int[col];
        List<List<String>> tabelle = new ArrayList<>(col);
        for (int i = 0; i < col; i++) {
            List<String> spalte = new ArrayList<>();
            String s = rm.getColumnLabel(i + 1);
            spalte.add(s);
            tabelle.add(spalte);
            max[i] = s.length();

        }
        //Tabelleneintr�ge
        if (r.next())
            do {
                for (int i = 0; i < col; i++) {
                    String s = r.getString(i + 1);
                    if (s == null) s = "null";
                    tabelle.get(i).add(s);
                    max[i] = Math.max(max[i], s.length());
                }
            } while (r.next());
        r.close();
        //Ausgabe
        for (int i = 0; i < tabelle.get(0).size(); i++) {
            String s = "";
            for (int j = 0; j < col; j++)
                s += "|" + String.format("%-" + max[j] + "s", tabelle.get(j).get(i));

            System.out.println(s + "|");
        }

    }

    public void createTableMitBlob(String tableName) throws SQLException {
        Statement stmt = verbindung.createStatement();
        //Anweisung an die datenbank schicken
        stmt.executeUpdate("CREATE TABLE " + tableName + " (" +
                        " name VARCHAR," +
                        " blob BYTEA," +
                        " PRIMARY KEY (name)," +
                        " FOREIGN KEY (name) REFERENCES test(name)" +
                        " )"
        );
    }

    public void insertOrUpdateBlob(String name, InputStream is) throws SQLException {
        Statement stmt = verbindung.createStatement();
        ResultSet r = stmt.executeQuery(
                "SELECT name" +
                        " FROM blob" +
                        " WHERE name='" + name + "'"

        );

        if (r.next()) {
            PreparedStatement ps = verbindung.prepareStatement(
                    "UPDATE blob" +
                            " SET blob= ?" +
                            " WHERE name= '" + name + "'");
            ps.setBinaryStream(1, is);
            ps.executeUpdate();
        } else {
            PreparedStatement ps = verbindung.prepareStatement(
                    "INSERT INTO blob VALUES (?,?)"
            );
            ps.setString(1, name);
            ps.setBinaryStream(2, is);
            ps.executeUpdate();
        }

    }

    public InputStream getBlob(String name) throws SQLException {
        Statement stmt = verbindung.createStatement();
        ResultSet r = stmt.executeQuery(
                "SELECT blob" +
                        " FROM blob" +
                        " WHERE name='" + name + "'"
        );
        if (r.next())
            return r.getBinaryStream(1);
        return null;
    }

    public Icon selectProfilBild(String text) throws SQLException, IOException {
        Statement stmt = verbindung.createStatement();
        ResultSet r = stmt.executeQuery(
                "SELECT profilbild" +
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
     * Fuegt ein Grafik.ICON in die Datenbank ein
     * @param text Name des Bildes
     * @param icon Object Grafik.ICON
     * @throws SQLException Because Fuck u
     * @throws IOException Because Fuck u more
     */
    public void insertProfilbild(String text, Icon icon) throws SQLException, IOException {


        BufferedImage image = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
// paint the Icon to the BufferedImage.
        icon.paintIcon(null, g, 0,0);
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

    public void updatePasswort(String name, String passwort) {

    }

    public Spieler selectSpieler(String kennung) throws SQLException, IOException {
        Statement stmt = verbindung.createStatement();
        ResultSet r = stmt.executeQuery(
                "SELECT *" +
                        " FROM t_spieler" +
                        " WHERE kennung='" + kennung + "'"
        );
        if (r.next()) {
           Spieler spieler = new Spieler(r.getString(1),new ImageIcon(ImageIO.read(r.getBinaryStream(4))));
            if(r.getString(7)!=null)
                spieler.setStrafpunkte(Integer.parseInt(r.getString(7)));

            return spieler;
        }


        return null;

    }
}

