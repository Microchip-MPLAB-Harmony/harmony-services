/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.database;

import com.microchip.h3.database.DatabaseEvents;
import com.microchip.h3.database.DatabaseEvents.SymbolStateChangedEvent;
import com.microchip.h3.database.DatabaseEvents.SymbolValueChangedEvent;
import com.microchip.h3.database.DatabaseEvents.SymbolVisualChangedEvent;
import com.microchip.h3.database.component.FrameworkComponent;
import com.microchip.h3.database.symbol.Symbol;
import com.microchip.mh3.event.Events;
import com.microchip.mh3.log.Log;
import com.microchip.utils.event.Event;
import com.microchip.utils.event.EventHandler;
import java.util.ArrayList;
import java.util.List;

public class DefaultDatabaseAgent implements DatabaseAgent {

    EventHandler eventHandler;

    private final ArrayList<String> componentIdList = new ArrayList<>();

    /*------------------------- Symbol Listener ---------------------------*/
    private StateChangeListener stateListener = null;
    private List<ComponentStateListener> componentStateChangeListener = new ArrayList();

    public DefaultDatabaseAgent() {
        eventHandler = new GlobalSymbolEventHandler();
        Events.addHandler(eventHandler);
    }

    public void setComponentID(String componentId) {
        componentIdList.add(componentId);
    }

    public void destroy() {
        Events.removeHandler(eventHandler);
        stateListener = null;
        componentStateChangeListener = null;
    }

    public void notifySymbolChanged(Symbol symbol) {
        if (symbol == null) {
            return;
        }
        if (!(symbol.getComponent() instanceof FrameworkComponent)) {
            return;
        }
        FrameworkComponent component = (FrameworkComponent) symbol.getComponent();
        if (componentIdList.contains(component.getID())) {
            stateListener.stateChanged(symbol);
            return;
        }
    }

    public void notifyComponentStateChanged(Event event) {
        componentStateChangeListener.forEach(com -> {
            com.componentStateChanged(event);
        });
    }

    class GlobalSymbolEventHandler implements EventHandler {

        private final Class[] FILTER = {SymbolValueChangedEvent.class,
            SymbolVisualChangedEvent.class, SymbolStateChangedEvent.class, DatabaseEvents.ComponentActivatedEvent.class,
            DatabaseEvents.ComponentDeactivatedEvent.class};

        @Override
        public Class<?>[] getEventFilters() {
            return FILTER;
        }

        @Override
        public String getHandlerName() {
            return this.getClass().getSimpleName();
        }

        @Override
        public void handleEvent(Event evt) throws Exception {
            if (evt instanceof SymbolValueChangedEvent) {
                Symbol s = ((SymbolValueChangedEvent) evt).sym;
                notifySymbolChanged(s);
            } else if (evt instanceof SymbolVisualChangedEvent) {
                Symbol s = ((SymbolVisualChangedEvent) evt).sym;
                notifySymbolChanged(s);
            } else if (evt instanceof SymbolStateChangedEvent) {
                Symbol s = ((SymbolStateChangedEvent) evt).sym;
                notifySymbolChanged(s);
            } else if (evt instanceof DatabaseEvents.ComponentActivatedEvent) {
                notifyComponentStateChanged(evt);
            } else if (evt instanceof DatabaseEvents.ComponentDeactivatedEvent) {
                notifyComponentStateChanged(evt);
            }
        }
    }

    /*----------------------- State Listener --------------------------------*/
    @Override
    public void addStateListener(StateChangeListener l) {
        if (l == null) {
            throw new NullPointerException();
        }
        stateListener = l;
    }

    @Override
    public void addComponentStateListener(ComponentStateListener c) {
        if (componentStateChangeListener == null) {
            throw new NullPointerException();
        }
        if (!componentStateChangeListener.contains(c)) {
            this.componentStateChangeListener.add(c);
        }
    }
}
