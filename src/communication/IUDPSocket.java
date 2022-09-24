package communication;

public interface IUDPSocket {
    void send(String msg);
    String receive();
}
