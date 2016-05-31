package netzwerk;

import gui.GUI;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dfleuren on 31.05.2016.
 */
public class Netzwerk {
    private GUI gui;

    public Netzwerk(String[] ipServer, GUI gui) {
        this.gui = gui;

        if (!ipServer[0].equals("")) {
            new Client(ipServer[0],this.gui);

        } else {
            new Server();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Verbinde zum Server: "+ipServer);
                InetAddress ia[] = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
                String ip = ia[1].getHostAddress();
                new Client(ip,this.gui);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}
