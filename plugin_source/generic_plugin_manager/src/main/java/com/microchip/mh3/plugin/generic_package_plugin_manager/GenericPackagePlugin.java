package com.microchip.mh3.plugin.generic_package_plugin_manager;

import com.microchip.mh3.Core;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin_manager.gui.MainScreen;
import com.microchip.mh3.plugin.generic_plugin_manager.javafx.FxSupport;
import com.microchip.mh3.plugin.packageplugin.PackagePlugin;
import com.microchip.mh3.plugin.packageplugin.PackagePluginManager;
import com.microchip.mh3.plugin.packageplugin.PluginConfiguration;
import java.nio.file.Path;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GenericPackagePlugin implements PackagePlugin {

    private Path pluginJarPath = null;
    
    public Map<String, Object> settingsMap;
    public static String pluginManagerName = "";
    public String pluginVersion = "";
    public static String mainHtmlPath;

    public static String COMPONENT_ID ;
    
    MainScreen jfxBrowserStage;
    
    private PluginConfiguration pluginConfiguration;
     private Runnable deregister;

    @Override
    public String getName() {
        return pluginManagerName;
    }

    @Override
    public String getVersion() {
        return pluginVersion;
    }


    private void logError(){
        Log.write(pluginManagerName, Log.Severity.Warning, 
                        "Failed to open "+pluginManagerName+". Load plugin before opening "+pluginManagerName, Log.Level.DEBUG);
    }

    private void openManagerAsWindow() {
        Platform.runLater(this::createAndShowStage);
    }
 
    private Stage stage = null;

    private void createAndShowStage() {
        try {
            if (stage == null) {
                stage = new Stage();
                stage.setScene(createDashboard());
                stage.setTitle(pluginManagerName);
                stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
                    this.stage = null;
                });
            }
            stage.show();
            stage.toFront();
        } catch (Exception ex) {
            Log.printException(ex);
            logError();
        }

    }

    private Scene createDashboard() {
        jfxBrowserStage = new MainScreen(stage, settingsMap);
        return jfxBrowserStage.getScene(mainHtmlPath);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public String reasonOfFail() {
       return "";
    }

    @Override
    public boolean initialize(PluginConfiguration pc) {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return true;
        }
        
        this.pluginConfiguration = pc;
        pluginManagerName = (String)pluginConfiguration.getName();

        FxSupport.enable();
        
        Log.write(pluginManagerName, Log.Severity.Info, "initialize "+ pluginManagerName + " plugin", Log.Level.USER);
        
        
        this.pluginJarPath = pluginConfiguration.getPackagePath().resolve(pluginConfiguration.getBinaryFilePath());
        
        Log.write(pluginManagerName, Log.Severity.Info, "Jar Path:  "+ pluginJarPath , Log.Level.USER);

        deregister = PackagePluginManager.singleton().registerMenuItem(this, pluginManagerName, this::openManagerAsWindow);
        
        mainHtmlPath = (String)pluginConfiguration.getInitArgs().get("main_html_path");
        
        Log.write("Generic HTML Path", Log.Severity.Info, "Main HTML path for "
                + "\"" +pluginManagerName + "\" : "+GenericPackagePlugin.mainHtmlPath , Log.Level.USER);

        Log.write(pluginManagerName, Log.Severity.Info, "loading "+ pluginManagerName +" plugin", Log.Level.USER);
        return true;
    }

    @Override
    public boolean close() {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return true;
        }
        deregister.run();
        Platform.runLater(() -> {
            if(stage!=null){
                stage.close();
                stage = null;
            }
            if (jfxBrowserStage != null) {
                jfxBrowserStage.clearObjects();
            }
        });
        Log.write(pluginManagerName, Log.Severity.Info, "unloading "+ pluginManagerName +" plugin", Log.Level.USER);
        return true;
    }

    @Override
    public PluginConfiguration getConfig() {
         return pluginConfiguration;
    }
}
