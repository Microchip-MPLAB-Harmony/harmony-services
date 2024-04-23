package com.microchip.mh3.plugin.generic_plugin.database.event;

import com.microchip.mh3.event.Events;
import com.microchip.utils.event.EventHandler;
import com.microchip.utils.event.Event;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javafx.application.Platform;

public class EventJet {

    private final EventHandler eventHandler;
    
    final Predicate<Class<? extends Event>> isEventMatches;

    public EventJet(Predicate<Class<? extends Event>> isEventMatches) {
        this.isEventMatches = isEventMatches;
        eventHandler = new GlobalEventHandler(this);
        Events.addHandler(eventHandler);
    }

    public void destroy() {
        Events.removeHandler(eventHandler);
    }

    private static class GlobalEventHandler implements EventHandler {

        private final EventJet eventJet;

        public GlobalEventHandler(EventJet eventJet) {
            this.eventJet = eventJet;
        }

        @Override
        public Class<?>[] getEventFilters() {
            return null;
        }

        @Override
        public String getHandlerName() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void handleEvent(Event event) throws Exception {
//            if (eventJet.isEventMatches.test(event.getClass())) {   // enable this when number of supported event types are increasing 
                eventJet.post(event);
//            }
        }
    }

    private static final Method CONSUMER_METHOD = Stream.of(Consumer.class.getMethods())
            .filter(method -> method.getName().equals("accept"))
            .findAny().orElse(null);

    private final Map<Class<? extends Event>, Set<Consumer<? extends Event>>> eventHandlerMap = new HashMap<>();

    public <T extends Event> void addEventListener(Class<T> eventClass, Consumer<T> handler) {
        Set<Consumer<? extends Event>> handlers = eventHandlerMap.get(eventClass);

        if (handlers == null) {
            handlers = new HashSet<>();
            handlers.add(handler);
            eventHandlerMap.put(eventClass, handlers);
        } else {
            handlers.add(handler);
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public void addEventListener(EventHandler handler) {
        Stream.of(handler.getEventFilters())
                .filter(eventClass -> eventClass.isInstance(Event.class))
                .forEach(eventClass -> addEventListener((Class<? extends Event>) eventClass,
                (event) -> {
                    try {
                        handler.handleEvent(event);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }));
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public void removeEventListener(EventHandler handler) {
        Stream.of(handler.getEventFilters())
                .filter(eventClass -> eventClass.isInstance(Event.class))
                .forEach(eventClass -> removeEventListener((Class<? extends Event>) eventClass,
                (event) -> {
                    try {
                        handler.handleEvent(event);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }));
    }

    public <T extends Event> void removeEventListener(Class<T> eventClass, Consumer<T> handler) {
        Set<Consumer<? extends Event>> handlers = eventHandlerMap.get(eventClass);

        if (handlers != null) {
            handlers.remove(handler);
        }
    }

    public void post(Event event) {
        eventHandlerMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isInstance(event))
                .flatMap(entry -> entry.getValue().stream())
                .forEach(consumer -> Platform.runLater(() -> this.callBack(consumer, event)));
    }

    private void callBack(Consumer<? extends Event> consumer, Event event) {
        try {
            CONSUMER_METHOD.invoke(consumer, event);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
            return;
        }
    }
}
