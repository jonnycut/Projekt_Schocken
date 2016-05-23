package db;

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
    private static java.sql.Connection conn;

    private Datenbank() {

    }

    public static Datenbank getInstance() throws ClassNotFoundException, SQLException {
        //wenn die Datenbank das erstemal geladen wird
        if (datenbank == null) {
            /*Datenbanktreiber laden
            Treiber in eine Directory(ExterneJARS) im Projekt ablegen
            danach rechtsKlick Add as Libary damit IntellJ den Treiber findet
             */
            Class.forName("org.postgresql.Driver");
            datenbank = new Datenbank();
        }

        //Prüfen ob eine Verbindung zum DB Server aufgebaut ist
        boolean renew = conn == null;
        //wenn keine Verbindung besteht soll diese hergestellt werden
        if (!renew)
            try {
                if (conn.isClosed()) {
                    renew = true;
                }
            } catch (SQLException e) {
                renew = true;
            }
        //wenn Verbindung besteht werden
        if (renew) {
            String host = "localhost";
            int port = 5432;
            String database = "test";

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
                conn = DriverManager.getConnection(url, props);
                conn.close();
            } catch (SQLException e) {
                throw new SQLException(
                        "Zugriff verweigert", e.getSQLState(), e);
            }
            try {
                conn = DriverManager.getConnection(url + database, props);
                System.out.println("Verbindung aufgebaut zur Datenbank:" + database);

            } catch (SQLException e) {
                throw new SQLException(
                        "Datenbank existiert nicht", e.getSQLState(), e);
            }
        }
        return datenbank;
    }
    public static Datenbank getInstance(String database) throws SQLException{
        String host = "localhost";
        int port = 5432;

        String url ="jdbc:postgresql://"+host+":"+port+"/";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","root");

        try{
            conn=DriverManager.getConnection(url,props);
            conn.createStatement().executeUpdate(
                    "CREATE DATABASE "+database);
            conn.close();
        }
        catch (SQLException e){
            throw new SQLException("Zugriff verweigert", e.getSQLState());
        }
        try {
            getInstance();
        }
        catch (ClassNotFoundException e){}
        einlesenScript();
        return  datenbank;
    }
    private static void einlesenScript() throws SQLException {
        try(BufferedReader br = new BufferedReader(new FileReader(
                Datenbank.class.getResource("init.sql").getFile())))
        {
            String sqlInstruction ="";
            String zeile ;
            while((zeile=br.readLine())!=null){
                zeile =zeile.split("--")[0];
                sqlInstruction +=zeile+" ";
                if(sqlInstruction.trim().endsWith(";")){
                    Statement stmt =conn.createStatement();
                    stmt.execute(sqlInstruction);
                    sqlInstruction="";
                }
            }
        }
        catch (IOException e){}
    }
    public void createTable(String tableName) throws SQLException {
        Statement stmt = conn.createStatement();
        //Anweisung an die Datenbank schicken
        stmt.executeUpdate("CREATE TABLE "+tableName+" ("+
                        " name VARCHAR,"+
                        " number INT,"+
                        " PRIMARY KEY (name)"+
                        " )"
        );
    }

    /**
     * Diese Methode löscht eine Tabelle, falls sie existiert, andernfalls tut sie nichts.
     * @param tableName Der Name der Tabelle, die gelöscht werden soll.
     * @throws SQLException Wenn beim Erstellen der Verbindung ein Fehler paasiert.
     */
    public void dropIfExist(String tableName) throws SQLException {
        Statement stmt = conn.createStatement();

        try {
            stmt.executeUpdate("DROP TABLE "+tableName
            );
        } catch (SQLException e) {
            if(!e.getSQLState().equals("42P01"))
                e.printStackTrace();
        }
    }

    public void insertOrUpdate(String name, int number) throws SQLException {
        Statement stmt = conn.createStatement();

        try{
            stmt.executeUpdate(
                    "INSERT INTO test "+" VALUES ('"+name+"', "+number+")"
            );
        }
        catch(SQLException e){
            stmt.executeUpdate(
                    "UPDATE  test"+
                            " SET number="+number+
                            " WHERE name='"+name+"'"
            );
        }

    }

    public void printTable(String tableName) throws SQLException {
        Statement stmt = conn.createStatement();
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
            String s = rm.getColumnLabel(i+1);
            spalte.add(s);
            tabelle.add(spalte);
            max[i]=s.length();

        }
        //Tabelleneinträge
        if(r.next())
            do{
                for (int i = 0; i <col ; i++) {
                    String s =r.getString(i+1);
                    if(s==null)s="null";
                    tabelle.get(i).add(s);
                    max[i]=Math.max(max[i],s.length());
                }
            }while(r.next());
        r.close();
        //Ausgabe
        for (int i = 0; i <tabelle.get(0).size() ; i++) {
            String s="";
            for (int j = 0; j <col ; j++)
                s+="|"+String.format("%-"+max[j]+"s",tabelle.get(j).get(i));

            System.out.println(s+"|");
        }

    }

    public void createTableMitBlob(String tableName) throws SQLException {
        Statement stmt = conn.createStatement();
        //Anweisung an die Datenbank schicken
        stmt.executeUpdate("CREATE TABLE "+tableName+" ("+
                        " name VARCHAR,"+
                        " blob BYTEA,"+
                        " PRIMARY KEY (name),"+
                        " FOREIGN KEY (name) REFERENCES test(name)"+
                        " )"
        );
    }

    public void insertOrUpdateBlob(String name,InputStream is) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet r = stmt.executeQuery(
                "SELECT name"+
                        " FROM blob"+
                        " WHERE name='"+name+"'"

        );

        if(r.next()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE blob" +
                            " SET blob= ?" +
                            " WHERE name= '" +name + "'");
            ps.setBinaryStream(1, is);
            ps.executeUpdate();
        }

        else{
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO blob VALUES (?,?)"
            );
            ps.setString(1, name);
            ps.setBinaryStream(2, is);
            ps.executeUpdate();
        }

    }

    public InputStream  getBlob(String name) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet r = stmt.executeQuery(
                "SELECT blob"+
                        " FROM blob"+
                        " WHERE name='"+name+"'"
        );
        if(r.next())
            return r.getBinaryStream(1);
        return null;
    }
}

