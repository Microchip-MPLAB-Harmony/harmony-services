package com.microchip.mh3.plugin.generic_package_plugin;

import com.microchip.mh3.Core;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.gui.BrowserLauncher;
import com.microchip.mh3.plugin.generic_plugin.javafx.FxSupport;
import com.microchip.mh3.plugin.packageplugin.PackagePlugin;
import com.microchip.mh3.plugin.packageplugin.PackagePluginManager;
import com.microchip.mh3.plugin.packageplugin.PluginConfiguration;
import java.nio.file.Path;
import java.util.Map;
import javafx.application.Platform;

public class GenericPackagePlugin implements PackagePlugin {

    private Path pluginJarPath = null;

    public Map<String, Object> settingsMap;
    public String pluginManagerName = "";
    public String pluginVersion = "";
    public String mainHtmlPath;

    public String COMPONENT_ID;

    BrowserLauncher browserLauncher;

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

    private void openManagerAsWindow() {
        Platform.runLater(()->{
            browserLauncher.createAndShowStage();
        });
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
        FxSupport.enable();
        
        pluginManagerName = (String) pluginConfiguration.getName();
        mainHtmlPath = (String) pluginConfiguration.getInitArgs().get("main_html_path");
        createLoadBrowserObject(pluginManagerName, mainHtmlPath);

        Log.write(pluginManagerName, Log.Severity.Info, "initialize " + pluginManagerName + " plugin", Log.Level.USER);

        this.pluginJarPath = pluginConfiguration.getPackagePath().resolve(pluginConfiguration.getBinaryFilePath());

        Log.write(pluginManagerName, Log.Severity.Info, "Jar Path:  " + pluginJarPath, Log.Level.USER);

        deregister = PackagePluginManager.singleton().registerMenuItem(this, pluginManagerName, this::openManagerAsWindow);

        Log.write("Generic HTML Path", Log.Severity.Info, "Main HTML path for "
                + "\"" + pluginManagerName + "\" : " + mainHtmlPath, Log.Level.USER);

        Log.write(pluginManagerName, Log.Severity.Info, "loading " + pluginManagerName + " plugin", Log.Level.USER);
        return true;
    }
    
    private void createLoadBrowserObject(String pluginManagerName, String mainHtmlPath){
        Platform.runLater(() -> {
            browserLauncher = new BrowserLauncher(pluginManagerName, mainHtmlPath);
        });
    }

    @Override
    public boolean close() {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return true;
        }
        deregister.run();

        Platform.runLater(() -> {
            if (browserLauncher != null) {
                browserLauncher.clearObjects();
                browserLauncher = null;
            }
        });
        Log.write(pluginManagerName, Log.Severity.Info, "unloading " + pluginManagerName + " plugin", Log.Level.USER);
        System.gc();
        return true;
    }

    @Override
    public PluginConfiguration getConfig() {
        return pluginConfiguration;
    }
}
