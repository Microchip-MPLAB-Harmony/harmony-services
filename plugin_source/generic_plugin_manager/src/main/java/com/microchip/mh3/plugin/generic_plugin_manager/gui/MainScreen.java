package com.microchip.mh3.plugin.generic_plugin_manager.gui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Map;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainScreen  {
    
    Scene scene;
    public static Stage parentStage;
    
    JFxWebBrowser browser;
    
    public static final String localHost = "http://localhost:"+System.getProperty("HARMONY_SERVER_PORT")+"/";
   
    public MainScreen(Stage parentStage, Map<String, Object> settingsMap){
        MainScreen.parentStage = parentStage;
    }

    public Scene getScene(String url) {
        browser = new JFxWebBrowser(parentStage, localHost + url);
//        browser = new JFxWebBrowser(parentStage, "http://localhost:3000/");
        scene = new Scene(browser);
        return scene;
    }
    
    public void clearObjects(){
        browser.clearObjects();
    }
}
