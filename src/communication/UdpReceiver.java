package communication;

import java.net.*;

public class UdpReceiver {
    DatagramSocket receiver = null;
    String senderIp;
    int senderPort;

    public UdpReceiver(String ipStr, int port) {
        try {
            receiver = new DatagramSocket(port, InetAddress.getByName(ipStr));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getSenderIp() {
        return senderIp;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public String receive() {
        String msg = null;
        try {
            byte[] buffer = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            receiver.receive(dp);
            senderIp = dp.getAddress().getHostAddress();
            senderPort = dp.getPort();
            msg = new String(dp.getData(), 0, dp.getLength());
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
            DatagramPacket dp =
                    new DatagramPacket(data, data.length,
                            receiver.getInetAddress(), receiver.getPort());
            // 3. отправить
            receiver.send(dp);
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
