package Ex3;

import java.io.*;
import java.net.*;

public class ClientUDP {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1250;

    public static void main(String[] args) {
        try {
            InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);
            DatagramSocket socket = new DatagramSocket();

            for (int i = 0; i < 10; i++) {
                String message = "Client " + (i) + ": Demande de la date et l'heure.";
                byte[] requestData = message.getBytes();

                DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, serverAddress, SERVER_PORT);
                socket.send(requestPacket);
                System.out.println("Requête envoyée au serveur depuis le client " + (i) + ".");

                byte[] responseData = new byte[1024];
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
                socket.receive(responsePacket);

                String receivedData = new String(responsePacket.getData(), 0, responsePacket.getLength());
                System.out.println("Réponse du serveur reçue par le client " + (i) + ": " + receivedData);

                Thread.sleep(1000);
            }

            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
