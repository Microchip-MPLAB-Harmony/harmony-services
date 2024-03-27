package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.LongSymbol;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LongSymbolDto extends ConfigSymbolDto {

    private final Long min;
    private final Long max;

    public LongSymbolDto(LongSymbol longSymbol) {
        super(longSymbol);
        min = longSymbol.getMin();
        max = longSymbol.getMax();
    }

}
