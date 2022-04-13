/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.database;

import com.microchip.h3.database.symbol.Symbol;

/**
 *
 * @author I17764
 */
public interface DatabaseAgent {

    public void addStateListener(StateChangeListener stateChangeListener);
    public void removeStateListener(StateChangeListener stateChangeListener);

    public void addSymbolListener(Symbol symbol, SymbolListener symbolListener);
    public void removeSymbolListener(Symbol symbol, SymbolListener symbolListener);

    public Symbol getSymbolByID(String symbolID);
    public String getComponentID();
    
    public void refresh();
}
