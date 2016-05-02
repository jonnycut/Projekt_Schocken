package spiel;

import java.util.Date;

/**
 * Created by KNapret on 02.05.2016.
 */
public class Spiel {

    private Spieler teilnehmer[];
    //private Spielfeld spielfeld;
    private Spieler besitzer;
    private String ip;
    private Date startZeit;
    private Spieler verlierer1 = null;
    private Spieler verlierer2 = null;
    private Spieler verlierer = null;
    private int runden=0;
    private Stock stock;

    public Spiel(){
        //ToDo Constructor Spiel
    }

    public void setVerlierer(int haelfte, Spieler spieler){

        switch (haelfte){

            case 1:
                this.verlierer1 = spieler;
                break;
            case 2:
                this.verlierer2 = spieler;
                break;
            case 3:
                this.verlierer = spieler;
                break;
            default:
                System.out.println("Fehler bei Rundenzuordnung");
                return;
        }


    }
}
