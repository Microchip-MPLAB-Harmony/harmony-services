package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.BooleanSymbol;
import com.microchip.h3.database.symbol.ConfigSymbol;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BooleanSymbolDto extends ConfigSymbolDto {

    public BooleanSymbolDto(BooleanSymbol booleanSymbol) {
        super((ConfigSymbol<Boolean>) booleanSymbol);
    }

}
