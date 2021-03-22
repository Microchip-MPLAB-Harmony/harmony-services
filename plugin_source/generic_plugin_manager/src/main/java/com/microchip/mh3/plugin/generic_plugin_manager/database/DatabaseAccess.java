/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin_manager.database;

import com.microchip.h3.database.attribute.KeyValuePairListAttribute;
import com.microchip.h3.database.symbol.ComboSymbol;
import com.microchip.h3.database.symbol.ConfigSymbol;
import com.microchip.h3.database.symbol.FloatSymbol;
import com.microchip.h3.database.symbol.IntegerSymbol;
import com.microchip.h3.database.symbol.KeyValueSetSymbol;
import com.microchip.h3.database.symbol.Symbol;
import com.microchip.mh3.database.Database;
import com.microchip.mh3.log.Log;
import static com.microchip.mh3.plugin.generic_package_plugin_manager.GenericPackagePlugin.pluginManagerName;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    
    public static void setParameterValue(String szComponentID, String symbolId, Object value) {
        Symbol sym = Database.get().getComponentManager().getSymbolByID(null, szComponentID, symbolId);
         
        if(sym == null) { 
            Log.write(pluginManagerName, Log.Severity.Error, "Symbol ID not found : " + symbolId, Log.Level.USER);
            return; }
        
        switch (sym.getSymbolType()) {
            case "Boolean":
                boolean bUpdate = (boolean)value.toString().equalsIgnoreCase("True");
                Database.get().getComponentManager().setSymbolUserValue(null, szComponentID, symbolId, bUpdate);
                break;
            case "Integer":
            case "Long":
            case "Hex":
                int nUpdate;
                if(value instanceof Double){
                    nUpdate = (int) (((Double) value).doubleValue());
                }else{
                    nUpdate = Integer.parseInt(value.toString().replace("0x", ""));
                }
                
                Database.get().getComponentManager().setSymbolUserValue(null, szComponentID, symbolId, nUpdate);
                break;
            case "Float":
                FloatSymbol floatSymbol = (FloatSymbol)sym;
                float fUpdate = Float.parseFloat(value.toString());
                floatSymbol.setUserValue(fUpdate);
                break;
            case "KeyValueSet":
                KeyValueSetSymbol symbol = (KeyValueSetSymbol) Database.get().getComponentManager().getSymbolByID(null, szComponentID, symbolId);
                symbol.setUserValue(getKeyValueSelectedIndex_ByKey(value, symbol));
                break;
            default:
                    Database.get().getComponentManager().setSymbolUserValue(null, szComponentID, symbolId, value);
                break;
        }
    }
    
    public static int getKeyValueSelectedIndex_ByKey(Object anItem, KeyValueSetSymbol symbol) {
        List<KeyValuePairListAttribute.KeyValuePair> l = symbol.getValues();
        for (KeyValuePairListAttribute.KeyValuePair k : l) {
            switch (symbol.getDisplayMode()) {
                case Key:
                    if (k.key.equals(anItem.toString())) {
                        return l.indexOf(k);
                    }
                    break;
                case Value:
                    if (k.value.equals(anItem.toString())) {
                        return l.indexOf(k);
                    }
                    break;
                case Description:
                    if (k.description.equals(anItem.toString())) {
                        return l.indexOf(k);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("showItemType " + symbol.getDisplayMode().name() + " is not supported");
            }
        }

        return -1;
    }
    
    public static Object getParameterValue(String szComponentID, String id) {
        Symbol sym = Database.get().getComponentManager().getSymbolByID(null, szComponentID, id);
        switch (sym.getSymbolType()) {
            case "KeyValueSet":
                KeyValueSetSymbol symbol = (KeyValueSetSymbol) Database.get().getComponentManager().getSymbolByID(null, szComponentID, id);
                return getKeyValueSetDisplayValue(symbol);
            default:
                return Database.get().getComponentManager().getSymbolValue(null, szComponentID, id);
        }
    }
    
    private static Object getKeyValueSetDisplayValue(KeyValueSetSymbol symbol){
        Object displayValue = null;
        List<KeyValuePairListAttribute.KeyValuePair> values = symbol.getValues();
        int index = symbol.getValue();
        
        //OutputMode oMode = symbol.getOutputMode();
        KeyValueSetSymbol.DisplayMode dmode = symbol.getDisplayMode();
        if(null != dmode)
            switch (dmode) {
            case Key:
                displayValue = values.get(index).key;
                break;
            case Value:
                displayValue = values.get(index).value;
                break;
            case Description:
                displayValue = values.get(index).description;
                break;
            default:
                break;
        }
        
        return displayValue;
    }
    
    public static String[] getSymbolArrayValues(String szComponentID, String id){
        Symbol sym = Database.get().getComponentManager().getSymbolByID(null, szComponentID, id);
         switch (sym.getSymbolType()) {
             case "KeyValueSet":
                return getKeyValueSetDisplayValues(szComponentID, id);
             case "Combo":
                 ComboSymbol symbol = (ComboSymbol) sym;
                 return symbol.getValues().toArray(new String[0]);
         }
         return null;
    }
    
     public static String[] getKeyValueSetDisplayValues(String szComponentID, String id){    
        Symbol sym = Database.get().getComponentManager().getSymbolByID(null, szComponentID, id);
        if (!"KeyValueSet".equals(sym.getSymbolType())) { return new String[]{}; }
        
        KeyValueSetSymbol symbol = (KeyValueSetSymbol)sym;
        List<String> displayValues = new ArrayList<>();
         //OutputMode oMode = symbol.getOutputMode();
        KeyValueSetSymbol.DisplayMode dmode = symbol.getDisplayMode();
        
        List<KeyValuePairListAttribute.KeyValuePair> values = symbol.getValues();
        for(int i=0; i<values.size();i++){
            if(null != dmode)
                switch (dmode) {
                case Key:
                    displayValues.add(values.get(i).key);
                    break;
                case Value:
                    displayValues.add(values.get(i).value);
                    break;
                case Description:
                    displayValues.add(values.get(i).description);
                    break;
                default:
                    break;
            }
        }       
        return displayValues.toArray(new String[]{});
    }
     
    public static String getLabelName(String componentID, String symbolID){
        Symbol sym = Database.get().getComponentManager().getSymbolByID(null, componentID, symbolID);
        if(sym instanceof ConfigSymbol){
            return ((ConfigSymbol)sym).getLabel();
        }
        return symbolID+" Not a ConfigSymbolType";
    }
    
    public static Object getMinValue(String componentID, String symbolID){
        Symbol sym = Database.get().getComponentManager().getSymbolByID(null, componentID, symbolID);
        if(sym instanceof IntegerSymbol){
            return ((IntegerSymbol)sym).getMin();
        }else if(sym instanceof FloatSymbol){
            return ((FloatSymbol)sym).getMin();
        }
        return symbolID+" Not a Integer/Float SymbolType";
    }
    
    public static Object getMaxValue(String componentID, String symbolID){
        Symbol sym = Database.get().getComponentManager().getSymbolByID(null, componentID, symbolID);
        if(sym instanceof IntegerSymbol){
            return ((IntegerSymbol)sym).getMax();
        }else if(sym instanceof FloatSymbol){
            return ((FloatSymbol)sym).getMax();
        }
        return symbolID+" Not a Integer/Float SymbolType";
    }
    
    public static Object getSymbolVisibleStatus(String componentID, String symbolID){
        Symbol sym = Database.get().getComponentManager().getSymbolByID(null, componentID, symbolID);
        if(sym instanceof ConfigSymbol){
            return ((ConfigSymbol)sym).getVisible();
        }
        return "Not a ConfigTypeSymobl";
    }
    
    public static void clearUserSymbolValue(String componentId, String symbolID){
        Symbol sym = Database.get().getComponentManager().getSymbolByID(null, componentId, symbolID);
        if(sym instanceof ConfigSymbol){
            ((ConfigSymbol) sym).clearUserValue();
        }
    }
    
    public static String getSymbolType(String componentID, String symbolID){
        Symbol sym = Database.get().getComponentManager().getSymbolByID(null, componentID, symbolID);
        return sym.getSymbolType();
    }
    
    
}
