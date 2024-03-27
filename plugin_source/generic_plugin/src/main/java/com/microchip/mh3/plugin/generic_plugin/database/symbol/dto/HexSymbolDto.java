package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.HexSymbol;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class HexSymbolDto extends ConfigSymbolDto {

    private final Long min;
    private final Long max;

    public HexSymbolDto(HexSymbol hexSymbol) {
        super(hexSymbol);
        min = hexSymbol.getMin();
        max = hexSymbol.getMax();
    }

}
