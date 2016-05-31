package netzwerk;

/**
 * Created by dfleuren on 31.05.2016.
 */
public class Programm {

        public static void main(String[] args) {
            if (args.length == 1) {
                new Client(args[0]);

            } else {
                new Server();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Client("127.0.0.1");
            }
        }
    }

