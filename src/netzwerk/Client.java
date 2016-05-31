package netzwerk;

import gui.GUI;
import java.io.*;
import java.net.Socket;

/**
 * Created by dfleuren on 23.05.2016.
 * Diese Klasse ist der Client für die Netzwerkkommunikation.
 */
public class Client {

    private GUI gui; // Die aktuelle GUI
    private String zeile; // Die Nachricht für den Chat


    /**
     * Der Konstruktor erzeugt einen Client, der sich mit dem Server verbindet.
     * @param gui GUI ist die aktuelle GUI.
     */
    public Client(String ip,GUI gui) {
        this.gui=gui;

        // Baut die Verbindung zum Server auf.
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
                                gui.getSpielfeld().netzwerkUpdate();
                            }

                            else
                                System.out.println(zeile);
                        }
                    } catch (IOException e) {
                        System.out.println("Verbindung zum Server verloren!");;
                        System.exit(0);
                    }
                }
            }.start();

            System.out.println("Verbindung zum Server eingerichtet!");

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


    public void sendeUpdate(){
        zeile = "#";
    }
}
