package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.FloatSymbol;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class FloatSymbolDto extends ConfigSymbolDto {

    private final Float min;
    private final Float max;

    public FloatSymbolDto(FloatSymbol floatSymbol) {
        super(floatSymbol);
        min = floatSymbol.getMin();
        max = floatSymbol.getMax();
    }

}
