package com.microchip.mh3.plugin.generic_plugin.database.event;

import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class EventDto {

    private final String eventName;
    private final SymbolDto symbol;
    private final ComponentDto component;

}
