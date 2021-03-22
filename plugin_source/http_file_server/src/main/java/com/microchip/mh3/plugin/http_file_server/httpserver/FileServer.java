/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.http_file_server.httpserver;

import com.microchip.mh3.log.Log;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

/**
 * Simple Jetty FileServer.
 */
public class FileServer extends Thread {


    private Server server;
    
    private static String frameworkPath;


    public void startServer(String frameworkPath){
        FileServer.frameworkPath = frameworkPath;
        start();
    }
    
    @Override
    public void run() {
        try {  
            server = new Server();
            ServerConnector serverConnector = new ServerConnector(server);
            server.addConnector(serverConnector);
           
            ResourceHandler resource_handler = new ResourceHandler();

            resource_handler.setDirectoriesListed(true);
//		resource_handler.setWelcomeFiles(new String[]{"index.html"});
            resource_handler.setResourceBase(frameworkPath);

            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resource_handler, new DefaultHandler()});
            server.setHandler(handlers);
            server.start();
            
            int portNumber = serverConnector.getLocalPort();
           
            System.setProperty("HARMONY_SERVER_PORT", portNumber+"");
            
            Log.write("Http File Server", Log.Severity.Info, "Port Number : "+portNumber, Log.Level.USER);
            
            server.join();
        } catch (Exception ex) {
            Log.printException(ex);
        }
    }
    
    public void destroyServer() {
        try {
            if (server == null) {
                return;
            }
            server.stop();
            server.destroy();

            server = null;
        } catch (Exception ex) {
            Log.printException(ex);
        }
    }

}
