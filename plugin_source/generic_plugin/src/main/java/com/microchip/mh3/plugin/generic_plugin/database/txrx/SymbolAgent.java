package com.microchip.mh3.plugin.generic_plugin.database.txrx;

import com.microchip.h3.database.ComponentManager;
import com.microchip.h3.database.ConfigurationDatabase;
import static com.microchip.h3.database.attribute.KeyValuePairListAttribute.KeyValuePair;
import com.microchip.h3.database.symbol.BooleanSymbol;
import com.microchip.h3.database.symbol.ComboSymbol;
import com.microchip.h3.database.symbol.CommentSymbol;
import com.microchip.h3.database.symbol.IntegerSymbol;
import com.microchip.h3.database.symbol.ConfigSymbol;
import com.microchip.h3.database.symbol.FloatSymbol;
import com.microchip.h3.database.symbol.KeyValueSetSymbol;
import com.microchip.h3.database.symbol.LongSymbol;
import com.microchip.h3.database.symbol.StringSymbol;
import com.microchip.h3.database.symbol.Symbol;
import com.microchip.h3.database.symbol.VisibleSymbol;
import com.microchip.mh3.database.Database;
import java.util.Optional;

/**
 * this class acts as a facade to Database and provide so many convenience
 * utility methods.
 *
 * most utility methods starts with get or find keyword.
 *
 * use get methods with care, because it throws exception if it doesn't see
 * proper (Non null) value.
 *
 * find methods are safe because it always returns optional value. It allows us
 * to further take decision whether to throw an exception or default value.
 *
 */
public class SymbolAgent {

    private final ConfigurationDatabase db;
    private final ComponentManager componentManager;

    private SymbolAgent() {
        db = Database.get();
        componentManager = db.getComponentManager();
    }

    private static SymbolAgent singleton;

    public static SymbolAgent singleton() {
        if (singleton == null) {
            singleton = new SymbolAgent();
        }
        return singleton;
    }

    public Optional<Symbol> findSymbol(String componentID, String symbolID) {
        return Optional.ofNullable(
                componentManager.getSymbolByID(null, componentID, symbolID)
        );
    }

    public Optional<VisibleSymbol> findVisibleSymbol(String componentID, String symbolID) {
        return findSymbol(componentID, symbolID)
                .filter(symbol -> symbol instanceof VisibleSymbol)
                .map(symbol -> (VisibleSymbol) symbol);
    }

    public <T> Optional<ConfigSymbol<T>> findConfigSymbol(String componentID, String symbolID) {
        return findSymbol(componentID, symbolID)
                .filter(symbol -> symbol instanceof ConfigSymbol)
                .map(symbol -> (ConfigSymbol<T>) symbol);
    }

    public Symbol getSymbol(String componentID, String symbolID) {
        return findSymbol(componentID, symbolID)
                .orElseThrow(() -> new RuntimeException("Symbol not found : componentID = " + componentID + ", Symbol ID = " + symbolID));
    }

    //------- BooleanSymbol-------
    public Optional<BooleanSymbol> findBooleanSymbol(String componentId, String symbolId) {
        return this.findSymbol(componentId, symbolId)
                .filter(symbol -> symbol instanceof BooleanSymbol)
                .map(symbol -> (BooleanSymbol) symbol);
    }

    public Optional<Boolean> findBooleanSymbolValue(String componentID, String symbolID) {
        return this.<Boolean>findConfigSymbol(componentID, symbolID)
                .map(ConfigSymbol<Boolean>::getValue);
    }

    public Boolean getBooleanSymbolValue(String componentID, String symbolID) {
        return findBooleanSymbolValue(componentID, symbolID)
                .orElseThrow(() -> new RuntimeException("BooleanSymbol not found : componentID = " + componentID + ", Symbol ID = " + symbolID));
    }

    //------- IntegerSymbol-------
    public Optional<IntegerSymbol> findIntegerSymbol(String componentId, String symbolId) {
        return this.findSymbol(componentId, symbolId)
                .filter(symbol -> symbol instanceof IntegerSymbol)
                .map(symbol -> (IntegerSymbol) symbol);
    }

    public Optional<Integer> findIntegerSymbolValue(String componentID, String symbolID) {
        return this.<Integer>findConfigSymbol(componentID, symbolID)
                .map(ConfigSymbol<Integer>::getValue);
    }

    public Integer getIntegerSymbolValue(String componentID, String symbolID) {
        return findIntegerSymbolValue(componentID, symbolID)
                .orElseThrow(() -> new RuntimeException("IntegerSymbol not found : componentID = " + componentID + ", Symbol ID = " + symbolID));
    }

    //------- FloatSymbol-------
    public Optional<FloatSymbol> findFloatSymbol(String componentId, String symbolId) {
        return this.findSymbol(componentId, symbolId)
                .filter(symbol -> symbol instanceof FloatSymbol)
                .map(symbol -> (FloatSymbol) symbol);
    }

