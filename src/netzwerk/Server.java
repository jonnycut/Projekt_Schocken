package netzwerk;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class Server {

    private List<User> users = new Vector<>();

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

    private void neueNachricht(User user, String nachricht) {
//        System.out.println(nachricht);
        for (User u : users) {
            if (u != user)
                u.sendeNachricht(nachricht);
        }
    }

    private void loescheUser(User user) {

        users.remove(user);
    }

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
