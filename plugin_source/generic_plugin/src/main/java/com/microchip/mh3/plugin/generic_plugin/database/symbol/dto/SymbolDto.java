package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.Symbol;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
public class SymbolDto {

    private final String componentId;
    private final String symbolId;
    private final String symbolType;

    public SymbolDto(Symbol symbol) {
        componentId = symbol.getComponent().getID();
        symbolId = symbol.getID();
        symbolType = symbol.getClass().getSimpleName();
    }
}