    public Optional<Float> findFloatSymbolValue(String componentID, String symbolID) {
        return this.<Float>findConfigSymbol(componentID, symbolID)
                .map(ConfigSymbol<Float>::getValue);
    }

    public Float getFloatSymbolValue(String componentID, String symbolID) {
        return findFloatSymbolValue(componentID, symbolID)
                .orElseThrow(() -> new RuntimeException("FloatSymbol not found : componentID = " + componentID + ", Symbol ID = " + symbolID));
    }

    //------------Long Symbol----------
    public Optional<LongSymbol> findLongSymbol(String componentId, String symbolId) {
        return this.findSymbol(componentId, symbolId)
                .filter(symbol -> symbol instanceof LongSymbol)
                .map(symbol -> (LongSymbol) symbol);
    }

    public Optional<Long> findLongSymbolValue(String componentID, String symbolID) {
        return this.<Long>findConfigSymbol(componentID, symbolID)
                .map(ConfigSymbol<Long>::getValue);
    }

    public Long getLongSymbolValue(String componentID, String symbolID) {
        return findLongSymbolValue(componentID, symbolID)
                .orElseThrow(() -> new RuntimeException("LongSymbol not found : componentID = " + componentID + ", Symbol ID = " + symbolID));
    }

    //------------String Symbol----------
    public Optional<StringSymbol> findStringSymbol(String componentId, String symbolId) {
        return this.findSymbol(componentId, symbolId)
                .filter(symbol -> symbol instanceof StringSymbol)
                .map(symbol -> (StringSymbol) symbol);
    }

    public Optional<String> findStringSymbolValue(String componentID, String symbolID) {
        return this.<String>findConfigSymbol(componentID, symbolID)
                .map(ConfigSymbol<String>::getValue);
    }

    public String getStringSymbolValue(String componentID, String symbolID) {
        return findStringSymbolValue(componentID, symbolID)
                .orElseThrow(() -> new RuntimeException("StringSymbol not found : componentID = " + componentID + ", Symbol ID = " + symbolID));
    }

    //------------KeyValueSet Symbol----------
    public Optional<KeyValueSetSymbol> findKeyValueSetSymbol(String componentId, String symbolId) {
        return this.findSymbol(componentId, symbolId)
                .filter(symbol -> symbol instanceof KeyValueSetSymbol)
                .map(symbol -> (KeyValueSetSymbol) symbol);
    }

    public Optional<KeyValuePair> findKeyValueSetSymbolPair(String componentID, String symbolID) {
        return findSymbol(componentID, symbolID)
                .filter(symbol -> symbol instanceof KeyValueSetSymbol)
                .map(symbol -> (KeyValueSetSymbol) symbol)
                .map(symbol -> symbol.getValues().get(symbol.getValue()));
    }

    public KeyValuePair getKeyValueSetSymbolPair(String componentID, String symbolID) {
        return findKeyValueSetSymbolPair(componentID, symbolID)
                .orElseThrow(() -> new RuntimeException("KeyValueSetSymbol not found : componentID = " + componentID + ", Symbol ID = " + symbolID));
    }

    public Optional<String> findKeyValueSetSymbolDescription(String componentID, String symbolID) {
        return findKeyValueSetSymbolPair(componentID, symbolID)
                .map(symbol -> symbol.description);
    }

    public String getKeyValueSetSymbolDescription(String componentID, String symbolID) {
        return getKeyValueSetSymbolPair(componentID, symbolID).description;
    }

    public Optional<String> findKeyValueSetSymbolKey(String componentID, String symbolID) {
        return findKeyValueSetSymbolPair(componentID, symbolID)
                .map(symbol -> symbol.key);
    }

    public String getKeyValueSetSymbolKey(String componentID, String symbolID) {
        return getKeyValueSetSymbolPair(componentID, symbolID).key;
    }

    public Optional<String> findKeyValueSetSymbolValue(String componentID, String symbolID) {
        return findKeyValueSetSymbolPair(componentID, symbolID)
                .map(symbol -> symbol.value);
    }

    public String getKeyValueSetSymbolValue(String componentID, String symbolID) {
        return getKeyValueSetSymbolPair(componentID, symbolID).value;
    }

    //------------Comment Symbol----------
    public Optional<CommentSymbol> findCommentSymbol(String componentID, String symbolID) {
        return findSymbol(componentID, symbolID)
                .filter(symbol -> symbol instanceof CommentSymbol)
                .map(symbol -> (CommentSymbol) symbol);
    }

    //------------Combo Symbol----------
    public Optional<ComboSymbol> findComboSymbol(String componentID, String symbolID) {
        return findSymbol(componentID, symbolID)
                .filter(symbol -> symbol instanceof ComboSymbol)
                .map(symbol -> (ComboSymbol) symbol);
    }

}
