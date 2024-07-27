package com.microchip.mh3.plugin.generic_plugin.database.event;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DefaultEventFilter implements EventFilter {

    private String eventName;
    private String componentId;
    private String symbolId;

    public DefaultEventFilter(EventDto eventDto) {
        eventName = eventDto.getEventName();

        if (eventDto.getComponent() != null) {
            componentId = eventDto.getComponent().getComponentId();
        }

        if (eventDto.getSymbol() != null) {
            componentId = eventDto.getSymbol().getComponentId();
            symbolId = eventDto.getSymbol().getSymbolId();
        }
        
        if (eventDto.getAttachment() != null) {
            componentId = eventDto.getAttachment().getComponentId();
        }
    }

    public List<EventFilter> possibleFilters() {
        return Arrays.asList(
                new DefaultEventFilter(eventName, componentId, symbolId),
                new DefaultEventFilter(eventName, componentId, null),
                new DefaultEventFilter(eventName, null, symbolId),
                new DefaultEventFilter(eventName, null, null)
        );
    }

}
