package com.microchip.mh3.plugin.generic_plugin.database.symbol.controller;

import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.ConfigSymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.SymbolAgent;

@ControllerPath("ConfigSymbol")
public class ConfigSymbolController extends VisibleSymbolController {

    private final SymbolAgent symbolAgent = SymbolAgent.singleton();

    @ControllerMethod
    public Response getLabel(Request request, String componentId, String symbolId) {
        return symbolAgent.findConfigSymbol(componentId, symbolId)
                .map(e
                        -> ConfigSymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .label(e.getLabel())
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getDescription(Request request, String componentId, String symbolId) {
        return symbolAgent.findConfigSymbol(componentId, symbolId)
                .map(e
                        -> ConfigSymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .description(e.getDescription())
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getReadOnly(Request request, String componentId, String symbolId) {
        return symbolAgent.findConfigSymbol(componentId, symbolId)
                .map(e
                        -> ConfigSymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .readOnly(e.getReadOnly())
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getValue(Request request, String componentId, String symbolId) {
        return symbolAgent.findConfigSymbol(componentId, symbolId)
                .map(e
                        -> ConfigSymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .value(e.getValue())
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }
}
