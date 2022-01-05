package bgu.spl.net.bidi;

import bgu.spl.net.impl.ConnectionHandlerImpl;

public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void broadcast(T msg);

    void disconnect(int connectionId);

    ConnectionHandlerImpl<T> getHandler(Integer sessionId);
}
