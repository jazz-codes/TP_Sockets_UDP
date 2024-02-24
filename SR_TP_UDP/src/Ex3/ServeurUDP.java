package Ex3;

import java.io.*;
import java.net.*;
import java.util.Date;

public class ServeurUDP {
    private static final int PORT = 1250;
    private static final int MAX_CLIENTS = 5;

    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("Serveur UDP démarré sur le port " + PORT);

            // Tableau pour stocker les threads des clients
            Thread[] clientThreads = new Thread[MAX_CLIENTS];

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                Thread clientThread = new Thread(new ClientHandler(socket, packet));

                // Démarrage du thread du client
                clientThread.start();

                // Ajout du thread dans le tableau des threads des clients
                for (int i = 0; i < MAX_CLIENTS; i++) {
                    if (clientThreads[i] == null || !clientThreads[i].isAlive()) {
                        clientThreads[i] = clientThread;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    // Classe pour gérer chaque client dans un thread séparé
    static class ClientHandler implements Runnable {
        private DatagramSocket socket;
        private DatagramPacket packet;

        public ClientHandler(DatagramSocket socket, DatagramPacket packet) {
            this.socket = socket;
            this.packet = packet;
        }

        @Override
        public void run() {
            try {
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                // simulation d'un traitement de 3 secondes
                Thread.sleep(3000);

                // date et heure actuelles
                String dateStr = new Date().toString();
                byte[] responseData = dateStr.getBytes();

                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
                socket.send(responsePacket);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
