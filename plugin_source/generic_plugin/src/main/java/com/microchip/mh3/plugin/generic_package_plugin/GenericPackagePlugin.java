package com.microchip.mh3.plugin.generic_package_plugin;

import com.microchip.mh3.Core;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.gui.BrowserLauncher;
import com.microchip.mh3.plugin.generic_plugin.gui.HtmlPluginConfig;
import com.microchip.mh3.plugin.generic_plugin.javafx.FxSupport;
import com.microchip.mh3.plugin.packageplugin.PackagePlugin;
import com.microchip.mh3.plugin.packageplugin.PackagePluginManager;
import com.microchip.mh3.plugin.packageplugin.PluginConfiguration;
import java.nio.file.Path;
import java.util.Map;
import javafx.application.Platform;

public class GenericPackagePlugin implements PackagePlugin {

    private Path pluginJarPath = null;

    public String pluginVersion = "";
    private HtmlPluginConfig pluginConfig;

    public String COMPONENT_ID;

    BrowserLauncher browserLauncher;

    private PluginConfiguration pluginConfiguration;
    private Runnable deregister;

    @Override
    public String getName() {
        return pluginConfig.pluginName();
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
        
        this.pluginConfig = new HtmlPluginConfig(pc);
        createLoadBrowserObject(pluginConfig);

        Log.write(pluginConfig.pluginName(), Log.Severity.Info, "initialize " + pluginConfig.pluginName() + " plugin", Log.Level.USER);

        this.pluginJarPath = pluginConfiguration.getPackagePath().resolve(pluginConfiguration.getBinaryFilePath());

        Log.write(pluginConfig.pluginName(), Log.Severity.Info, "Jar Path:  " + pluginJarPath, Log.Level.USER);

        deregister = PackagePluginManager.singleton().registerMenuItem(this, pluginConfig.pluginName(), this::openManagerAsWindow);

        Log.write("Generic HTML Path", Log.Severity.Info, "Main HTML path for "
                + "\"" + pluginConfig.pluginName() + "\" : " + pluginConfig.mainHtmlPath(), Log.Level.USER);

        Log.write(pluginConfig.pluginName(), Log.Severity.Info, "loading " + pluginConfig.pluginName() + " plugin", Log.Level.USER);
        return true;
    }
    
    private void createLoadBrowserObject(HtmlPluginConfig pluginConfig){
        Platform.runLater(() -> {
            browserLauncher = new BrowserLauncher(pluginConfig);
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
        Log.write(pluginConfig.pluginName(), Log.Severity.Info, "unloading " + pluginConfig.pluginName() + " plugin", Log.Level.USER);
        System.gc();
        return true;
    }

    @Override
    public PluginConfiguration getConfig() {
        return pluginConfiguration;
    }
}
