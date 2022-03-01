package com.microchip.mh3.plugin.generic_plugin.gui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.microchip.mh3.log.Log;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class BrowserLauncher  {
    
    Scene scene;
    public String pluginManagerName;
    public String mainHtmlPath;
    
    JFxWebBrowser browser;
    public Stage stage = null;
    
    public String localHost;
   
    public BrowserLauncher(String pluginName, String mainHtmlPath){
        this.pluginManagerName = pluginName;
        this.mainHtmlPath = mainHtmlPath;
    }

    public Scene getScene(String url) {
        localHost = "http://localhost:"+System.getProperty("HARMONY_SERVER_PORT")+"/";
        browser = new JFxWebBrowser(stage, localHost + url, pluginManagerName);
//        browser = new JFxWebBrowser(stage,"http://localhost:3000/", pluginManagerName);
        scene = new Scene(browser);
        return scene;
    }
    
    public void clearObjects(){
        if(browser!=null){
            browser.clearObjects();
            browser = null;
        }
        if(scene!=null){
            scene = null;
        }
         if (stage != null) {
             stage.close();
             stage = null;
            }
    }

    public void createAndShowStage() {
        try {
            if (stage == null) {
                stage = new Stage();
                stage.setScene(getScene(mainHtmlPath));
                stage.setTitle(pluginManagerName);
                stage.setMaximized(true);
                stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
                    clearObjects();
                    this.stage = null;
                });
            }
            stage.show();
            stage.toFront();
        } catch (Exception ex) {
            Log.write(pluginManagerName, Log.Severity.Error, "Unable to create stage ", Log.Level.USER);
            Log.printException(ex);
            logError();
        }
    }
    
     private void logError() {
        Log.write(pluginManagerName, Log.Severity.Warning,
                "Failed to open " + pluginManagerName + ". Load plugin before opening " + pluginManagerName, Log.Level.DEBUG);
    }
}
