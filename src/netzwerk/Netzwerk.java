package netzwerk;

import gui.GUI;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dfleuren on 23.05.2016.
 * Diese Klasse stellt die Netzwerkkommunikation sicher.
 * Erzeugt entwerder ein Client oder einen Server mit Client.
 */
public class Netzwerk {

    private GUI gui; // Die aktuelle GUI

    /**
     * Erzeugt einen Client oder einen Server mit Client für die Netzwerkkommunikation.
     * @param ipServer String[] beinhaltet diese Array IP-Addresse:
     *                 Wird nur ein Client erzeugt.
     *                 Beinhaltet das Array keine IP-Addresse wird ein Server und ein Client erzeugt.
     * @param gui GUI GUI ist die aktuelle GUI.
     */
    public Netzwerk(String[] ipServer, GUI gui) {
        this.gui = gui;

        // JA: Die IP vom Server steht im Array. Nur Client erzeugen.
        if (!ipServer[0].equals("")) {
            new Client(ipServer[0],this.gui);
        }
        // NEIN: Es steht keine IP vom Server im Array. Server und Client erzeugen.
        else {
            new Server();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Verbinde zum Server:  "+ipServer);

                // Auslesen der Lokalen IP-Addresse vom Netzwerkadapter, um dem Client die Server IP zu übergeben.
                InetAddress ia[] = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
                String ip = ia[1].getHostAddress();
                new Client(ip,this.gui);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}
