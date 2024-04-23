package com.microchip.mh3.plugin.generic_plugin.database.symbol.controller;

import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDtoFactory;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.SymbolAgent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

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

}
