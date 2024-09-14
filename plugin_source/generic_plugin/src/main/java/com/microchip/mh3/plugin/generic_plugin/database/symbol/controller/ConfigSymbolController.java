package com.microchip.mh3.plugin.generic_plugin.database.symbol.controller;

import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.ConfigSymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;

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

    @ControllerMethod
    public Response setValue(Request request, String componentId, String symbolId, Object value) {
        return symbolAgent.findConfigSymbol(componentId, symbolId)
                .map(e -> {
                    Object javaValue = SymbolAgent.singleton().jsToJava(e.getSymbolType(), value);
                    if (javaValue != null) {
                        e.setUserValue(javaValue);
                    } else {
                        Log.write(this.getClass().getName(), Log.Severity.Warning,
                                "Failed to set Symbol Value. Unsupported DataType of value. "
                                + "Component ID: " + componentId + ", "
                                + "Symbol ID: " + symbolId + ", "
                                + "Symbol Type: " + e.getSymbolType() + ", "
                                + "Value Type: " + value.getClass().getName());
                    }

                    return e;
                })
                .map(SymbolDto::new)
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }
}
