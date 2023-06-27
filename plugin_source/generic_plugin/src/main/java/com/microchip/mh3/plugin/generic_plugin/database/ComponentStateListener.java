/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.database;

import com.microchip.utils.event.Event;

@FunctionalInterface
public interface ComponentStateListener {
    public void componentStateChanged(Event event);
}
