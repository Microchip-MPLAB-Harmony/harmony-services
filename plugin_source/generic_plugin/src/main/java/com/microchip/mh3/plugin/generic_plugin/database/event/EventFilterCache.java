package com.microchip.mh3.plugin.generic_plugin.database.event;

import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is scalable and for each connection a respective (event filter cache)
 * object must be created.
 *
 */
public class EventFilterCache {

    private final String connectorId;
    private final String eventFilterCacheId;

    private final Map<String, Set<EventFilter>> eventFilterMap = new HashMap<>();

    public EventFilterCache(String connectorId, String eventFilterCacheId) {
        this.connectorId = connectorId;
        this.eventFilterCacheId = eventFilterCacheId;
    }

    public void addEventFilter(String eventName, EventFilter eventFilter) {

        Set<EventFilter> filters = eventFilterMap.get(eventName);
        if (filters == null) {
            filters = new HashSet<>();
            eventFilterMap.put(eventName, filters);
        }

        filters.add(eventFilter);
    }

    public void removeEventFilter(String eventName, EventFilter eventFilter) {

        Set<EventFilter> filters = eventFilterMap.get(eventName);
        if (filters != null) {
            filters.remove(eventFilter);
        }
    }

    public boolean isEventMatches(String eventName) {
        return eventFilterMap.containsKey(eventName);
    }

    public boolean isFilterMatches(List<EventFilter> possibleFilters) {
        String eventName = possibleFilters.get(0).getEventName();

        Set<EventFilter> filters = eventFilterMap.get(eventName);
        if (filters == null) {
            return false;
        }

        return possibleFilters.stream()
                .anyMatch(filter -> filters.contains(filter));
    }

//    public boolean isFilterMatches(Response response) {
//
//        final EventDto eventDto = (EventDto) response.getData();
//
//        Set<EventFilter> filters = eventFilterMap.get(eventDto.getEventName());
//        if (filters == null) {
//            return false;
//        }
//
////        EventFilterProvider eventFilterProvider = (EventFilterProvider) response.getData();
//        return eventFilterProvider.possibleFilters().stream()
//                .anyMatch(filter -> filters.contains(filter));
//    }
    public String getEventFilterCacheId() {
        return eventFilterCacheId;
    }

    public String getConnectorId() {
        return connectorId;
    }

}
