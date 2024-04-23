package com.microchip.mh3.plugin.generic_plugin.commons.shortnames;

import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@ControllerPath("ShortNames")
public class ShortNamesController {

    private final ShortNamesUtil shortNamesUtil = new ShortNamesUtil();

    @ControllerMethod
    public Response generateShortNamesTemplate(Request request, String componentId, String symbolId) {

        ShortNames shortNames = shortNamesUtil.generateShortNamesTemplate(componentId, symbolId);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("shortNames", shortNames);

        return Response.success(attributes);
    }

    @ControllerMethod
    public Response applyShortNames(Request request, String componentId, String symbolId, ShortNames shortNames) {

        shortNamesUtil.applyShortNames(componentId, shortNames);

        return Response.success();
    }

    /**
     * Place the resolved xml file in <home>/.mh3/filename
     * 
     * @param request
     * @param fileName : the name of the resolved xml file(include .xml extension) at .mh3 location
     * @return 
     */
    @ControllerMethod
    public Response getResolvedShortNamesTemplate(Request request, String fileName) {
        
        Path xmlFile = Paths.get(System.getProperty("user.home"), ".mh3", fileName);

        ShortNames shortNames = shortNamesUtil.getResolvedShortNamesTemplate(xmlFile);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("shortNames", shortNames);

        return Response.success(attributes);
    }

}
