package com.microchip.mh3.plugin.generic_plugin.database.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;

public interface EventFilter {
    
    
    String getEventName();
    
//    @Getter
//    private final String eventName;
//    private final Map<String, String> attributes = new HashMap<>();
//    
//    public EventFilter(String eventName) {
//        this.eventName = eventName;
//    }
//    
//    public EventFilter addAttribute(String key, String value) {
//        this.attributes.put(key, value);
//        return this;
//    }
//    
//    public Map<String, String> getAttributes() {
//        return Collections.unmodifiableMap(attributes);
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 37 * hash + Objects.hashCode(this.eventName);
//        hash = 37 * hash + Objects.hashCode(this.attributes);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final EventFilter other = (EventFilter) obj;
//        if (!Objects.equals(this.eventName, other.eventName)) {
//            return false;
//        }
//        return Objects.equals(this.attributes, other.attributes);
//    }
    
    
}
