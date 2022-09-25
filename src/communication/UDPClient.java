package communication;

import java.io.IOException;
import java.net.*;

public class UDPClient implements IUDPSocket {
    private final DatagramSocket clientSocket;
    private InetAddress serverIP = null;
    private int serverPort = 0;

    public UDPClient(String serverIP, int serverPort) throws UnknownHostException, SocketException {
        //try {
            this.serverIP = InetAddress.getByName(serverIP);
            this.serverPort = serverPort;
            clientSocket = new DatagramSocket();
        //} catch (SocketException | UnknownHostException e) {
            //e.printStackTrace();
        //}
    }

    public void send(String msg) throws IOException {
        //try {
            /* Создайте буфер для хранения отправляемых данных. */
            byte[] sendingDataBuffer = new byte[1024];
            // Преобразуйте строку в байты и сохраните в буфер
            sendingDataBuffer = msg.getBytes();

            // Создайте UDP-пакет
            DatagramPacket sendingPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length,
                    serverIP, serverPort);

            // Отправьте UDP-пакет серверу
            clientSocket.send(sendingPacket);
        //} catch (IOException e) {
            //e.printStackTrace();
        //}
    }

    public String receive() throws IOException {
        String receivedData = null;
        //try {
            /* Создайте буфер для хранения получаемых данных. */
            byte[] receivingDataBuffer = new byte[1024];
            /* Создайте экземпляр UDP-пакета для хранения клиентских данных с использованием буфера
            для полученных данных */
            DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            // Получите данные от клиента и сохраните их в receivingPacket
            clientSocket.receive(receivingPacket);

            // Преобразуйте данные в строку
            receivedData =  new String(receivingPacket.getData(), 0, receivingPacket.getLength());
        //} catch (IOException e) {
           // e.printStackTrace();
        //}
        return receivedData;
    }

    public void close() {
        if (clientSocket != null)
            clientSocket.close();
    }
}
