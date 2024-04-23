package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.IntegerSymbol;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class IntegerSymbolDto extends ConfigSymbolDto {

    private final Integer min;
    private final Integer max;

    public IntegerSymbolDto(IntegerSymbol integerSymbol) {
        super(integerSymbol);
        min = integerSymbol.getMin();
        max = integerSymbol.getMax();
    }

}
