package com.microchip.mh3.plugin.generic_plugin.componentPluginLauncher;

import com.microchip.h3.database.component.ComponentPluginConfiguration;
import com.microchip.mh3.Core;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.gui.BrowserLauncher;
import com.microchip.mh3.plugin.generic_plugin.javafx.FxSupport;
import com.microchip.mh3.windowmanager.WindowManager;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import javafx.application.Platform;
import javax.swing.JMenuItem;

public class LaunchPlugin  {

    private Path pluginJarPath = null;
    private JMenuItem menuItem = null;
    
    public Map<String, Object> settingsMap;
    public String pluginManagerName;
    public String pluginVersion;
    public String mainHtmlPath;
    
    public BrowserLauncher browserLauncher;

    public String COMPONENT_ID ;
    
    BrowserLauncher jfxBrowserStage;


    public String getName() {
        return pluginManagerName;
    }

    public String getVersion() {
        return pluginVersion;
    }


    private void logError(){
        Log.write(pluginManagerName, Log.Severity.Warning, 
                        "Failed to open "+pluginManagerName+". Load plugin before opening "+pluginManagerName, Log.Level.DEBUG);
    }

    public void handleComponentActivation(ComponentPluginConfiguration cpc) {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return;
        }
        FxSupport.enable();
        pluginManagerName = (String)cpc.getArgs().get("plugin_name");
        mainHtmlPath = (String)cpc.getArgs().get("main_html_path");
        createLoadBrowserObject(pluginManagerName, mainHtmlPath);
        menuItem = new JMenuItem(pluginManagerName);
        menuItem.addActionListener(this::openManagerAsWindow);
        WindowManager.getInstance().addPluginLauncher(menuItem);
        
        this.pluginJarPath = Paths.get(cpc.getJarPath());

        Log.write(pluginManagerName, Log.Severity.Info, "Jar Path:  " + pluginJarPath, Log.Level.USER);

        Log.write("Generic HTML Path", Log.Severity.Info, "Main HTML path for "
                + "\"" + pluginManagerName + "\" : " + mainHtmlPath, Log.Level.USER);

        Log.write(pluginManagerName, Log.Severity.Info, "loading " + pluginManagerName + " plugin", Log.Level.USER);
    }
    
    private void createLoadBrowserObject(String pluginManagerName, String mainHtmlPath){
        Platform.runLater(() -> {
            browserLauncher = new BrowserLauncher(pluginManagerName, mainHtmlPath);
        });
    }
    
    private void openManagerAsWindow(ActionEvent event) {
        Platform.runLater(()->{
            browserLauncher.createAndShowStage();
        });
    }


    public void handleComponentDeactivation(ComponentPluginConfiguration cpc) {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return;
        }
        Platform.runLater(() -> {
            if (jfxBrowserStage != null) {
                jfxBrowserStage.clearObjects();
            }
        });

        WindowManager.getInstance().removePluginLauncher(menuItem);

        Log.write(pluginManagerName, Log.Severity.Info, "unloading "+pluginManagerName+" plugin", Log.Level.USER);
    }
}
