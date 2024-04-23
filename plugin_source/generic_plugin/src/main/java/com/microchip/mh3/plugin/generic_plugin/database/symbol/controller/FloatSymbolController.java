package com.microchip.mh3.plugin.generic_plugin.database.symbol.controller;

import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.FloatSymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.SymbolAgent;

@ControllerPath("FloatSymbol")
public class FloatSymbolController extends ConfigSymbolController {

    private final SymbolAgent symbolAgent = SymbolAgent.singleton();

    @ControllerMethod
    public Response getValue(Request request, String componentId, String symbolId) {
        return symbolAgent.findFloatSymbol(componentId, symbolId)
                .map(e
                        -> FloatSymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .value(e.getValue())
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getFloatSymbol(Request request, String componentId, String symbolId) {
        return symbolAgent.findFloatSymbol(componentId, symbolId)
                .map(FloatSymbolDto::new)
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response setValue(Request request, String componentId, String symbolId, float value) {
        return symbolAgent.findFloatSymbol(componentId, symbolId)
                .map(e -> {
                    e.setUserValue(value);
                    return e;
                })
                .map(SymbolDto::new)
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

}
