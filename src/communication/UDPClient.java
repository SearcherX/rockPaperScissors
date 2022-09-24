package communication;

import java.io.IOException;
import java.net.*;

public class UDPClient {
    private DatagramSocket clientSocket;
    private InetAddress serverIP = null;
    private int serverPort = 0;

    public UDPClient(String serverIP, int serverPort) {
        try {
            this.serverIP = InetAddress.getByName(serverIP);
            this.serverPort = serverPort;
            clientSocket = new DatagramSocket();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        try {
            byte[] sendingDataBuffer = new byte[1024];
            sendingDataBuffer = msg.getBytes();

            // Создайте UDP-пакет
            DatagramPacket sendingPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length,
                    serverIP, serverPort);

            // Отправьте UDP-пакет серверу
            clientSocket.send(sendingPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receive() {
        String receivedData = null;
        try {
            byte[] receivingDataBuffer = new byte[1024];
            // Получите ответ от сервера, т.е. предложение из заглавных букв
            DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            clientSocket.receive(receivingPacket);

            // Выведите на экране полученные данные
            receivedData =  new String(receivingPacket.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receivedData;
    }
}
