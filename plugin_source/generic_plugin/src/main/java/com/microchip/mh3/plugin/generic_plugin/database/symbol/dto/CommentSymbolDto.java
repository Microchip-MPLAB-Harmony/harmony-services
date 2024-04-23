package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.CommentSymbol;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CommentSymbolDto extends VisibleSymbolDto {

    private final String label;

    public CommentSymbolDto(CommentSymbol commentSymbol) {
        super(commentSymbol);
        label = commentSymbol.getLabel();
    }

}
