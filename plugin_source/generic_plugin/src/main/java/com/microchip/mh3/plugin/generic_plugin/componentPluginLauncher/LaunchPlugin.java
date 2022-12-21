package com.microchip.mh3.plugin.generic_plugin.componentPluginLauncher;

import com.microchip.h3.database.component.ComponentPluginConfiguration;
import com.microchip.mh3.Core;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.gui.BrowserLauncher;
import com.microchip.mh3.plugin.generic_plugin.gui.HtmlPluginConfig;
import com.microchip.mh3.plugin.generic_plugin.javafx.FxSupport;
import com.microchip.mh3.windowmanager.WindowManager;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Platform;
import javax.swing.JMenuItem;

public class LaunchPlugin  {

    private Path pluginJarPath = null;
    private JMenuItem menuItem = null;
    
    private HtmlPluginConfig pluginConfig;
    public String pluginVersion;
    
    public BrowserLauncher browserLauncher;

    public String COMPONENT_ID ;
    
    BrowserLauncher jfxBrowserStage;


    public String getName() {
        return pluginConfig.pluginName();
    }

    public String getVersion() {
        return pluginVersion;
    }


    private void logError(){
        Log.write(pluginConfig.pluginName(), Log.Severity.Warning, 
                        "Failed to open "+pluginConfig.pluginName()+". Load plugin before opening "+pluginConfig.pluginName(), Log.Level.DEBUG);
    }

    public void handleComponentActivation(ComponentPluginConfiguration cpc) {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return;
        }
        FxSupport.enable();
        
        this.pluginConfig = new HtmlPluginConfig(cpc);
        
        Log.write(pluginConfig.pluginName(), Log.Severity.Info, "initialize " + pluginConfig.pluginName() + " plugin", Log.Level.USER);
        
        createLoadBrowserObject(this.pluginConfig);
        menuItem = new JMenuItem(pluginConfig.pluginName());
        menuItem.addActionListener(this::openManagerAsWindow);
        WindowManager.getInstance().addPluginLauncher(menuItem);
        
        this.pluginJarPath = Paths.get(cpc.getJarPath());

        Log.write(pluginConfig.pluginName(), Log.Severity.Info, "Main HTML path for "
                + "\"" + pluginConfig.pluginName() + "\" : " + pluginConfig.mainHtmlPath(), Log.Level.USER);
    }
    
    private void createLoadBrowserObject(HtmlPluginConfig pluginConfig){
        Platform.runLater(() -> {
            browserLauncher = new BrowserLauncher(pluginConfig);
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

        Log.write(pluginConfig.pluginName(), Log.Severity.Info, "unloading "+pluginConfig.pluginName()+" plugin", Log.Level.USER);
    }
}
