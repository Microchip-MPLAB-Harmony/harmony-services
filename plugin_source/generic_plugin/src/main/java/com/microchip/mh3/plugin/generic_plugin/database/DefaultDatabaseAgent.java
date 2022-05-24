/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.database;

import com.microchip.h3.database.ComponentManager;
import com.microchip.h3.database.DatabaseEvents.SymbolValueChangedEvent;
import com.microchip.h3.database.component.FrameworkComponent;
import com.microchip.h3.database.symbol.Symbol;
import com.microchip.mh3.database.Database;
import com.microchip.mh3.event.Events;
import com.microchip.utils.event.Event;
import com.microchip.utils.event.EventHandler;

public class DefaultDatabaseAgent implements DatabaseAgent {
    
    EventHandler eventHandler;
    
    private final ComponentManager componentManager;
    private String COMPONENT_ID;

    /*------------------------- Symbol Listener ---------------------------*/
    private StateChangeListener stateListener = null;
    
    public DefaultDatabaseAgent() {
        this.componentManager = Database.get().getComponentManager();
        eventHandler = new GlobalSymbolEventHandler();
        Events.addHandler(eventHandler);
    }
    
    public void setComponentID(String componentId) {
        this.COMPONENT_ID = componentId;
    }
    
    public void destroy() {
        Events.removeHandler(eventHandler);
        stateListener = null;       
    }
    
    public void notifySymbolChanged(Symbol symbol) {
        if (symbol == null) {
            return;
        }
        
        if (!(symbol.getComponent() instanceof FrameworkComponent)) {
            return;
        }
        FrameworkComponent component = (FrameworkComponent) symbol.getComponent();
        
        if (component.getID().equals(COMPONENT_ID)) {
            stateListener.stateChanged(symbol);
        }
    }
    
    class GlobalSymbolEventHandler implements EventHandler {
        
        private final Class[] FILTER = {SymbolValueChangedEvent.class};
        
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
    public String getComponentID() {
        return this.COMPONENT_ID;
    }
}
