package com.microchip.mh3.plugin.generic_plugin.database.symbol.controller;

import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDtoFactory;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;

@ControllerPath("Symbol")
public class SymbolController {

    private final SymbolAgent symbolAgent = SymbolAgent.singleton();
    private final SymbolDtoFactory symbolDtoFactory = new SymbolDtoFactory();

    @ControllerMethod
    public Response getSymbolType(Request request, String componentId, String symbolId) {
        return symbolAgent.findSymbol(componentId, symbolId)
                .map(e
                        -> SymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .symbolType(e.getClass().getSimpleName())
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getSymbol(Request request, String componentId, String symbolId) {
        return symbolDtoFactory.findSymbol(componentId, symbolId)
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

}
