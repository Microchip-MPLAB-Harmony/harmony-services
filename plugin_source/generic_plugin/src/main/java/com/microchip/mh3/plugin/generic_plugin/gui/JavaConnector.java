/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.gui;

import com.microchip.h3.database.DatabaseEvents;
import com.microchip.h3.database.component.Component;
import com.microchip.h3.database.symbol.ConfigSymbol;
import com.microchip.h3.database.symbol.Symbol;
import com.microchip.mh3.environment.Environment;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.database.DatabaseAccess;
import com.microchip.mh3.plugin.generic_plugin.database.DefaultDatabaseAgent;
import static com.microchip.mh3.plugin.generic_plugin.gui.tools.Utils.getArrayValuesAsString;
import com.microchip.utils.event.Event;
import com.teamdev.jxbrowser.js.JsAccessible;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.stage.Stage;

@JsAccessible
public final class JavaConnector {

    private final HtmlPluginConfig pluginConfig;

    Stage parentStage;
    JFxWebBrowser browserObject;
    DefaultDatabaseAgent agent;

    String recentSymbolUpdatedByReact = "";

    Set<String> symbolListenersList = new HashSet<>();

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public JavaConnector(HtmlPluginConfig pluginConfig, Stage parentStage, JFxWebBrowser browserObject) {
        this.pluginConfig = pluginConfig;
        this.parentStage = parentStage;
        this.browserObject = browserObject;
        agent = new DefaultDatabaseAgent();
        agent.addStateListener(this::stateChanged);
        agent.addComponentStateListener(this::componentActivated);
        agent.addComponentStateListener(this::componentDeActivated);
    }

    @JsAccessible
    public void setComponentId(String componentId) {
        agent.setComponentID(componentId);
    }

    @JsAccessible
    public String getSymbolData(String componentId, String symbolId) {
        try {
            return DatabaseAccess.getParameterValue(componentId, symbolId).toString();
        } catch (Exception e) {
            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Symbol value null : " + symbolId, Log.Level.USER);
            Log.printException(e);
        }
        return null;
    }
    
    @JsAccessible
    public Object getParameterValue(String componentId, String symbolId) {
        try {
            return DatabaseAccess.getParameterValue(componentId, symbolId).toString();
        } catch (Exception e) {
            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Symbol value null : " + symbolId, Log.Level.USER);
            Log.printException(e);
        }
        return null;
    }

    @JsAccessible
    public Object getSymbolDataByDisplayMode(String componentId, String symbolId, String displayMode) {
        try {
            return DatabaseAccess.getParameterValueByDisplayMode(componentId, symbolId, displayMode);
        } catch (Exception e) {
            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Symbol value null : " + symbolId, Log.Level.USER);
            Log.printException(e);
        }
        return null;
    }

    @JsAccessible
    public String getSymbolValues(String componentId, String symbolId) {
        try {
            StringBuilder builder = new StringBuilder();
            String[] comboValues = DatabaseAccess.getSymbolArrayValues(componentId, symbolId);
            builder = getArrayValuesAsString(builder, comboValues);
            return builder.toString();
        } catch (Exception e) {
            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Symbol value null : " + symbolId, Log.Level.USER);
            Log.printException(e);
        }
        return null;
    }

    @JsAccessible
    public String getSymbolValuesByDisplayMode(String componentId, String symbolId, String displayMode) {
        try {
            StringBuilder builder = new StringBuilder();
            String[] comboValues = DatabaseAccess.getSymbolArrayValuesByDisplayMode(componentId, symbolId, displayMode);
            builder = getArrayValuesAsString(builder, comboValues);
            return builder.toString();
        } catch (Exception e) {
            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Symbol value null : " + symbolId, Log.Level.USER);
            Log.printException(e);
        }
        return null;
    }

    @JsAccessible
    public String getSymbolDescription(String componentId, String symbolId) {
        return DatabaseAccess.getSymbolDescription(pluginConfig.pluginName(), componentId, symbolId);
    }

    @JsAccessible
    public Object getSymbolDefaultValue(String componentId, String symbolId) {
        return DatabaseAccess.getSymbolDefaultValue(pluginConfig.pluginName(), componentId, symbolId, "");
    }

    @JsAccessible
    public Object getSymbolDefaultValueByDisplayMode(String componentId, String symbolId, String displayMode) {
        return DatabaseAccess.getSymbolDefaultValue(pluginConfig.pluginName(), componentId, symbolId, displayMode);
    }

    @JsAccessible
    public void updateSymbolData(String componentId, String symbolId, Object value) {
        try {
            recentSymbolUpdatedByReact = symbolId;
            DatabaseAccess.setParameterValue(pluginConfig.pluginName(), componentId, symbolId, value);
        } catch (Exception ex) {
            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Database Update failed: " + symbolId, Log.Level.USER);
            Log.printException(ex);
        }
    }

