/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.database;

import com.microchip.h3.database.ComponentManager;
import com.microchip.h3.database.DatabaseEvents.SymbolValueChangedEvent;
import com.microchip.h3.database.DatabaseEvents.SymbolVisualChangedEvent;
import com.microchip.h3.database.component.FrameworkComponent;
import com.microchip.h3.database.symbol.Symbol;
import com.microchip.mh3.database.Database;
import com.microchip.mh3.event.Events;
import com.microchip.mh3.log.Log;
import com.microchip.utils.event.Event;
import com.microchip.utils.event.EventHandler;
import java.util.ArrayList;

public class DefaultDatabaseAgent implements DatabaseAgent {
    
    EventHandler eventHandler;
    
    private final ComponentManager componentManager;
    private final ArrayList<String> componentIdList = new ArrayList<>();
    private int componentIdErrorCount = 0;

    /*------------------------- Symbol Listener ---------------------------*/
    private StateChangeListener stateListener = null;
    
    public DefaultDatabaseAgent() {
        this.componentManager = Database.get().getComponentManager();
        eventHandler = new GlobalSymbolEventHandler();
        Events.addHandler(eventHandler);
    }
    
    public void setComponentID(String componentId) {
        componentIdList.add(componentId);
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
        if (componentIdList.contains(component.getID())) {
            stateListener.stateChanged(symbol);  
            return;
        }
        if(componentIdList.isEmpty()){
            if(componentIdErrorCount == 0){
                Log.write("Generic Plugin", Log.Severity.Error, "Listener component id is not configured for current launched plugin. "
                    + "This will break symbol reverse communication."  , Log.Level.USER);
                componentIdErrorCount++;
            }
        }
    }
    
    class GlobalSymbolEventHandler implements EventHandler {
        
        private final Class[] FILTER = {SymbolValueChangedEvent.class, SymbolVisualChangedEvent.class};
        
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
}
