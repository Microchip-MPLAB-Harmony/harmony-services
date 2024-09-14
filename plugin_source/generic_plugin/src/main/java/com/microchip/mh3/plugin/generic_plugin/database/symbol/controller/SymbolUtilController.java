package com.microchip.mh3.plugin.generic_plugin.database.symbol.controller;

import com.microchip.h3.database.component.FrameworkComponent;
import com.microchip.h3.database.symbol.ConfigSymbol;
import com.microchip.h3.database.symbol.Symbol;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.database.component.controller.ComponentAgent;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDtoFactory;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.args.SymbolValue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;

@ControllerPath("SymbolUtil")
public class SymbolUtilController {

    private final SymbolAgent symbolAgent = SymbolAgent.singleton();
    private final SymbolDtoFactory symbolDtoFactory = new SymbolDtoFactory();

    @ControllerMethod
    public final Response getSymbolTypes(Request request, String componentId, String[] symbolIds) {
        List<SymbolDto> symbolDtos = Arrays.stream(symbolIds)
                .map(symbolId -> symbolAgent.findSymbol(componentId, symbolId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(SymbolDto::new)
                .collect(Collectors.toList());

        return Response.success(symbolDtos);
    }

    @ControllerMethod
    public final Response getSymbols(Request request, String componentId, String[] symbolIds) {
        List<SymbolDto> symbolDtos = Arrays.stream(symbolIds)
                .map(symbolId -> symbolDtoFactory.findSymbol(componentId, symbolId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return Response.success(symbolDtos);
    }

    @ControllerMethod
    public final Response getChildCount(Request request, String componentId, String symbolId) {
        return symbolAgent.findSymbol(componentId, symbolId)
                .map(e -> {
                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("componentId", componentId);
                    attributes.put("symbolId", symbolId);
                    attributes.put("childCount", e.getChildCount());
                    return Response.success(attributes);
                })
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public final Response getChildren(Request request, String componentId, String symbolId) {
        return symbolAgent.findSymbol(componentId, symbolId)
                .map(e -> {
                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("componentId", componentId);
                    attributes.put("symbolId", symbolId);
                    attributes.put("children", e.getChildren().stream().map(symbol -> symbol.getID()).collect(toList()));
                    return Response.success(attributes);
                })
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response clearUserValue(Request request, String componentId, String[] symbolIds) {

        Stream.of(symbolIds)
                .map(symbolId -> symbolAgent.findConfigSymbol(componentId, symbolId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(ConfigSymbol::clearUserValue);

        return Response.success();
    }

    @ControllerMethod
    public Response setValues(Request request, String componentId, SymbolValue[] symbolValues) {

        Optional<FrameworkComponent> foundComponent = ComponentAgent.singleton().findComponentById(componentId);
        if (!foundComponent.isPresent()) {
            return Response.error("Invalid Component ID : " + componentId, request);
        }

        FrameworkComponent component = foundComponent.get();

        for (SymbolValue symbolValue : symbolValues) {
            Symbol symbol = component.getSymbolByID(symbolValue.getSymbolId());

            if (symbol != null && symbol instanceof ConfigSymbol) {
                Object javaValue = SymbolAgent.singleton().jsToJava(symbol.getSymbolType(), symbolValue.getValue());
                if (javaValue != null) {
                    ((ConfigSymbol) symbol).setUserValue(javaValue);
                } else {
                    Log.write(this.getClass().getName(), Log.Severity.Warning,
                            "Failed to set Symbol Value. Unsupported DataType of value. "
                            + "Component ID: " + componentId + ", "
                            + "Symbol ID: " + symbolValue.getSymbolId() + ", "
                            + "Symbol Type: " + symbol.getSymbolType() + ", "
                            + "Value Type: " + ((Object) symbolValue.getValue()).getClass().getName());
                }
            } else if (symbol == null) {
                Log.write(this.getClass().getName(), Log.Severity.Warning,
                        "Failed to set Symbol Value. Symbol ID is Invalid. "
                        + "Component ID: " + componentId + ", "
                        + "Symbol ID: " + symbolValue.getSymbolId());
            } else { // symbol is not null & symbol type may be other than ConfigSymbol
                Log.write(this.getClass().getName(), Log.Severity.Warning,
                        "Failed to set Symbol Value. Unsupported Symbol Type. "
                        + "Component ID: " + componentId + ", "
                        + "Symbol ID: " + symbolValue.getSymbolId() + ", "
                        + "Symbol Type: " + symbol.getSymbolType());
            }
        }

        return Response.success();
    }

}
