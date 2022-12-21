package com.microchip.mh3.plugin.generic_plugin.gui;

import com.microchip.h3.database.component.ComponentPluginConfiguration;
import com.microchip.mh3.plugin.packageplugin.PluginConfiguration;
import com.teamdev.jxbrowser.js.JsAccessible;
import java.util.Map;

@JsAccessible
public class HtmlPluginConfig {

    private final String pluginName;
    private final String pluginID;
    private final String mainHtmlPath;
    private final boolean debugEnabled;
    private final boolean localServerEnabled;
    private final Map<String, Object> initArgs;

    public HtmlPluginConfig(ComponentPluginConfiguration cpc) {
        this.pluginID = cpc.getUiManagerID();
        this.initArgs = cpc.getArgs();
        this.pluginName = readFromInitArgs(initArgs, "plugin_name");
        this.mainHtmlPath = readFromInitArgs(initArgs, "main_html_path");
        this.debugEnabled = readBooleanFromInitArgs(initArgs, "debug");
        this.localServerEnabled = readBooleanFromInitArgs(initArgs, "local_server");
    }

    public HtmlPluginConfig(PluginConfiguration pc) {
        this.pluginID = pc.getPluginID();
        this.initArgs = pc.getInitArgs();
        this.pluginName = pc.getName();
        this.mainHtmlPath = readFromInitArgs(initArgs, "main_html_path");
        this.debugEnabled = readBooleanFromInitArgs(initArgs, "debug");
        this.localServerEnabled = readBooleanFromInitArgs(initArgs, "local_server");
    }

    private static String readFromInitArgs(Map<String, Object> initArgs, String key) {
        Object value = initArgs.get(key);
        return value != null ? (String) value : "";
    }
    
    private static boolean readBooleanFromInitArgs(Map<String, Object> initArgs, String key) {
        Object value = initArgs.get(key);
        return value != null ? Boolean.parseBoolean(value.toString()) : false;
    }
    
    @JsAccessible
    public boolean debugEnabled(){
        return debugEnabled;
    }
    
    @JsAccessible
    public boolean localServerEnabled(){
        return localServerEnabled;
    }

    @JsAccessible
    public String pluginName() {
        return pluginName;
    }

    @JsAccessible
    public String pluginID() {
        return pluginID;
    }

    @JsAccessible
    public String mainHtmlPath() {
        return mainHtmlPath;
    }

    @JsAccessible
    public Map<String, Object> initArgs() {
        return initArgs;
    }

}
