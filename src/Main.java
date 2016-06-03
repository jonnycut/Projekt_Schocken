import Datenbank.Datenbank;
import gui.GUI;
import spiel.Spieler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by U.F.O. on 23.05.2016.
 *
 * @author KNapret(Uwe) / DFleuren(Flurry) / EHampel(Opa)
 */
public class Main {

    /**Die Methode erzeugt die GUI und die Datenbank oder die Verbindung zu einer Datenbank
     *
     * @param args Mögliche Parameter
     */
    public static void main(String[] args) {

        Datenbank.dbErstellen();
        new GUI();
    }

}

