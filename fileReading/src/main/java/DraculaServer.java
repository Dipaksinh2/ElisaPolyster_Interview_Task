import java.io.*;
import java.net.*;

public class DraculaServer {
    public static void main(String[] args) {
        int port = 5002;
        String fileName = "dracula.txt";

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Dracula Server running on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket, fileName)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket, String fileName) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
