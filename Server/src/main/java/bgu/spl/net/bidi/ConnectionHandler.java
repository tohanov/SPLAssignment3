package bgu.spl.net.bidi;

import java.io.Closeable;

import bgu.spl.net.impl.UserSession;

public interface ConnectionHandler<T> extends Closeable {
    void send(T msg);

	UserSession getUserSession(); //FIXME

	void setUserSession(UserSession session); //FIXME
}
