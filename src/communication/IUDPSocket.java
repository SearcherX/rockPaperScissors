package communication;

import java.io.IOException;

public interface IUDPSocket {
    void send(String msg) throws IOException;
    String receive() throws IOException;
    void close();
}
