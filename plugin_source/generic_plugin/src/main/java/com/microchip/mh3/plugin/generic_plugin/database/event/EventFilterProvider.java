package com.microchip.mh3.plugin.generic_plugin.database.event;

import java.util.List;

public interface EventFilterProvider {
    
    List<EventFilter> possibleFilters();
    
}
