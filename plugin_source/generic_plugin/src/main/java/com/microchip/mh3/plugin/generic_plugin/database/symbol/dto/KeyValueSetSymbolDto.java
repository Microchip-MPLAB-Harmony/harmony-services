package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.attribute.KeyValuePairListAttribute;
import com.microchip.h3.database.symbol.KeyValueSetSymbol;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class KeyValueSetSymbolDto extends ConfigSymbolDto {

    private final List<KeyValuePairListAttribute.KeyValuePair> optionPairs;
    private final String displayMode;
    private final KeyValuePairListAttribute.KeyValuePair selectedOptionPair;

    public KeyValueSetSymbolDto(KeyValueSetSymbol keyValueSetSymbol) {
        super(keyValueSetSymbol);

        optionPairs = keyValueSetSymbol.getValues();
        displayMode = keyValueSetSymbol.getDisplayMode().name();
        selectedOptionPair = keyValueSetSymbol.getValues().get(keyValueSetSymbol.getValue());
    }

}
