package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.StringSymbol;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class StringSymbolDto extends ConfigSymbolDto {

    public StringSymbolDto(StringSymbol stringSymbol) {
        super(stringSymbol);
    }

}
