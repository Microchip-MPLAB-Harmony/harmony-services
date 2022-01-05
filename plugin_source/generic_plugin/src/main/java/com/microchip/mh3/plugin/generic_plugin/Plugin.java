package com.microchip.mh3.plugin.generic_plugin;

import com.microchip.h3.database.component.ComponentPluginConfiguration;
import com.microchip.mh3.Core;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.ComponentActivationHandler;
import com.microchip.mh3.plugin.generic_plugin.componentPluginLauncher.LaunchPlugin;
import com.microchip.mh3.plugin.generic_plugin.javafx.FxSupport;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class Plugin extends com.microchip.mh3.plugin.Plugin implements ComponentActivationHandler {

    private Path pluginJarPath = null;
    
    public Map<String, Object> settingsMap;
    public String pluginManagerName;
    public String pluginVersion;
    public String mainHtmlPath;
    
    Map<ComponentPluginConfiguration, LaunchPlugin> browserList = new LinkedHashMap<>();

    @Override
    public String getName() {
        return pluginManagerName;
    }

    @Override
    public String getVersion() {
        return pluginVersion;
    }

    @Override
    public boolean load(String pluginJarPath) {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return true;
        }
        FxSupport.enable();
        this.pluginJarPath = Paths.get(pluginJarPath);
        return true;
    }

    @Override
    public boolean unload() {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return true;
        }
        return true;
    }

    private void logError(){
        Log.write(pluginManagerName, Log.Severity.Warning, 
                        "Failed to open "+pluginManagerName+". Load plugin before opening "+pluginManagerName, Log.Level.DEBUG);
    }

    @Override
    public void handleComponentActivation(ComponentPluginConfiguration cpc) {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return;
        }
        LaunchPlugin browser = new LaunchPlugin();
        browser.handleComponentActivation(cpc);
        browserList.put(cpc, browser);
    }
    
    @Override
    public void handleComponentDeactivation(ComponentPluginConfiguration cpc) {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return;
        }
        browserList.get(cpc).handleComponentDeactivation(cpc);
        browserList.remove(cpc);
    }
}
