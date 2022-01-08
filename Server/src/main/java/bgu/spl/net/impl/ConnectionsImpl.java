package bgu.spl.net.impl;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.net.bidi.ConnectionHandler;
import bgu.spl.net.bidi.Connections;


public class ConnectionsImpl<T> implements Connections<T>{

    private ConcurrentHashMap<Integer,ConnectionHandler<T>> connectionsMap;

    public ConnectionsImpl(){    
        connectionsMap=new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {

        ConnectionHandler<T> conHandeler=connectionsMap.get(connectionId);

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

    public void addIdToConnections(Integer id, ConnectionHandler<T> conHandler){
        connectionsMap.put(id, conHandler);
    }

    public ConnectionHandler<T> getHandler(Integer connectionId){
        return connectionsMap.get(connectionId);
    }
    




}