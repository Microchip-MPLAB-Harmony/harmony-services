/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.gui;

import com.microchip.h3.database.symbol.ConfigSymbol;
import com.microchip.h3.database.symbol.Symbol;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.database.DatabaseAccess;
import com.microchip.mh3.plugin.generic_plugin.database.DefaultDatabaseAgent;
import com.microchip.mh3.plugin.generic_plugin.database.SymbolListener;
import com.teamdev.jxbrowser.js.JsAccessible;
import javafx.stage.Stage;

@JsAccessible
public final class JavaConnector {

    Stage parentStage;
    String pluginManagerName;
    JFxWebBrowser browserObject;
    DefaultDatabaseAgent agent;

    public JavaConnector(String pluginName, Stage parentStage, JFxWebBrowser browserObject) {
        this.pluginManagerName = pluginName;
        this.parentStage = parentStage;
        this.browserObject = browserObject;
        agent = new DefaultDatabaseAgent();
    }

    @JsAccessible
    public void setComponentId(String componentId) {
        agent.setComponentID(componentId);
    }

    @JsAccessible
    public String getSymbolData(String componentId, String symbolId) {
        try {
            String symbolValue = DatabaseAccess.getParameterValue(componentId, symbolId).toString();
            return symbolValue;
        } catch (Exception e) {
            Log.write(pluginManagerName, Log.Severity.Error, "Symbol value null : " + symbolId, Log.Level.USER);
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
//            String joined = String.join("M*C", comboValues);
//            System.out.println(joined);
            return builder.toString();
        } catch (Exception e) {
            Log.write(pluginManagerName, Log.Severity.Error, "Symbol value null : " + symbolId, Log.Level.USER);
            Log.printException(e);
        }
        return null;
    }

    @JsAccessible
    public String updateSymbolData(String componentId, String symbolId, Object value) {
        try {
            DatabaseAccess.setParameterValue(pluginManagerName, componentId, symbolId, value);
            return "success";
        } catch (Exception ex) {
            Log.write(pluginManagerName, Log.Severity.Error, "Database Update failed: " + symbolId, Log.Level.USER);
            Log.printException(ex);
            return "failure";
        }
    }

    @JsAccessible
    public String getSymbolType(String componentId, String symbolId) {
        return DatabaseAccess.getSymbolType(componentId, symbolId);
    }

    @JsAccessible
    public String getSymbolLabelName(String stComponent, String symbolID) {
        return DatabaseAccess.getLabelName(stComponent, symbolID);
    }

    @JsAccessible
    public Object getSymbolMinValue(String stComponent, String symbolID) {
        return DatabaseAccess.getMinValue(stComponent, symbolID);
    }

    @JsAccessible
    public Object getSymbolMaxValue(String stComponent, String symbolID) {
        return DatabaseAccess.getMaxValue(stComponent, symbolID);
    }

    @JsAccessible
    public Object getSymbolVisibleStatus(String stComponent, String symbolID) {
        return DatabaseAccess.getSymbolVisibleStatus(stComponent, symbolID);
    }

    @JsAccessible
    public Object getSymbolEnableStatus(String stComponent, String symbolID) {
        return DatabaseAccess.getSymbolEnableStatus(stComponent, symbolID);
    }

    @JsAccessible
    public void addSymbolListener(String symbolId) {
        agent.addSymbolListener(agent.getSymbolByID(symbolId), this::symbolChagned);
    }

    @JsAccessible
    public void addSymbolsListener(Symbol[] symbols, SymbolListener l) {

    }

    @JsAccessible
    public void removeSymbolListener(Symbol symbol, SymbolListener l) {

    }

    public void removeSymbolsListener(Symbol[] symbols, SymbolListener l) {

    }

    @JsAccessible
    public void clearSymbol(String componentId, String symbolId) {
        DatabaseAccess.clearUserSymbolValue(componentId, symbolId);
    }

    @JsAccessible
    public double getScreenWidth() {
        return parentStage.getWidth();
    }

    @JsAccessible
    public double getScreenHeight() {
        return parentStage.getHeight();
    }

    @JsAccessible
    public String getPortNumber() {
        return System.getProperty("HARMONY_SERVER_PORT");
    }

    @JsAccessible
    public void ZoomInReactCall() {
        browserObject.ZooMInZoomOut(1);
    }

    @JsAccessible
    public void ZoomOutReactCall() {
        browserObject.ZooMInZoomOut(-1);
    }

    public void symbolChagned(Symbol sy) {
        new Thread(() -> {
           try {
                ConfigSymbol sym = (ConfigSymbol) sy;
                Object obj = browserObject.getFrame().executeJavaScript("SymbolValueChanged(\"" + 
                        sym.getID() + "M*C" + sym.getValue()+ "M*C"+ sym.getEnabled() + "M*C" + sym.getVisible()+ "\")");
            } catch (Exception ex) {
                Log.write(pluginManagerName, Log.Severity.Error, "Unable to excute javascript api for symbol : "+ sy.getID()+" ->" + ex, Log.Level.USER);
                Log.printException(ex);
            }
        }).start();
    }
}
