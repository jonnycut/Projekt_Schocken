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

    private GUI gui; // Die aktuelle GUI
    private String zeile; // Die Nachricht/Steuersignal für den Chat
   private BufferedWriter writer; // writer für die Steuersignale


    /**
     * Der Konstruktor erzeugt einen Client, der sich mit dem Server verbindet.
     * @param gui GUI ist die aktuelle GUI.
     */
    public Client(String ip,GUI gui) {
        this.gui=gui;

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
            new Thread(){
                public  void run(){
                    try {
                        String zeile;
                        while ((zeile = reader.readLine()) != null){
                            if(zeile.contains("#")){
                                gui.getSpielfeld().netzwerkUpdate(zeile.substring(1));
                            }

                            else
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
            while ((zeile = br.readLine()) != null){
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
     * @param info String - StatusInfo, die im Infobereich angezeigt wird
     */
    public void sendeUpdate(String info){

        try {
            writer.write("#"+info);
            writer.newLine();
            writer.flush();
            System.out.println("UpdateSignal gesendet");
        } catch (IOException e) {
            System.out.println("UpdateSignal nicht moeglich");
            e.printStackTrace();
        }
    }
}
