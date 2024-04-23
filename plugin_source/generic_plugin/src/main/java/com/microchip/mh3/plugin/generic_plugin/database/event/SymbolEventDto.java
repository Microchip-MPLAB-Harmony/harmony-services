package com.microchip.mh3.plugin.generic_plugin.database.event;

import com.google.gson.JsonObject;
import com.microchip.h3.database.symbol.ConfigSymbol;
import com.microchip.h3.database.symbol.Symbol;
import com.microchip.utils.event.Event;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;

public class SymbolEventDto implements EventFilterProvider {

    public final String eventName;
    public final String componentId;
    public final String symbolId;
    public final String symbolType;
    public final Object value;
    public final boolean readOnly;
    public final boolean visible;

    public SymbolEventDto(Event event, Symbol symbol) {
        this.eventName = event.getClass().getSimpleName();
        this.componentId = symbol.getComponent().getID();
        this.symbolId = symbol.getID();
        this.symbolType = symbol.getClass().getSimpleName();
        if (symbol instanceof ConfigSymbol) {
            this.value = ((ConfigSymbol) symbol).getValue();
        } else {
            this.value = null;
        }
        this.readOnly = symbol.getReadOnly();
        this.visible = symbol.getVisible();
    }

    @Override
    public List<EventFilter> possibleFilters() {
        final List<EventFilter> filters = new ArrayList<>();

        filters.add(new EventFilterImpl(eventName, null, null));
        filters.add(new EventFilterImpl(eventName, componentId, null));
        filters.add(new EventFilterImpl(eventName, componentId, symbolId));
        filters.add(new EventFilterImpl(eventName, null, symbolId));

        return filters;
    }

//    public static EventFilter createEventFilter(JsonObject attributes) {
//        String eventName = attributes.get("eventName").getAsString();
//        String componentId = attributes.has("componentId") ? attributes.get("componentId").getAsString() : null;
//        String symbolId = attributes.has("symbolId") ? attributes.get("symbolId").getAsString() : null;
//
//        return new EventFilterImpl(eventName, componentId, symbolId);
//    }

    @EqualsAndHashCode
    private static class EventFilterImpl implements EventFilter {

        private final String eventName;
        private final String componetId;
        private final String symbolId;

        public EventFilterImpl(String eventName, String componentId, String symbolId) {
            this.eventName = eventName;
            this.componetId = componentId;
            this.symbolId = symbolId;
        }

        @Override
        public String getEventName() {
            return eventName;
        }
    }

}
