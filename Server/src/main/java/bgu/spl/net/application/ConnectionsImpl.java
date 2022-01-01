import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.ConnectionHandler;

public class ConnectionsImpl<T> implements Connections<T>{

    private ConcurrentHashMap<Integer,bgu.spl.net.srv.ConnectionHandler<T>> connectionsMap;
    private AtomicInteger currentId;

    public ConnectionsImpl(){
        
        currentId=new AtomicInteger(0);

        connectionsMap=new ConcurrentHashMap<>();
               

    }

    @Override
    public boolean send(int connectionId, T msg) {

        ConnectionHandler conHandeler=connectionsMap.get(connectionId);

        if(conHandeler==null)
            return false;
        
        else{
            conHandeler.send(msg);

            return true;
        }

        
    }

    @Override
    public void broadcast(T msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void disconnect(int connectionId) {      //TODO:check later if working
        
        connectionsMap.remove(connectionId);    
        
    }

    public int addIdToConnections(ConnectionHandlerImpl conHandler){
       
        int idToAdd;
        
        do{
            idToAdd=currentId.get();
        }while(!currentId.compareAndSet(idToAdd, idToAdd+1));

        
        connectionsMap.put(idToAdd, conHandler);

        return idToAdd;

    }

    public bgu.spl.net.srv.ConnectionHandler<T> getHandler(Integer connectionId){
        return connectionsMap.get(connectionId);
    }
    




}