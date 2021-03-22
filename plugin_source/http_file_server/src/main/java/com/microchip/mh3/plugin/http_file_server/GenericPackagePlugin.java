package com.microchip.mh3.plugin.http_file_server;

import com.microchip.mh3.Core;
import com.microchip.mh3.environment.Environment;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.http_file_server.httpserver.FileServer;
import com.microchip.mh3.plugin.packageplugin.PackagePlugin;
import com.microchip.mh3.plugin.packageplugin.PluginConfiguration;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class GenericPackagePlugin implements PackagePlugin {

    private Path pluginJarPath = null;

    public static String settingsYMlPath = null;
    public Map<String, Object> settingsMap;
    public static String pluginManagerName = "";
    public String pluginVersion = "";

    FileServer fileServer;

    public static String COMPONENT_ID;

    private PluginConfiguration pluginConfiguration;

    @Override
    public String getName() {
        return pluginManagerName;
    }

    @Override
    public String getVersion() {
        return pluginVersion;
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
        Log.write("Harmony File Server", Log.Severity.Info, "initialize harmony file server service", Log.Level.USER);
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return true;
        }

        this.pluginConfiguration = pc;
        this.pluginJarPath = pluginConfiguration.getPackagePath().resolve(pluginConfiguration.getBinaryFilePath());

        String frameworkPah = Paths.get(Environment.frameworkPath()).toString();
        try {
            fileServer = new FileServer();
            fileServer.startServer(frameworkPah);
        } catch (Exception ex) {
        }

        Log.write("Harmony File Server", Log.Severity.Info, "loading harmony file server service", Log.Level.USER);
        return true;
    }

    @Override
    public boolean close() {
        if (Core.getInstance().getType() == Core.Type.Headless) {
            return true;
        }

        fileServer.destroyServer();

        Log.write("Harmony File Server", Log.Severity.Info, "unloading harmony file server service", Log.Level.USER);
        return true;
    }

    @Override
    public PluginConfiguration getConfig() {
        return pluginConfiguration;
    }
}
