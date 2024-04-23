package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.ComboSymbol;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ComboSymbolDto extends ConfigSymbolDto {

    private final List<String> options;

    public ComboSymbolDto(ComboSymbol comboSymbol) {
        super(comboSymbol);

        options = comboSymbol.getValues();
    }

}
