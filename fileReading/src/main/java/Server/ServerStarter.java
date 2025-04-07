package Server;

import UtilForServer.CommonServer;

public class ServerStarter {
    public static void main(String[] args) {
        int port1 = 5000;
        int port2 = 5001;
        String file1 = "frankenstein.txt";
        String file2 = "dracula.txt";

        new Thread(new CommonServer(port1, file1)).start();
        new Thread(new CommonServer(port2, file2)).start();
    }
}
