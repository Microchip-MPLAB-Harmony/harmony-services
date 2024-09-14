package com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.args;

import lombok.Data;

@Data
public class SymbolValue {
    private final String symbolId;
    private final Object value;
}
