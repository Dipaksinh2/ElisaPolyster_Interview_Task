import java.io.*;
import java.net.*;

public class FrankServer {
    public static void main(String[] args) throws URISyntaxException {
        int port = 5001;
        String fileName = "frankenstein.txt";

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Frankenstein Server running on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket, fileName)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method is created to handle request from client
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
