package com.microchip.mh3.plugin.browser_engine;

import com.microchip.mh3.Core;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.packageplugin.PackagePlugin;
import com.microchip.mh3.plugin.packageplugin.PluginConfiguration;
import java.nio.file.Path;
import java.util.Map;

public class GenericPackagePlugin implements PackagePlugin {

    private Path pluginJarPath = null;
    
    public Map<String, Object> settingsMap;
    public static String pluginManagerName = "";
    public String pluginVersion = "";

    public static String COMPONENT_ID ;
    
    
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
        
        Log.write(pluginManagerName, Log.Severity.Info, "initialize "+ pluginManagerName + " plugin", Log.Level.USER);

        this.pluginJarPath = pluginConfiguration.getPackagePath().resolve(pluginConfiguration.getBinaryFilePath());
        
        Log.write(pluginManagerName, Log.Severity.Info, "Jar Path:  "+ pluginJarPath , Log.Level.USER);

        
        Log.write(pluginManagerName, Log.Severity.Info, "loading "+ pluginManagerName +" plugin", Log.Level.USER);
        return true;
    }

    @Override
    public boolean close() {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return true;
        }
        Log.write("MCCHarmony", Log.Severity.Info, "unloading "+ pluginManagerName +" plugin", Log.Level.USER);
        JXbrowserEngine.deleteBrowserTempFiles();
       
        Log.write(pluginManagerName, Log.Severity.Info, "unloading "+ pluginManagerName +" plugin", Log.Level.USER);
        System.gc(); 
        return true;
    }

    @Override
    public PluginConfiguration getConfig() {
         return pluginConfiguration;
    }
}
