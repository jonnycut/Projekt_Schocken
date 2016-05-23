package Datenbank;
import Datenbank.Datenbank;

import java.sql.SQLException;

/**
 * Created by ehampel on 23.05.2016.
 */
public class DBTest {

    public DBTest(){
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
            System.out.println(e.getMessage());
            e.printStackTrace();
        }}}