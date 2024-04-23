package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.VisibleSymbol;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class VisibleSymbolDto extends SymbolDto {

    private final Boolean visible;

    public VisibleSymbolDto(VisibleSymbol visibleSymbol) {
        super(visibleSymbol);
        visible = visibleSymbol.getVisible();
    }

}
