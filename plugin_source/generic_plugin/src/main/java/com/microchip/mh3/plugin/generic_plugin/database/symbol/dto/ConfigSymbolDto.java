package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.ConfigSymbol;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ConfigSymbolDto extends VisibleSymbolDto {

    private final String label;
    private final String description;
    private final Boolean readOnly;
    private final Object value;

    public ConfigSymbolDto(ConfigSymbol configSymbol) {
        super(configSymbol);
        label = configSymbol.getLabel();
        description = configSymbol.getDescription();
        readOnly = configSymbol.getReadOnly();
        value = configSymbol.getValue();
    }

}
