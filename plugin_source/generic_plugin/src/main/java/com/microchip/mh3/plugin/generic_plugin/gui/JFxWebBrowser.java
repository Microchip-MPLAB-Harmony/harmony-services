/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.gui;


import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.browser_engine.JXbrowserEngine;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.callback.AlertCallback;
import com.teamdev.jxbrowser.browser.callback.CreatePopupCallback;
import com.teamdev.jxbrowser.browser.callback.InjectJsCallback;
import com.teamdev.jxbrowser.browser.callback.input.MoveMouseWheelCallback;
import com.teamdev.jxbrowser.browser.callback.input.PressKeyCallback;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.ui.event.KeyPressed;
import com.teamdev.jxbrowser.ui.event.MouseWheel;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import com.teamdev.jxbrowser.zoom.ZoomLevel;
import java.awt.Desktop;
import java.io.IOException;
import static java.lang.String.format;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public final class JFxWebBrowser extends Region {

     /** for communication from the Javascript engine. */
    private  JavaConnector javaConnector = null;
    
    private  Engine engine = null ;
    private  BrowserView browserView;
    private  Browser browser;
    private String pluginManagerName;
    
    public JXbrowserEngine  browserEngine;
    
    public final String PREFERRED_ID = "DVMainWindow";

    public Browser getBrowser() {
        return browser;
    }
    
    JsObject webWindow;
    Stage parentStage;
   
    double z = 0.25;
    int nMinimumZoomFactor = 0;
    Tooltip toolTip = new Tooltip();
    
    ZoomLevel zoomlevelArrays [] = new ZoomLevel[]{ZoomLevel.P_75, ZoomLevel.P_80, ZoomLevel.P_90, ZoomLevel.P_100,
        ZoomLevel.P_110, ZoomLevel.P_125,  ZoomLevel.P_150, ZoomLevel.P_175, ZoomLevel.P_200};
    int currentZoomLevelIndex = 3;

    public JFxWebBrowser(Stage parentStage, String url, String pluginManagerName) {
        try {
            this.parentStage = parentStage;
            this.pluginManagerName = pluginManagerName;
            javaConnector = new JavaConnector(pluginManagerName, parentStage);
            browserEngine = new JXbrowserEngine();
            browser = browserEngine.getBrowserInstance(pluginManagerName);
            Log.write(pluginManagerName, Log.Severity.Info, "JXBrowser user directory : " + browser.engine().options().userDataDir(), Log.Level.USER);
            browser.set(InjectJsCallback.class, params -> {
                Frame frame = params.frame();
                String window = "window";
                JsObject jsObject = frame.executeJavaScript(window);
                if (jsObject == null) {
                    throw new IllegalStateException(
                            format("'%s' JS object not found", window));
                }
                jsObject.putProperty("javaConnector", javaConnector);
                return InjectJsCallback.Response.proceed();
            });

            browser.set(MoveMouseWheelCallback.class, params -> {
                MouseWheel event = params.event();
                boolean controlDown = event.keyModifiers().isControlDown();
                if (controlDown) {
                    double deltaY = event.deltaY();
                    ZooMInZoomOut(deltaY);
                }
                return MoveMouseWheelCallback.Response.proceed();
            });

            browser.set(PressKeyCallback.class, params -> {
                KeyPressed event = params.event();
                if (event.keyCode().equals(com.teamdev.jxbrowser.ui.KeyCode.KEY_CODE_F5)) {
                    browser.navigation().reload();
                }
                return PressKeyCallback.Response.proceed();
            });
            
            browser.set(CreatePopupCallback.class, (params) -> {
                openWebDirectory(params.targetUrl());
                return CreatePopupCallback.Response.suppress();
            });

            addStageListners();

            Platform.runLater(() -> {
                browser.navigation().loadUrl(url);
            });

            browserView = BrowserView.newInstance(browser);

            browser.set(AlertCallback.class, (params, tell) -> {
                String title = params.title();
                String message = params.message();
                String okActionText = params.okActionText();
                System.out.println(message);
                if(message.startsWith("Missing_Symbol:")){
                    Log.write(pluginManagerName, Log.Severity.Error, "The following symbol id is missing : "
                            + ""+ message.replaceAll("Missing_Symbol:", ""), Log.Level.USER);
                }
                Log.write(pluginManagerName, Log.Severity.Info, message, Log.Level.USER);
                tell.ok();
            });
            
             browser.zoom().level(zoomlevelArrays[currentZoomLevelIndex]);

            //add components
            getChildren().add(browserView);
        } catch (Exception ex) {
            Log.write(pluginManagerName, Log.Severity.Error, "Error:  " + ex.toString(), Log.Level.USER);
        }
    }
    public static void openWebDirectory(String url){
        new Thread(() -> {
            try {
                URI uri = new URI(url);
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(uri);
                    } catch (IOException e) {
                    }
                }
            } catch (URISyntaxException ex) {
            }
        }).start();
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browserView, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
    }
    
     public BrowserView getWebView() {
        return browserView;
    }

    public Engine getWebEngine() {
        return engine;
    }
    
    public void ZooMInZoomOut(double value) {
        if (value > 0) {
            if(currentZoomLevelIndex<zoomlevelArrays.length-1){
                currentZoomLevelIndex++;
            }
            browser.zoom().level(zoomlevelArrays[currentZoomLevelIndex]);
        } else {
            if(currentZoomLevelIndex>=0){
                currentZoomLevelIndex--;
            }
            browser.zoom().level(zoomlevelArrays[currentZoomLevelIndex]);
        }
    }
    
    private void addStageListners(){
        parentStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            ZooMInZoomOut(newVal.doubleValue()- oldVal.doubleValue());
        });

        parentStage.heightProperty().addListener((obs, oldVal, newVal) -> {
             ZooMInZoomOut(newVal.doubleValue()- oldVal.doubleValue());
        });
    }
    
    public void clearObjects() {
        try {
            if(browser!=null){
                browser.close();
                browserEngine.disPoseBrowserEvent(pluginManagerName);
                browserEngine.removeUsedUserDirectory(browser.engine().options().userDataDir());
                browserEngine = null;
                browser = null;
            }
           
            if(browserView!=null){
                browserView.getChildren().clear();
                browserView = null;
            }
        } catch (Exception ex) {
            Log.write(pluginManagerName, Log.Severity.Error, "Unable to clear browser objects", Log.Level.USER);
            Log.printException(ex);
        }

    }
}
