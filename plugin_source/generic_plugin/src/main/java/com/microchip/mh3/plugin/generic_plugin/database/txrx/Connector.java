package com.microchip.mh3.plugin.generic_plugin.database.txrx;

//import java.util.UUID;

public interface Connector {

    void send(String response);

    void receive(String request);
    
    static String generateUUID() {
        return "defaultUUID"; // TODO: generate unique id while moving to WebSocket
//        return UUID.randomUUID().toString();
    }
}
