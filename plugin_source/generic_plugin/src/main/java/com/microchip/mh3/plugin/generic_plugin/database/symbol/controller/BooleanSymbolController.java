package com.microchip.mh3.plugin.generic_plugin.database.symbol.controller;

import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.BooleanSymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;

@ControllerPath("BooleanSymbol")
public class BooleanSymbolController extends ConfigSymbolController {

    private final SymbolAgent symbolAgent = SymbolAgent.singleton();

    @ControllerMethod
    public Response getValue(Request request, String componentId, String symbolId) {
//        if (!request.getData().isJsonObject()) {
//            return Response.error("Illegal Arguments: Argument data must be an Object", request);
//        }
//        JsonObject dataObject = (JsonObject) request.getData();
//        String componentId = dataObject.get("componentId").getAsString();
//        String symbolId = dataObject.get("symbolId").getAsString();

        return symbolAgent.findBooleanSymbol(componentId, symbolId)
                .map(e -> {
                    BooleanSymbolDto booleanSymbolModel = BooleanSymbolDto.builder()
                            .componentId(componentId)
                            .symbolId(symbolId)
                            .value(e.getValue())
                            .build();

//                    Map<String, Object> attributes = new HashMap<>();
//                    attributes.put("componentId", componentId);
//                    attributes.put("symbolId", symbolId);
//                    attributes.put("value", e.getValue());
                    return Response.success(booleanSymbolModel);
                }).orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getBooleanSymbol(Request request, String componentId, String symbolId) {
        return symbolAgent.findBooleanSymbol(componentId, symbolId)
                .map(e -> {
                    return Response.success(new BooleanSymbolDto(e));
                }).orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response setValue(Request request, String componentId, String symbolId, boolean value) {
        return symbolAgent.findBooleanSymbol(componentId, symbolId)
                .map(e -> {
                    e.setUserValue(value);
                    return e;
                })
                .map(SymbolDto::new)
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

}
