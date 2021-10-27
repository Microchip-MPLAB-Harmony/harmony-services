/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin_manager.gui;

import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin_manager.database.DatabaseAccess;
import com.teamdev.jxbrowser.js.JsAccessible;


@JsAccessible
public final class JavaConnector {
    
    @JsAccessible
    public String getSymbolData(String componentId, String symbolId) {
        try{
            String symbolValue = DatabaseAccess.getParameterValue(componentId, symbolId).toString();
            return symbolValue;
        }catch(Exception e){
            Log.printException(e);
        }
        return null;
    }

    @JsAccessible
    public String getSymbolValues(String componentId, String symbolId) {
        try {
            StringBuilder builder = new StringBuilder();
            String[] comboValues = DatabaseAccess.getSymbolArrayValues(componentId, symbolId);
            for (int i = 0; i < comboValues.length; i++) {
                builder.append(comboValues[i]);
                if (i != comboValues.length - 1) {
                    builder.append("M*C");
                }
            }
            return builder.toString();
        } catch (Exception e) {
            Log.printException(e);
        }
        return null;
    }
    
    @JsAccessible
    public void updateSymbolData(String componentId, String symbolId, Object value){
        DatabaseAccess.setParameterValue(componentId, symbolId, value);
    }
    
    @JsAccessible
    public String getSymbolType(String componentId, String symbolId){
        return DatabaseAccess.getSymbolType(componentId, symbolId);
    }
    
    @JsAccessible
    public String getSymbolLabelName(String stComponent, String symbolID){
        return DatabaseAccess.getLabelName(stComponent, symbolID);
    }
    
    @JsAccessible
    public Object getSymbolMinValue(String stComponent, String symbolID){
        return DatabaseAccess.getMinValue(stComponent, symbolID);
    }
    
     @JsAccessible
    public Object getSymbolMaxValue(String stComponent, String symbolID){
        return DatabaseAccess.getMaxValue(stComponent, symbolID);
    }
    
    @JsAccessible
    public Object getSymbolVisibleStatus(String stComponent, String symbolID){
        return DatabaseAccess.getSymbolVisibleStatus(stComponent, symbolID);
    }
    
     @JsAccessible
    public void clearSymbol(String componentId, String symbolId) {
        DatabaseAccess.clearUserSymbolValue(componentId, symbolId);
    }
    
      @JsAccessible
    public double getScreenWidth(){
         return MainScreen.parentStage.getWidth();
    }
    
     @JsAccessible
    public double getScreenHeight(){
        return MainScreen.parentStage.getHeight();
    }
    
    @JsAccessible
    public String getPortNumber(){
        return System.getProperty("HARMONY_SERVER_PORT");
    }
    
    
}
