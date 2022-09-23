package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;

public class UdpSender {
    private final DatagramSocket sender;
    private final String serverIpStr;
    private final int serverPort;

    public UdpSender(String serverIpStr, int serverPort) {
        this.serverIpStr = serverIpStr;
        this.serverPort = serverPort;
        try {
            sender = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String msg) {
        try {
            byte[] data = msg.getBytes();
            DatagramPacket request =
                    new DatagramPacket(data, data.length,
                            InetAddress.getByName(serverIpStr), serverPort);
            sender.send(request);
        } catch (Exception ex) {
            System.out.println("Something wrong: " + ex.getMessage());
        } finally {
            try {
                if (!sender.isClosed()) {
                    sender.close();
                }
            } catch (Exception ex) {
                System.out.println("Something wrong in finally: " + ex.getMessage());
            }
        }
    }

    public String receive() {
        byte[] buffer = new byte[1024];
        DatagramPacket response = new DatagramPacket(buffer, buffer.length);
        try {
            sender.receive(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(buffer, 0, response.getLength());
    }
}
