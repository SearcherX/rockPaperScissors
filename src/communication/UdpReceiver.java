package communication;

import java.net.*;

public class UdpReceiver {
    DatagramSocket receiver = null;
    InetAddress senderAddress;
    int senderPort;
    int port;

    public UdpReceiver(String ipStr, int port) {
        this.port = port;
        try {
            //receiver = new DatagramSocket(port, InetAddress.getByName(ipStr));
            receiver = new DatagramSocket(port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String receive() {
        String msg = null;
        try {
            byte[] buffer = new byte[1024];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            receiver.receive(request);
            senderAddress = request.getAddress();
            senderPort = request.getPort();
            msg = new String(request.getData(), 0, request.getLength());
        } catch (Exception ex) {
            System.out.println("Something wrong: " + ex.getMessage());
        } finally {
            try {
                if (receiver != null && !receiver.isClosed()) {
                    receiver.close();
                }
            } catch (Exception ex) {
                System.out.println("Something wrong in finally: " + ex.getMessage());
            }
        }
        return msg;
    }

    public void send(String msg) {
        try {
            byte[] data = msg.getBytes();
            DatagramPacket response =
                    new DatagramPacket(data, data.length,
                            senderAddress, senderPort);
            // 3. отправить
            receiver.send(response);
        } catch (Exception ex) {
            System.out.println("Something wrong: " + ex.getMessage());
        } finally {
            try {
                if (!receiver.isClosed()) {
                    receiver.close();
                }
            } catch (Exception ex) {
                System.out.println("Something wrong in finally: " + ex.getMessage());
            }
        }
    }
}
