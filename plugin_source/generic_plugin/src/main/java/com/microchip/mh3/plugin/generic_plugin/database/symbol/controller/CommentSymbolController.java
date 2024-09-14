package com.microchip.mh3.plugin.generic_plugin.database.symbol.controller;

import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.CommentSymbolDto;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;

@ControllerPath("CommentSymbol")
public class CommentSymbolController extends VisibleSymbolController {

    private final SymbolAgent symbolAgent = SymbolAgent.singleton();

    @ControllerMethod
    public Response getLabel(Request request, String componentId, String symbolId) {
        return symbolAgent.findCommentSymbol(componentId, symbolId)
                .map(e
                        -> CommentSymbolDto.builder()
                        .componentId(componentId)
                        .symbolId(symbolId)
                        .label(e.getLabel())
                        .build())
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

    @ControllerMethod
    public Response getCommentSymbol(Request request, String componentId, String symbolId) {
        return symbolAgent.findCommentSymbol(componentId, symbolId)
                .map(CommentSymbolDto::new)
                .map(Response::success)
                .orElse(Response.error("Symbol Not Found : id or type does not match", request));
    }

}
