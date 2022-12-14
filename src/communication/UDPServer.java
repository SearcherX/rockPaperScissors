package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer implements IUDPSocket {
    private DatagramSocket serverSocket = null;
    private InetAddress senderAddress;
    private int senderPort;


    public UDPServer(int port) throws SocketException {
        serverSocket = new DatagramSocket(port);
    }

    public InetAddress getSenderAddress() {
        return senderAddress;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public String receive() throws IOException {
        String receivedData = null;
        /* Создайте буфер для хранения получаемых данных. */
        byte[] receivingDataBuffer = new byte[1024];

        /* Создайте экземпляр UDP-пакета для хранения клиентских данных с использованием буфера
        для полученных данных */
        DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);

        // Получите данные от клиента и сохраните их в inputPacket
        serverSocket.receive(inputPacket);
        // Преобразуйте данные в строку
        receivedData = new String(inputPacket.getData(), 0, inputPacket.getLength());

        // Получите IP-адрес и порт клиента
        senderAddress = inputPacket.getAddress();
        senderPort = inputPacket.getPort();
        return receivedData;
    }

    public void send(String msg) throws IOException {
        /* Создайте буфер для хранения отправляемых данных. */
        byte[] sendingDataBuffer;
        // Преобразуйте строку в байты и сохраните в буфер
        sendingDataBuffer = msg.getBytes();

        // Создайте новый UDP-пакет с данными, чтобы отправить их клиенту
        DatagramPacket outputPacket = new DatagramPacket(
                sendingDataBuffer, sendingDataBuffer.length,
                senderAddress, senderPort
        );

        // Отправьте пакет клиенту
        serverSocket.send(outputPacket);
    }

    public void close() {
        if (serverSocket != null)
            serverSocket.close();
    }
}
