package com.microchip.mh3.plugin.generic_plugin.database.event;

import com.google.gson.JsonObject;
import com.microchip.h3.database.DatabaseEvents.ComponentActivatedEvent;
import com.microchip.h3.database.DatabaseEvents.ComponentDeactivatedEvent;
import com.microchip.h3.database.DatabaseEvents.SymbolStateChangedEvent;
import com.microchip.h3.database.DatabaseEvents.SymbolValueChangedEvent;
import com.microchip.h3.database.DatabaseEvents.SymbolVisualChangedEvent;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.utils.event.Event;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerPath("event")
public class EventController {

    private final Consumer<Response> transmitter;
    private final EventJet eventJet;

    private final Map<String, EventFilterCache> eventFilterCaches = new HashMap<>();

    public EventController(Consumer<Response> transmitter) {
        this.transmitter = transmitter;
        this.eventJet = new EventJet(this::isEventMatches);
        EventDtoFactory eventDtoFactory = new EventDtoFactory(this::transmit);

        // symbol event handlers
        this.eventJet.addEventListener(SymbolValueChangedEvent.class, eventDtoFactory::handle);
        this.eventJet.addEventListener(SymbolStateChangedEvent.class, eventDtoFactory::handle);
        this.eventJet.addEventListener(SymbolVisualChangedEvent.class, eventDtoFactory::handle);

        // component event handlers
        this.eventJet.addEventListener(ComponentActivatedEvent.class, eventDtoFactory::handle);
        this.eventJet.addEventListener(ComponentDeactivatedEvent.class, eventDtoFactory::handle);
    }

    public boolean isEventMatches(Class<? extends Event> eventClass) {
        String eventName = eventClass.getSimpleName();

        return eventFilterCaches
                .values()
                .stream()
                .anyMatch(e -> e.isEventMatches(eventName));
    }

    public void transmit(Response response) {
        findEventFilterCaches(response)
                .forEach(eventFilterCache -> {
                    response.setUuid(eventFilterCache.getConnectorId()); // TODO : ensure that response is sent with different uuid
                    response.setEventFilterCacheId(eventFilterCache.getEventFilterCacheId());
                    this.transmitter.accept(response);
                });
    }

    private Stream<EventFilterCache> findEventFilterCaches(Response response) {
        final EventDto eventDto = (EventDto) response.getData();
        DefaultEventFilter defaultEventFilter = new DefaultEventFilter(eventDto);
        List<EventFilter> possibleFilters = defaultEventFilter.possibleFilters();
//        Log.write("Server Event Controller", Log.Severity.Error, "Event DTO: " + eventDto.toString());
//        Log.write("Server Event Controller", Log.Severity.Error, "Possible Filters : "
//                + possibleFilters.stream()
//                        .map(Object::toString)
//                        .collect(Collectors.joining(", ")));

        return eventFilterCaches
                .values()
                .stream()
                .filter(eventFilterCache -> eventFilterCache.isFilterMatches(possibleFilters));
//        return Arrays.asList("defaultUUID");
    }

    public void destroy() {
        this.eventJet.destroy();
    }

    @ControllerMethod
    public Response registerFilters(Request request, String eventFilterCacheId, DefaultEventFilter[] filters) {
        EventFilterCache eventFilterCache = eventFilterCaches.get(eventFilterCacheId);
        if (eventFilterCache == null) {
            eventFilterCache = new EventFilterCache(request.getUuid(), eventFilterCacheId);
            eventFilterCaches.put(eventFilterCacheId, eventFilterCache);
        }

        for (DefaultEventFilter filter : filters) {
//            Log.write("Server Event Controller", Log.Severity.Error, "adding Filter : " + filter.toString());
            eventFilterCache.addEventFilter(filter.getEventName(), filter);
        }

        return Response.success();
    }

    @ControllerMethod
    public Response deregisterFilters(Request request, String eventFilterCacheId, DefaultEventFilter[] filters) {
        EventFilterCache eventFilterCache = eventFilterCaches.get(eventFilterCacheId);
        if (eventFilterCache == null) {
            return Response.error("Unable to deregister the Event Filters. Event Filter Cache is empty", request);
        }

        for (DefaultEventFilter filter : filters) {
//            Log.write("Server Event Controller", Log.Severity.Error, "removing Filter : " + filter.toString());
            eventFilterCache.removeEventFilter(filter.getEventName(), filter);
        }
        return Response.success();
    }

    @ControllerMethod
    public Response destroyEventFilterCache(Request request, String eventFilterCacheId) {
        eventFilterCaches.remove(eventFilterCacheId);
        return Response.success();
    }

}
