/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.database;

import com.microchip.h3.database.symbol.Symbol;


@FunctionalInterface
public interface StateChangeListener {
    public void stateChanged(Symbol symbol);
}
