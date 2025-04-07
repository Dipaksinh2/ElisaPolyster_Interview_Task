package Client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Client {
    public static void main(String[] args) {
        String[] servers = { "localhost:5000", "localhost:5001" };
        Map<String, Integer> wordCount = new ConcurrentHashMap<>();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (String server : servers) {
            executor.execute(() -> {
                connectToServer(server, wordCount);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        printTopWords(wordCount);
    }

    private static void connectToServer(String server, Map<String, Integer> wordCount) {
        String[] parts = server.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        try {
            Socket socket = new Socket(host, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line, wordCount);
            }
        } catch (IOException e) {
            System.out.println("There is issue while connecting with port");
            e.printStackTrace();
        }
    }

    private static void processLine(String line, Map<String, Integer> wordCount) {
        String[] words = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        for (String word : words) {
            wordCount.merge(word, 1, Integer::sum);
        }
    }

    private static void printTopWords(Map<String, Integer> wordCount) {
        wordCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}