    @JsAccessible
    public void updateMultipleSymbols(String componentId, Map<String, Object> symbolsInfo) {
        symbolsInfo.keySet().forEach(symbolId -> {
            try {
                updateSymbolData(componentId, symbolId, symbolsInfo.get(symbolId));
            } catch (Exception ex) {
                Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Database Update failed: " + symbolId, Log.Level.USER);
                Log.printException(ex);
            }
        });
    }

    @JsAccessible
    public String getSymbolType(String componentId, String symbolId) {
        return DatabaseAccess.getSymbolType(componentId, symbolId);
    }

    @JsAccessible
    public String getSymbolLabelName(String stComponent, String symbolID) {
        return DatabaseAccess.getLabelName(pluginConfig.pluginName(), stComponent, symbolID);
    }

    @JsAccessible
    public Object getSymbolMinValue(String stComponent, String symbolID) {
        return DatabaseAccess.getMinValue(pluginConfig.pluginName(), stComponent, symbolID);
    }

    @JsAccessible
    public Object getSymbolMaxValue(String stComponent, String symbolID) {
        return DatabaseAccess.getMaxValue(pluginConfig.pluginName(), stComponent, symbolID);
    }

    @JsAccessible
    public Object getSymbolVisibleStatus(String stComponent, String symbolID) {
        return DatabaseAccess.getSymbolVisibleStatus(pluginConfig.pluginName(), stComponent, symbolID);
    }

    @JsAccessible
    public Object getSymbolReadOnlyStatus(String stComponent, String symbolID) {
        return DatabaseAccess.getSymbolReadOnlyStatus(pluginConfig.pluginName(), stComponent, symbolID);
    }

    @JsAccessible
    public void addSymbolListener(String symbolId) {
        if (!symbolListenersList.contains(symbolId)) {
            symbolListenersList.add(symbolId);
        }
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

    @JsAccessible
    public boolean IsTrustZoneSupported() {
        return Environment.isTrustzoneEnabled();
    }

    @JsAccessible
    public void sendMessage(String componentID, String messageID, Map<String, Object> args) {
        DatabaseAccess.sendMessage(pluginConfig.pluginName(), componentID, messageID, args);
    }

    public void stateChanged(Symbol sy) {
        if (!symbolListenersList.contains(sy.getID())) {
            return;
        }
        if (recentSymbolUpdatedByReact.equals(sy.getID())) {
            recentSymbolUpdatedByReact = "";
            return;
        }
        executorService.execute(() -> {
            try {
                ConfigSymbol sym = (ConfigSymbol) sy;
                Object symbolValue = DatabaseAccess.getParameterValue(sym.getComponent().getID(), sym.getID());
                browserObject.getFrame().executeJavaScript("SymbolValueChanged(\""
                        + sym.getID() + "M*C" + symbolValue + "M*C" + sym.getReadOnly() + "M*C" + sym.getVisible() + "\")");
            } catch (Exception ex) {
                Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Unable to excute javascript api for symbol : " + sy.getID() + " ->" + ex, Log.Level.USER);
                Log.printException(ex);
            }
        });
    }

    public void componentActivated(Event event) {
//        try {
//            if (event instanceof DatabaseEvents.ComponentActivatedEvent) {
//                Component c = ((DatabaseEvents.ComponentActivatedEvent) event).component;
//                executorService.execute(() -> {
//                    try {
//                        browserObject.getFrame().executeJavaScript("ComponentActivated(\"" + c.getID() + "\")");
//                    } catch (Exception e) {
//                        Log.write(pluginConfig.pluginName(), Log.Severity.Error, "component activation status call to React failed: " + c.getID(), Log.Level.USER);
//                        Log.printException(e);
//                    }
//                });
//            }
//        } catch (Exception e) {
//            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "component activation status call to React failed", Log.Level.USER);
//            Log.printException(e);
//        }
    }

    public void componentDeActivated(Event event) {
//        try {
//            if (event instanceof DatabaseEvents.ComponentDeactivatedEvent) {
//                Component c = ((DatabaseEvents.ComponentDeactivatedEvent) event).component;
//                executorService.execute(() -> {
//                    try {
//                        browserObject.getFrame().executeJavaScript("ComponentDeActivated(\"" + c.getID() + "\")");
//                    } catch (Exception e) {
//                        Log.write(pluginConfig.pluginName(), Log.Severity.Error, "component deActivation status call to React failed: " + c.getID(), Log.Level.USER);
//                        Log.printException(e);
//                    }
//                });
//            }
//        } catch (Exception e) {
//            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "component deActivation status call to React failed", Log.Level.USER);
//            Log.printException(e);
//        }
    }

    public void clearJavaConnectorObjects() {
        executorService.shutdownNow();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            Log.write(pluginConfig.pluginName(), Log.Severity.Error, "Unable to terminate excutorService thread.", Log.Level.USER);
            Log.printException(ie);
            executorService.shutdownNow();
        }
        executorService = null;
        agent.destroy();
    }
}
