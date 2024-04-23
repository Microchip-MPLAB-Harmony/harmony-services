/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.gui;

import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.browser_engine.GenericPackagePlugin;
import com.microchip.mh3.plugin.browser_engine.JXbrowserEngine;
import com.microchip.mh3.plugin.generic_plugin.database.ComponentService;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Connector;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.JxBrowserConnector;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Transceiver;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.callback.AlertCallback;
import com.teamdev.jxbrowser.browser.callback.CreatePopupCallback;
import com.teamdev.jxbrowser.browser.callback.InjectJsCallback;
import com.teamdev.jxbrowser.browser.callback.input.MoveMouseWheelCallback;
import com.teamdev.jxbrowser.browser.callback.input.PressKeyCallback;
import com.teamdev.jxbrowser.browser.event.ConsoleMessageReceived;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.js.ConsoleMessage;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.ui.KeyCode;
import static com.teamdev.jxbrowser.ui.KeyCode.KEY_CODE_F5;
import com.teamdev.jxbrowser.ui.event.KeyPressed;
import com.teamdev.jxbrowser.ui.event.MouseWheel;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import com.teamdev.jxbrowser.zoom.ZoomLevel;
import java.awt.Desktop;
import java.io.IOException;
import static java.lang.String.format;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public final class JFxWebBrowser extends Region {

     /** for communication from the Javascript engine. */
    private  JavaConnector javaConnector = null;

    private Transceiver transceiver;

    private  Engine engine = null ;
    private  BrowserView browserView;
    private  Browser browser;
    private final HtmlPluginConfig pluginConfig;

    public JXbrowserEngine  browserEngine;


    public Browser getBrowser() {
        return browser;
    }

    JsObject webWindow;
    Stage parentStage;

    public Frame frame;

    double z = 0.25;
    int nMinimumZoomFactor = 0;
    Tooltip toolTip = new Tooltip();

    ZoomLevel zoomlevelArrays [] = new ZoomLevel[]{ZoomLevel.P_75, ZoomLevel.P_80, ZoomLevel.P_90, ZoomLevel.P_100,
        ZoomLevel.P_110, ZoomLevel.P_125,  ZoomLevel.P_150, ZoomLevel.P_175, ZoomLevel.P_200};
    int currentZoomLevelIndex = 3;

    public JFxWebBrowser(Stage parentStage, String url, HtmlPluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
//        this.transceiver = new Transceiver(this);

        try {
            this.parentStage = parentStage;
            javaConnector = new JavaConnector(pluginConfig, parentStage, this);
            browserEngine = new JXbrowserEngine();
            browser = browserEngine.getBrowserInstance(pluginConfig.pluginName(), pluginConfig.debugEnabled());
            if (pluginConfig.debugEnabled()) {
                Log.write(pluginConfig.pluginName(), Log.Severity.Info, "JXBrowser remote debugging port : " + browser.engine().options().remoteDebuggingPort(), Log.Level.USER);
                Log.write(pluginConfig.pluginName(), Log.Severity.Info, "To debug " + pluginConfig.pluginName() + " make sure the plugin is "
                        + "launched and now load \"chrome://inspect\" in google chrome and wait for few seconds then click on inspect link"
                        + " (under remote target) to debug.", Log.Level.USER);
            }
            browser.set(InjectJsCallback.class, params -> {
                frame = params.frame();
                String window = "window";
                JsObject jsObject = frame.executeJavaScript(window);
                if (jsObject == null) {
                    throw new IllegalStateException(
                            format("'%s' JS object not found", window));
                }
                jsObject.putProperty("javaConnector", javaConnector);
                jsObject.putProperty("pluginConfig", pluginConfig);
                jsObject.putProperty("componentService", ComponentService.singleton());
                if (this.transceiver != null) {
                    this.transceiver.destroy();
                }
                this.transceiver = new Transceiver(this::createConnector);
                this.transceiver.addPluginConfig(pluginConfig);
//                jsObject.putProperty("serverJxBrowserConnector", transceiver.getJxBrowserConnector());
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
                switch (event.keyCode()) {
                    case KEY_CODE_F5:
                        browser.navigation().reload();
                        break;
                    case KEY_CODE_NUMPAD0:
                        if (event.keyModifiers().isControlDown()) {
                            ZooMInZoomOut(0);
                        }
                }
                return PressKeyCallback.Response.proceed();
            });

            browser.set(CreatePopupCallback.class, (params) -> {
                openWebDirectory(params.targetUrl());
                return CreatePopupCallback.Response.suppress();
            });

//            addStageListners();

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
                    Log.write(pluginConfig.pluginName(), Log.Severity.Error, "The following symbol id is missing : "
                            + ""+ message.replaceAll("Missing_Symbol:", ""), Log.Level.USER);
                }
                Log.write(pluginConfig.pluginName(), Log.Severity.Info, message, Log.Level.USER);
                tell.ok();
            });

            browser.zoom().level(zoomlevelArrays[currentZoomLevelIndex]);

            browser.on(ConsoleMessageReceived.class, this::log);

            //add components
            getChildren().add(browserView);
        } catch (Exception ex) {
            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Error:  " + ex.toString(), Log.Level.USER);
            Log.printException(ex);
        }
    }

    private void log(ConsoleMessageReceived event) {
        ConsoleMessage consoleMessage = event.consoleMessage();

        final String MARKER = "[MH3]";
        String message = consoleMessage.message();
        if (message.startsWith(MARKER)) {
            message = message.substring(MARKER.length());
        } else {
            return;
        }

        switch(consoleMessage.level()) {
            case CONSOLE_MESSAGE_LEVEL_UNSPECIFIED:     // 0
            case LOG:           // 2
                Log.write(pluginConfig.pluginName(), Log.Severity.Info, message, Log.Level.USER);
                return;
            case WARNING:       // 3
                Log.write(pluginConfig.pluginName(), Log.Severity.Warning, message, Log.Level.USER);
                return;
            case LEVEL_ERROR:   // 4
                Log.write(pluginConfig.pluginName(), Log.Severity.Error, message, Log.Level.USER);
                return;
            case DEBUG:         // 1
            case VERBOSE:       // 5
                Log.write(pluginConfig.pluginName(), Log.Severity.Info, message, Log.Level.DEBUG);
            default:
                return;
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

    public Frame getFrame(){
        return frame;
    }

    public void ZooMInZoomOut(double value) {
        if (value == 0) {
            browser.zoom().level(ZoomLevel.P_100);
            return;
        }
        if (value > 0) {
            if(currentZoomLevelIndex<zoomlevelArrays.length-1){
                currentZoomLevelIndex++;
            }
            browser.zoom().level(zoomlevelArrays[currentZoomLevelIndex]);
        } else {
            if(currentZoomLevelIndex>0){
                currentZoomLevelIndex--;
            }
            browser.zoom().level(zoomlevelArrays[currentZoomLevelIndex]);
        }
    }

//    private void addStageListners(){
//        parentStage.widthProperty().addListener((obs, oldVal, newVal) -> {
//            ZooMInZoomOut(newVal.doubleValue()- oldVal.doubleValue());
//        });
//
//        parentStage.heightProperty().addListener((obs, oldVal, newVal) -> {
//             ZooMInZoomOut(newVal.doubleValue()- oldVal.doubleValue());
//        });
//    }
    public void clearObjects() {
        this.transceiver.destroy();
        try {
            if(javaConnector!=null){
                javaConnector.clearJavaConnectorObjects();
                javaConnector = null;
            }

            if(browser!=null){
                browser.close();
                browserEngine.disPoseBrowserEvent(pluginConfig.pluginName());
                browserEngine.removeUsedUserDirectory(browser.engine().options().userDataDir());
                browserEngine = null;
                browser = null;
            }

            if(browserView!=null){
                browserView.getChildren().clear();
                browserView = null;
            }
        } catch (Exception ex) {
            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Unable to clear browser objects", Log.Level.USER);
            Log.printException(ex);
        }

    }

    public Connector createConnector(Consumer<String> receiver) {
        JxBrowserConnector jxBrowserConnector = new JxBrowserConnector(receiver, this);
        JsObject window = frame.executeJavaScript("window");
        if (window == null) {
            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "JS object not found : window");
            throw new IllegalStateException("JS object not found : window");
        }
        window.putProperty("serverJxBrowserConnector", jxBrowserConnector);
        return jxBrowserConnector;
    }
}
