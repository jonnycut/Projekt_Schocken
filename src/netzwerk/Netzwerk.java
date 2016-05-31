package netzwerk;

import java.net.Inet4Address;
import java.net.InetAddress;
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
                InetAddress ia[] = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
                String ip = ia[1].getHostAddress();
                new Client(ip);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}
