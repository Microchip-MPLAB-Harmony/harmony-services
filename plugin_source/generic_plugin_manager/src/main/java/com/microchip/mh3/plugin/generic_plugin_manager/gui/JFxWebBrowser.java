/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin_manager.gui;

import com.microchip.mh3.log.Log;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.callback.AlertCallback;
import com.teamdev.jxbrowser.browser.callback.InjectJsCallback;
import com.teamdev.jxbrowser.browser.callback.input.MoveMouseWheelCallback;
import com.teamdev.jxbrowser.browser.callback.input.PressKeyCallback;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.ui.event.KeyPressed;
import com.teamdev.jxbrowser.ui.event.MouseWheel;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import java.io.File;
import static java.lang.String.format;
import java.nio.file.Paths;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public class JFxWebBrowser extends Region {

     /** for communication from the Javascript engine. */
    private final JavaConnector javaConnector = new JavaConnector();
    
    private  Engine engine = null ;
    private  BrowserView browserView;
    private  Browser browser;

    public Browser getBrowser() {
        return browser;
    }
    
    JsObject webWindow;
    Stage parentStage;
    
    
    double z = 0.25;
    int nMinimumZoomFactor = 0;
    Tooltip toolTip = new Tooltip();

    public JFxWebBrowser(Stage parentStage, String url) {
        try {
            this.parentStage = parentStage;
            engine = Engine.newInstance(
                    EngineOptions.newBuilder(HARDWARE_ACCELERATED)
                            .chromiumDir(Paths.get(System.getProperty("user.home"), ".jxbrowser"))
                            .licenseKey("6P835FT5HAF7IFUZKRD2L06GCIKZC0KMH6SG3VPQOW8Q2KK9373VA8HTH0G1FA2HIJ23")
                            .build());

            browser = engine.newBrowser();

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
                tell.ok();
            });

            //add components
            getChildren().add(browserView);
        } catch (Exception ex) {
            Log.printException(ex);
        }
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
            browser.zoom().in();
        } else {
            browser.zoom().out();
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
    
    public void clearObjects(){
        browser.close();
        engine.close();
        browser = null;
        engine = null;
        browserView = null;
    }
}
