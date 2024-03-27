package com.microchip.mh3.plugin.generic_plugin.database.symbol.controller;

import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.LongSymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.SymbolAgent;

@ControllerPath("LongSymbol")
public class LongSymbolController extends ConfigSymbolController {

    private final SymbolAgent symbolAgent = SymbolAgent.singleton();

    @ControllerMethod
    public Response getValue(Request request, String componentId, String symbolId) {
        return symbolAgent.findLongSymbol(componentId, symbolId)
                .map(e
                        -> LongSymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .value(e.getValue())
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getLongSymbol(Request request, String componentId, String symbolId) {
        return symbolAgent.findLongSymbol(componentId, symbolId)
                .map(LongSymbolDto::new)
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response setValue(Request request, String componentId, String symbolId, long value) {
        return symbolAgent.findLongSymbol(componentId, symbolId)
                .map(e -> {
                    e.setUserValue(value);
                    return e;
                })
                .map(SymbolDto::new)
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

}
