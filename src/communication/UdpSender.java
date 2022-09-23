package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;

public class UdpSender {
    private final DatagramSocket sender;
    private final String receiverIpStr;
    private final int receiverPort;

    public UdpSender(String receiverIpStr, int receiverPort) {
        this.receiverIpStr = receiverIpStr;
        this.receiverPort = receiverPort;
        try {
            sender = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String msg) {
        try {
            byte[] data = msg.getBytes();
            DatagramPacket dp =
                    new DatagramPacket(data, data.length,
                            InetAddress.getByName(receiverIpStr), receiverPort);
            // 3. отправить
            sender.send(dp);
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
        return new String(response.getData(), 0, response.getLength());
    }
}
