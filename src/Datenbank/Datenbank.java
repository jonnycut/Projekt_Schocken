package Datenbank;

/**
 * Created by ehampel on 23.05.2016.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Datenbank {
    private static Datenbank datenbank;
    private static java.sql.Connection verbindung;
    private static String ip = "";

    private Datenbank() {

    }

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
            String database = "db_schocken";

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
                        "datenbank existiert nicht", e.getSQLState(), e);
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
                    "CREATE DATABASE " + database + " " +
                            "WITH OWNER = postgres " +
                            "Encoding = 'UTF-8' " +
                            "LC_COLLATE = 'German_Germany.1252' " +
                            "LC_CTYPE = 'German_Germany.1252' " +
                            "CONNECTION LIMIT = -1; "

            );
            verbindung.close();
        } catch (SQLException e) {
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
     * Diese Methode sucht ein noch offenes Spiel und liefert die SpielID
     * @return  1 für offen,2 für geschlossen,3 für abgebrochen
     * @throws SQLException
     */
    public int selectOffenesSpiel() throws SQLException {
        Statement stmt = verbindung.createStatement();

        ResultSet r = stmt.executeQuery(
                "SELECT Spiel_ID FROM t_Spiel WHERE Status ="+"'1'"
        );
        return r.getInt(1);
    }

    /**
     * Diese Methode legt ein neues Spiel in der Relation t_Spiel an.
     * Die Spiel_ID wird durch den Datentyp SERIAL automatisch hochgezählt.
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
                    "INSERT INTO t_spielleiter " +
                            " VALUES ('" + spielleiter + "')");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.executeUpdate(

                    "INSERT  INTO t_Spiel " +
                            "(Status) " +
                            " VALUES ('1')");
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    /**
     *
     * @param kennung = der Spielername der sich anmelden möchte
     * @param passwort = Kennwort des anzumeldenden Spielers
     * @return liefert falls ein Eintrag dieser Kombination match ein true ansonsten false
     * @throws SQLException
     */
    public boolean selectNutzerkennung(String kennung,String passwort) throws SQLException {

        Statement stmt = verbindung.createStatement();

        ResultSet r =stmt.executeQuery(
                "SELECT * FROM " + "t_Spieler" + " WHERE " + "Kennung = " + kennung + " AND "+ "Passwort = "+ passwort );

        if(r.next())
            return true;
        else
            return false;
    }

    /**
     * Diese Methode prüft bei der Registrierung ob ein Nutzer mit diesem Namen bereits vorhanden ist
     * @param kennung = der geünschte Nutzermname
     * @return wenn der Nutzername bereits vergeben ist liefert die Methode false, sonst true
     * @throws SQLException
     */
    private boolean selectNutzerKennungReg(String kennung) throws SQLException{
        Statement stmt = verbindung.createStatement();
        ResultSet r =stmt.executeQuery(
                "SELECT * FROM " + "t_Spieler" + " WHERE " + "Kennung = " + kennung );

        if(r.next())
            return false;
        else
            return true;
    }

    /**Diese Methode legt einen neuen Spieler in der Relation t_Spieler an.
     * @param kennung
     * @param passwort
     * @return wurde der Spieler angelegt wird 0 zurück geliefert sonst 1
     * @throws SQLException
     */
    public int insertNutzerKennung(String kennung,String passwort) throws SQLException {
        if(selectNutzerKennungReg(kennung)){
            Statement stmt = verbindung.createStatement();

            try {
                stmt.executeUpdate("INSERT INTO t_Spieler " +
                        "(Kennung, Passwort)" +
                        " VALUES "+"('"+kennung+"','"+passwort+"'");
            }
            catch (SQLException e){
                e.printStackTrace();
            }


            return 0;
        }
        else
            return 1;

    }






    //---------------------------------------------------------------------------------------------------------------------

    /**
     * Diese Methode löscht eine Tabelle, falls sie existiert, andernfalls tut sie nichts.
     *
     * @param tabellenName Der Name der Tabelle, die gelöscht werden soll.
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
        //Tabelleneinträge
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
}

