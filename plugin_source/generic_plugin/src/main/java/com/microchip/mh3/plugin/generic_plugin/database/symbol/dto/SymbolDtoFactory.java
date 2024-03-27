package com.microchip.mh3.plugin.generic_plugin.database.symbol.dto;

import com.microchip.h3.database.symbol.BooleanSymbol;
import com.microchip.h3.database.symbol.ComboSymbol;
import com.microchip.h3.database.symbol.CommentSymbol;
import com.microchip.h3.database.symbol.FloatSymbol;
import com.microchip.h3.database.symbol.IntegerSymbol;
import com.microchip.h3.database.symbol.KeyValueSetSymbol;
import com.microchip.h3.database.symbol.StringSymbol;
import com.microchip.h3.database.symbol.LongSymbol;
import com.microchip.h3.database.symbol.HexSymbol;
import com.microchip.h3.database.symbol.Symbol;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.SymbolAgent;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;

public class SymbolDtoFactory {

    private final SymbolAgent symbolAgent = SymbolAgent.singleton();

    public Optional<SymbolDto> findSymbol(String componentId, String symbolId) {
        return symbolAgent.findSymbol(componentId, symbolId)
                .map(this::getSymbol);
    }

    public SymbolDto getSymbol(Symbol symbol) {

        switch (symbol.getClass().getSimpleName()) {
            case "BooleanSymbol":
                return new BooleanSymbolDto((BooleanSymbol) symbol);
            case "IntegerSymbol":
                return new IntegerSymbolDto((IntegerSymbol) symbol);
            case "FloatSymbol":
                return new FloatSymbolDto((FloatSymbol) symbol);
            case "KeyValueSetSymbol":
                return new KeyValueSetSymbolDto((KeyValueSetSymbol) symbol);
            case "CommentSymbol":
                return new CommentSymbolDto((CommentSymbol) symbol);
            case "ComboSymbol":
                return new ComboSymbolDto((ComboSymbol) symbol);
            case "StringSymbol":
                return new StringSymbolDto((StringSymbol) symbol);
            case "LongSymbol":
                return new LongSymbolDto((LongSymbol) symbol);
            case "HexSymbol":
                return new HexSymbolDto((HexSymbol) symbol);
            default:
                Log.write("SymbolDtoFactory", Log.Severity.Info, "Symbol Type is unknown : " + symbol.getClass().getSimpleName(), Log.Level.DEBUG);
                return null;
        }
    }

    public List<SymbolDto> findAllSymbols(SymbolIdentity[] symbols) {
        return Arrays.stream(symbols)
                .map(e -> symbolAgent.findSymbol(e.getComponentId(), e.getSymbolId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(e -> {
                    switch (e.getClass().getSimpleName()) {
                        case "BooleanSymbol":
                            return new BooleanSymbolDto((BooleanSymbol) e);
                        case "IntegerSymbol":
                            return new IntegerSymbolDto((IntegerSymbol) e);
                        case "FloatSymbol":
                            return new FloatSymbolDto((FloatSymbol) e);
                        case "KeyValueSetSymbol":
                            return new KeyValueSetSymbolDto((KeyValueSetSymbol) e);
                        case "CommentSymbol":
                            return new CommentSymbolDto((CommentSymbol) e);
                        case "ComboSymbol":
                            return new ComboSymbolDto((ComboSymbol) e);
                        case "StringSymbol":
                            return new StringSymbolDto((StringSymbol) e);
                        case "LongSymbol":
                            return new LongSymbolDto((LongSymbol) e);
                        case "HexSymbol":
                            return new HexSymbolDto((HexSymbol) e);
                        default:
                            Log.write("SymbolDtoFactory", Log.Severity.Info, "Symbol Type is unknown : " + e.getClass().getSimpleName(), Log.Level.DEBUG);
                            return null;
                    }
                })
                .filter(e -> e != null)
                .collect(toList());
    }
}
