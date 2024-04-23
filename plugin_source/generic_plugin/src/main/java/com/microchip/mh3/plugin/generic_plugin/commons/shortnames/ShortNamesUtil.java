package com.microchip.mh3.plugin.generic_plugin.commons.shortnames;

import com.microchip.h3.database.attribute.KeyValuePairListAttribute;
import com.microchip.h3.database.component.FrameworkComponent;
import com.microchip.h3.database.symbol.KeyValueSetSymbol;
import com.microchip.h3.database.symbol.Symbol;
import com.microchip.mh3.database.Database;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.project.Project;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.xml.bind.JAXBContext;
import static java.util.stream.Collectors.toList;
import javax.xml.bind.Unmarshaller;

public class ShortNamesUtil {

    public ShortNames getResolvedShortNamesTemplate(Path xmlFile) {
        ShortNames shortNames = null;
        try {
            JAXBContext context = JAXBContext.newInstance(ShortNames.class);
            Unmarshaller um = context.createUnmarshaller();
            shortNames = (ShortNames) um.unmarshal(xmlFile.toFile());
        } catch (Exception e) {
            Log.write("Short Names Util", Log.Severity.Error, "JAXB Exception " + e, Log.Level.ALL);
        }
        return shortNames;
    }

    public void applyShortNames(String componentID, ShortNames shortNames) {
        getAllChildKeyValueSetSymbols(componentID)
                .filter(keyValueSetSymbol -> shortNames.getValueGroup(keyValueSetSymbol.getID()).isPresent())
                .forEach(keyValueSetSymbol -> {
                    keyValueSetSymbol.setDisplayMode(KeyValueSetSymbol.DisplayMode.Description, true);

                    Optional<ValueGroup> foundValueGroup = shortNames.getValueGroup(keyValueSetSymbol.getID());

                    if (!foundValueGroup.isPresent()) {
                        Log.write("Short Names Util", Log.Severity.Info,
                                "Unable to find value group in JSON"
                                + ". Component ID : " + componentID
                                + ", Symbol ID : " + keyValueSetSymbol.getID(),
                                Log.Level.DEBUG);
                        return;
                    }

                    ValueGroup valueGroup = foundValueGroup.get();

                    keyValueSetSymbol.getValues().stream()
                            .filter(pair -> !valueGroup.isNameExist(pair.description))
                            .forEach(pair -> {
                                Optional<String> foundName = valueGroup.getName(pair.description);
                                if (foundName.isPresent()) {
                                    pair.description = foundName.get();
                                } else {
                                    Log.write("Short Names Util", Log.Severity.Info,
                                            "Unable to find caption in JSON"
                                            + ". Component ID : " + componentID
                                            + ", Symbol ID : " + keyValueSetSymbol.getID()
                                            + ", Option description : " + pair.description,
                                            Log.Level.DEBUG);
                                }
                            });
                });
    }

    private Stream<KeyValueSetSymbol> getAllChildKeyValueSetSymbols(String componentId) {
        return ((FrameworkComponent) Database.get().getComponentManager()
                .getActiveComponent(componentId))
                .getSymbols()
                .stream()
                .flatMap(this::getChildren)
                .filter(symbol -> symbol instanceof KeyValueSetSymbol)
                .map(symbol -> (KeyValueSetSymbol) symbol);
    }

    private void reportMissingSymbol(String componentID, ShortNames shortNames) {
        List<String> symbolIDs = getAllChildKeyValueSetSymbols(componentID)
                .map(Symbol::getID)
                .collect(toList());

        List<String> valueGroupIDs = shortNames.getValueGroups()
                .stream()
                .map(ValueGroup::getId)
                .collect(toList());

        symbolIDs.stream()
                .filter(symbolID -> !valueGroupIDs.contains(symbolID))
                .forEach(symbolID -> Log.write("Short Names Util", Log.Severity.Error, "Symbol not found in XML " + symbolID, Log.Level.ALL));
        valueGroupIDs.stream()
                .filter(valueGroupID -> !symbolIDs.contains(valueGroupID))
                .forEach(valueGroupID -> Log.write("Short Names Util", Log.Severity.Error, "Symbol not found in Database " + valueGroupID, Log.Level.ALL));

        getAllChildKeyValueSetSymbols(componentID)
                .forEach(e -> {
                    Optional<ValueGroup> valueGroup = shortNames.getValueGroup(e.getID());
                    if (valueGroup.isPresent()) {
                        reportMissingCaptions(valueGroup.get(), e);
                    }
                });
    }

    private void reportMissingCaptions(ValueGroup valueGroup, KeyValueSetSymbol symbol) {
        symbol.getValues()
                .stream()
                .map(e -> e.description)
                .filter(e -> !valueGroup.isCaptionExist(e))
                .forEach(description -> Log.write("Short Names Util", Log.Severity.Error, "description of Symbol not found in XML's caption: symbol = " + symbol.getID() + ", description = " + description, Log.Level.ALL));
    }

    public ShortNames generateShortNamesTemplate(String componentId, String symbolId) {

        List<ValueGroup> valueGroups = getAllChildKeyValueSetSymbols(componentId, symbolId)
                .map(keyValueSetSymbol -> createValueGroup(keyValueSetSymbol))
                //                .sorted(Comparator.comparing(ValueGroup::getId))
                .collect(toList());

        String processorName = Project.getProcessorName();
        return new ShortNames(processorName, valueGroups);
    }

    public Stream<KeyValueSetSymbol> getAllChildKeyValueSetSymbols(String componentId, String symbolId) {

        return ((FrameworkComponent) Database.get().getComponentManager()
                .getActiveComponent(componentId))
                .getSymbols()
                .stream()
                .flatMap(this::getChildren)
                .filter(symbol -> symbol instanceof KeyValueSetSymbol)
                .map(symbol -> (KeyValueSetSymbol) symbol);
    }

    public Stream<Symbol> getChildren(Symbol symbol) {
        return Stream.concat(
                Stream.of(symbol),
                symbol.getChildren().stream().flatMap(this::getChildren));
    }


    private ValueGroup createValueGroup(KeyValueSetSymbol symbol) {
        List<Value> values = symbol.getValues().stream()
                .map(pair -> new Value(pair.description, ""))
                //                .sorted(Comparator.comparing(Value::getCaption))
                .collect(toList());

        return new ValueGroup(symbol.getID(), values);
    }

}
