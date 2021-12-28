import bgu.spl.net.api.bidi.Connections;

public class ConnectionsImpl<T> implements Connections<T>{

    public ConnectionsImpl(){
        

    }

    @Override
    public boolean send(int connectionId, T msg) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void broadcast(T msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void disconnect(int connectionId) {
        // TODO Auto-generated method stub
        
    }

    




}