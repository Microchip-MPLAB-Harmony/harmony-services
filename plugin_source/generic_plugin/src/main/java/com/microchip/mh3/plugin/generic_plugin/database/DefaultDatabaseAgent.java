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
import com.microchip.mh3.log.Log;
import com.microchip.utils.event.Event;
import com.microchip.utils.event.EventHandler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DefaultDatabaseAgent implements DatabaseAgent {

    EventHandler eventHandler;

    private final ComponentManager componentManager;
    private String COMPONENT_ID;

    public DefaultDatabaseAgent() {
        this.componentManager = Database.get().getComponentManager();

        stateListeners = new HashSet<>();
        symListenersMap = new HashMap<>();
        eventHandler = new GlobalSymbolEventHandler();
        Events.addHandler(eventHandler);
    }
    
    public void setComponentID(String componentId){
        this.COMPONENT_ID = componentId;
    }

    public void destroy() {
        Events.removeHandler(eventHandler);
        symListenersMap.clear();        // even this is not required, because "this" object will be nullified by the holder
    }

    /*------------------------- Symbol Listener ---------------------------*/
    private Set<StateChangeListener> stateListeners = null;
    private Map<Symbol, Set<SymbolListener>> symListenersMap = null;

    @Override
    public void addSymbolListener(Symbol symbol, SymbolListener l) {
        if (l == null) {
            throw new NullPointerException();
        }
        if(symListenersMap.containsKey(symbol)){
            return;
        }
        Set<SymbolListener> set = symListenersMap.get(symbol);
        if (set == null) {
            set = new HashSet<>();
            set.add(l);
            symListenersMap.put(symbol, set);
        } else {
            set.add(l);
        }
    }

    public void addSymbolsListener(Symbol[] symbols, SymbolListener l) {
        if (l == null) {
            throw new NullPointerException();
        }

        for (Symbol symbol : symbols) {
            if (symListenersMap.containsKey(symbol)) {
                continue;
            }
            Set<SymbolListener> set = symListenersMap.get(symbol);
            if (set == null) {
                set = new HashSet<>();
                set.add(l);
                symListenersMap.put(symbol, set);
            } else {
                set.add(l);
            }
        }
    }

    @Override
    public void removeSymbolListener(Symbol symbol, SymbolListener l) {
        Set<SymbolListener> set = symListenersMap.get(symbol);
        if (set != null) {
            set.remove(l);
        }
    }

    public void removeSymbolsListener(Symbol[] symbols, SymbolListener l) {
        for (Symbol symbol : symbols) {
            Set<SymbolListener> set = symListenersMap.get(symbol);
            if (set != null) {
                set.remove(l);
            }
        }
    }

    public void notifySymbolChanged(Symbol symbol) {
        if (symbol == null) {
            return;
        }
   
        if(!(symbol.getComponent() instanceof FrameworkComponent))
                return;
        FrameworkComponent component = (FrameworkComponent)symbol.getComponent();

        if (component.getID().equals(COMPONENT_ID)) {
            Set<SymbolListener> set = symListenersMap.get(symbol);
            if (set != null) {
                for (SymbolListener l : set) {
                    l.symbolChanged(symbol);
                }
            }

//            notifyStateChanged();
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
        if (!stateListeners.contains(l)) {
            stateListeners.add(l);
        }
    }

    @Override
    public void removeStateListener(StateChangeListener l) {
        stateListeners.remove(l);
    }

    public void notifyStateChanged() {
        for (StateChangeListener l : stateListeners) {
            l.stateChanged();
        }
    }

    /*----------------- Helper Methods -----------------*/
    @Override
    public Symbol getSymbolByID(String symbolID) {
        return componentManager.getSymbolByID(null, this.COMPONENT_ID, symbolID);
    }

    public Symbol[] getSymbolsByID(String[] symbolIDs) {
        if (symbolIDs == null) {
            return null;
        }

        Symbol[] symbols = new Symbol[symbolIDs.length];
        Symbol symbol = null;
        int i = 0;
        for (String symbolID : symbolIDs) {
            if (symbolID == null) {
                throw new NullPointerException("Symbol ID is null");
            }
            symbol = componentManager.getSymbolByID(null, this.COMPONENT_ID, symbolID);
            if (symbol == null) {
                throw new NullPointerException("Symbol ID is invalid : " + symbolID);
            }
            symbols[i] = symbol;
            i++;
        }

        return symbols;
    }

    @Override
    public String getComponentID() {
        return this.COMPONENT_ID;
    }

    @Override
    public void refresh() {

        if (symListenersMap == null) {
            return;
        }

        Symbol symbol;
        for (Entry<Symbol, Set<SymbolListener>> entry : symListenersMap.entrySet()) {
            symbol = entry.getKey();
            for (SymbolListener l : entry.getValue()) {
                l.symbolChanged(symbol);
            }
        }

    }
}
