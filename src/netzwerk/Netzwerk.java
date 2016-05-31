package netzwerk;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Created by dfleuren on 31.05.2016.
 */
public class Netzwerk {

    public Netzwerk(String[] ipServer) {
        if (ipServer.length == 1) {
            new Client(ipServer[0]);

        } else {
            new Server();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                new Client("" + Inet4Address.getLocalHost());

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}
