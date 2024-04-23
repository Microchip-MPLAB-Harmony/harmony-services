package com.microchip.mh3.plugin.generic_plugin.database.plugin;

import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import com.microchip.mh3.plugin.generic_plugin.gui.HtmlPluginConfig;
import java.util.HashMap;
import java.util.Map;

@ControllerPath("Plugin")
public class PluginController {

    private final Map<String, HtmlPluginConfig> pluginConfigs = new HashMap<>();

    public void addPluginConfig(HtmlPluginConfig htmlPluginConfig) {
        pluginConfigs.put(htmlPluginConfig.pluginInstanceId(), htmlPluginConfig);
    }

    @ControllerMethod
    public Response getConfig(Request request, String pluginInstanceId) {
        HtmlPluginConfig htmlPluginConfig = pluginConfigs.get(pluginInstanceId);

        return Response.success(htmlPluginConfig);
    }

}
