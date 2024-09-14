package com.microchip.mh3.plugin.generic_plugin.database.symbol.controller;

import com.microchip.h3.database.symbol.KeyValueSetSymbol.DisplayMode;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.KeyValueSetSymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;

@ControllerPath("KeyValueSetSymbol")
public class KeyValueSetSymbolController extends ConfigSymbolController {

    private final SymbolAgent symbolAgent = SymbolAgent.singleton();

    @ControllerMethod
    public Response getValue(Request request, String componentId, String symbolId) {
        return symbolAgent.findKeyValueSetSymbol(componentId, symbolId)
                .map(e
                        -> KeyValueSetSymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .value(e.getValue())
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getOptionPairs(Request request, String componentId, String symbolId) {
        return symbolAgent.findKeyValueSetSymbol(componentId, symbolId)
                .map(e
                        -> KeyValueSetSymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .optionPairs(e.getValues())
                        .value(e.getValue())
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getSelectedOptionPair(Request request, String componentId, String symbolId) {
        return symbolAgent.findKeyValueSetSymbol(componentId, symbolId)
                .map(e
                        -> KeyValueSetSymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .selectedOptionPair(e.getValues().get(e.getValue()))
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getDisplayMode(Request request, String componentId, String symbolId) {
        return symbolAgent.findKeyValueSetSymbol(componentId, symbolId)
                .map(e
                        -> KeyValueSetSymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .displayMode(e.getDisplayMode().name())
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response setDisplayMode(Request request, String componentId, String symbolId, String displayMode) {
        return symbolAgent.findKeyValueSetSymbol(componentId, symbolId)
                .map(e -> {
                    e.setDisplayMode(DisplayMode.valueOf(displayMode), false);
                    return e;
                })
                .map(SymbolDto::new)
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getKeyValueSetSymbol(Request request, String componentId, String symbolId) {
        return symbolAgent.findKeyValueSetSymbol(componentId, symbolId)
                .map(KeyValueSetSymbolDto::new)
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response setValue(Request request, String componentId, String symbolId, int value) {
        return symbolAgent.findKeyValueSetSymbol(componentId, symbolId)
                .map(e -> {
                    e.setUserValue(value);
                    return e;
                })
                .map(SymbolDto::new)
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

}
