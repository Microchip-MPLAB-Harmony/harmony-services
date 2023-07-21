package com.microchip.mh3.plugin.generic_plugin.gui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.database.ComponentService;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class BrowserLauncher  {
    
    Scene scene;
    private final HtmlPluginConfig pluginConfig;
    
    JFxWebBrowser browser;
    public Stage stage = null;
    
    public String localHost;
   
    public BrowserLauncher(HtmlPluginConfig pluginConfig){
        this.pluginConfig = pluginConfig;
    }

    public Scene getScene(String url) {
        if(this.pluginConfig.localServerEnabled()){
            browser = new JFxWebBrowser(stage,"http://localhost:3000/", this.pluginConfig);
        }else{
            localHost = "http://localhost:"+System.getProperty("HARMONY_SERVER_PORT")+"/";
            browser = new JFxWebBrowser(stage, localHost + url, this.pluginConfig);
        }
        scene = new Scene(browser);
        return scene;
    }
   
    public void clearObjects(){
        if(browser!=null){
            browser.clearObjects();
            ComponentService.singleton().destroy();
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
        Platform.runLater(()->{
            try {
                if (stage == null) {
                    stage = new Stage();
                    stage.setScene(getScene(pluginConfig.mainHtmlPath()));
                    stage.setTitle(pluginConfig.pluginName());
                    stage.setMaximized(true);
                    stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
                        clearObjects();
                        this.stage = null;
                    });
                }
                stage.show();
                stage.toFront();
            } catch (Exception ex) {
                Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Unable to create stage ", Log.Level.USER);
                Log.printException(ex);
                logError();
            }
        });
    }
    
     private void logError() {
        Log.write(pluginConfig.pluginName(), Log.Severity.Warning,
                "Failed to open " + pluginConfig.pluginName() + ". Load plugin before opening " + pluginConfig.pluginName(), Log.Level.DEBUG);
    }
}
