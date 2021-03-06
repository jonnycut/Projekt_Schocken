package netzwerk;

import gui.GUI;

import java.io.*;
import java.net.Socket;

/**
 * Created by U.F.O. on 23.05.2016.
 * Diese Klasse ist der Client für die Netzwerkkommunikation.
 *
 * @author DFleuren / KNapret
 */
public class Client {

    /**
     * Die aktuelle GUI
     */
    private GUI gui;

    /**
     * Die Nachricht/Steuersignal für den Chat
     */
    private String zeile;
    /**
     * Der Writer für die Infos/Steuersignal oder für den Chat
     */
    private BufferedWriter writer; //


    /**
     * Der Konstruktor erzeugt einen Client, der sich mit dem Server verbindet.
     *
     * @param gui GUI ist die aktuelle GUI.
     * @param ip String mit der IP-Addresse.
     */
    public Client(String ip, GUI gui) {
        this.gui = gui;

        // Baut die Verbindung zum Server auf.
        this.gui.setClient(this);
        try (Socket socket = new Socket(ip, 5060);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(
                             socket.getInputStream()));

             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(
                             socket.getOutputStream()));

             BufferedReader br = new BufferedReader(
                     new InputStreamReader(
                             System.in));
        ) {

            // Erzeugt einen neuen Thread und liest die einkommenden Nachrichten und Steuersignale aus.
            new Thread() {
                public void run() {
                    try {
                        String zeile;
                        while ((zeile = reader.readLine()) != null) {
                            if (zeile.contains("#")) {
                                gui.getSpielfeld().updateSpielfeld(zeile.substring(1));
                            } else if (zeile.contains("@")) {
                                System.out.println("updateCounter: " + zeile.substring(1));
                                gui.getSpielfeld().setCounter(Integer.parseInt(zeile.substring(1)));
                            } else
                                System.out.println(zeile);
                        }
                    } catch (IOException e) {
                        System.out.println("Verbindung zum Server verloren!");
                        System.exit(0);
                    }
                }
            }.start();

            System.out.println("Verbindung zum Server eingerichtet!");

            this.writer = writer;

            //Hier noch einen neuen Thread drum.
            // Erzeugt eine neue Nachricht und sendet diese zum Server.
            //String zeile;
            while ((zeile = br.readLine()) != null) {
                writer.write(zeile);
                writer.newLine();
                writer.flush();
            }

        } catch
                (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sendet das UpdateSignal '#Info' ueber das Netzwerk an den Server
     * Falls das Schreiben in den Stream fehlschlägt, erscheint eine Consolenfehlermeldung.
     *
     * @param info String - StatusInfo, die im Infobereich angezeigt wird
     */
    public void sendeUpdate(String info) {

        try {
            writer.write("#" + info);
            writer.newLine();
            writer.flush();
            System.out.println("UpdateSignal gesendet");
        } catch (IOException e) {
            System.out.println("UpdateSignal nicht moeglich");
            e.printStackTrace();
        }
    }

    /**
     * Sendet daas CounterUpdateSymbol über das Netzwerk an den Server
     * Gefolgt von dem Wert, auf den der Counter zu setzen ist.
     * Falls das Schreiben in den Stream fehlschlägt, erscheint eine Consolenfehlermeldung.
     *
     * @param counter int Wert auf den der Counter gesetzt wird.
     */
    public void sendeUpdateCounter(int counter) {

        try {
            writer.write("@" + counter);
            writer.newLine();
            writer.flush();
            System.out.println("CounterSignal gesendet");
        } catch (IOException e) {
            System.out.println("CountSignal nicht möglich");
            e.printStackTrace();
        }
    }
}
