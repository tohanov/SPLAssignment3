package bgu.spl.net.bidi;

public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void broadcast(T msg);

    void disconnect(int connectionId);

    ConnectionHandler<T> getHandler(Integer sessionId);
}
