package netzwerk;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

/**
 * Created by U.F.O. on 23.05.2016.
 *
 * @author DFleuren
 */
public class Server {

    /**
     * Die aktuelle Liste aller angemeldeten Clients am Server
     */
    private List<User> users = new Vector<>();


    /**
     * <pre>
     * Dieser Konstructor ist für die Erstellung des Servers.
     * Läuft über den Port 5060
     * Sendet und empfängt Chatnachricht, Steuersignale oder Infos für der Spiel.
     * </pre>
     */
    public Server() {
        try {
            ServerSocket serverSocket = new ServerSocket(5060);

            new Thread() {
                public void run() {
                    while (true) {
                        try {
                            users.add(new User(
                                    serverSocket.accept()));
                        } catch (IOException e) {
                            //Verbindungsaufbau ging schief
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            System.out.println("Server eingerichtet!");

        } catch (IOException e) {
            //Kommt hier hin, wenn der Port schon belegt ist,
            //oder mann darf keinen Port öffenen.
            e.printStackTrace();
        }

    }

    /**
     * <pre>
     * Ist ein Private Methode für die Nachriten und Steuersignal Verteilung im Netzwerk.
     * </pre>
     *
     * @param user      Liste von Usern um allen die Chatnachricht oder das Steuersignal zu schicken
     * @param nachricht String die Nachricht für die User
     */
    private void neueNachricht(User user, String nachricht) {
//        System.out.println(nachricht);
        for (User u : users) {
            // if (u != user)
            u.sendeNachricht(nachricht);
        }
    }

    /**
     * <pre>
     * Ist ein Private Methode um User die den Chat verlassen haben aus der Liste zunehmen
     * </pre>
     *
     * @param user Liste von allen Usern die mit dem Server verbunden sind
     */
    private void loescheUser(User user) {

        users.remove(user);
    }

    /**
     * <pre>
     * Ist ein Private Klasse für die Nutzer
     * </pre>
     */
    private class User {

        private Socket socket;

        public User(Socket socket) {
            this.socket = socket;

            new Thread() {
                public void run() {

                    String ip = socket.getInetAddress().toString();
                    System.out.println("Client mit: " + ip + "angemeldet");
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()))) {
                        String zeile;
                        while ((zeile = reader.readLine()) != null) {
                            neueNachricht(User.this, zeile);
                        }

                    } catch (IOException e) {
                        // Wenn er hier rein kommt, kann er nicht mehr den Client erreichen
                        //
                    }
                    System.out.println("Client mit: " + ip + "nicht erreichbar!");
                    loescheUser(User.this);
                }
            }.start();
        }

        /**
         * <pre>
         * Diese Methode übermitelt die Chatnachrichten an alle Clients die auf dem Server sind
         * </pre>
         *
         * @param nachricht String ist die Chat Nachricht
         */
        public void sendeNachricht(String nachricht) {
            try {
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()));
                writer.write(nachricht);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
