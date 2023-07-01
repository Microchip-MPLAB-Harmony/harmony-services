/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.database;

import com.microchip.mcc.harmony.Harmony3Library;
import com.microchip.mcc.harmony.HarmonyPluginInterface;
import com.microchip.mh3.log.Log;
import com.teamdev.jxbrowser.js.JsAccessible;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javafx.application.Platform;

@JsAccessible
public class ComponentService {

    private HarmonyPluginInterface harmonyPluginInterface;
    private static ComponentService instance;
    String pluginName = "";

    public void destroy() {
        harmonyPluginInterface = null;
        instance = null;
    }

    public void updatePluginName(String name) {
        this.pluginName = name;
    }

    public static ComponentService singleton() {
        if (instance == null) {
            instance = new ComponentService();
        }
        return instance;
    }

    private ComponentService() {
        harmonyPluginInterface = Harmony3Library.harmonyPluginInterface();
    }

    @JsAccessible
    public boolean isComponentActive(String componentID) {
        return harmonyPluginInterface.getActiveComponentIDs()
                .stream()
                .anyMatch(e -> e.equals(componentID));
    }

    @JsAccessible
    public final boolean isComponentAvailable(String componentID) {
        return harmonyPluginInterface.getAvailableFrameworkComponents()
                .anyMatch(e -> e.getID().equals(componentID));
    }

    @JsAccessible
    public String[] getActiveComponents() {
        return harmonyPluginInterface.getActiveComponents().map(e-> e.getID()).toArray(String[] ::new);
    }
    
    @JsAccessible
    public final String[] getAvailableComponents() {
        return harmonyPluginInterface.getAvailableFrameworkComponents().map(e-> e.getID()).toArray(String[] ::new);
    }
    
    @JsAccessible
    public CompletableFuture<Void> activateComponent(String groupID, String componentID) {

        if (groupID == null) {
            Log.write(this.pluginName, Log.Severity.Warning, "Group ID can not be null. Activating component in Root");
            return activateComponent(componentID);
        } else {
            return CompletableFuture.runAsync(() -> this.activateComponentSneaky(groupID, componentID),
                    Platform::runLater);
        }
    }

    @JsAccessible
    public CompletableFuture<Void> activateComponent(String componentID) {
        return CompletableFuture.runAsync(() -> this.activateComponentSneaky(componentID),
                Platform::runLater);
    }

    // non-blocking
    @JsAccessible
    public CompletableFuture<Void> activateComponents(String groupID, List<String> componentIDs) {
        List<CompletableFuture<Void>> futures = componentIDs.stream()
                .map(e -> CompletableFuture.runAsync(() -> activateComponentSneaky(groupID, e), Platform::runLater))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    // non-blocking
    @JsAccessible
    public CompletableFuture<Void> activateComponents(List<String> componentIDs) {
        List<CompletableFuture<Void>> futures = componentIDs.stream()
                .map(e -> CompletableFuture.runAsync(() -> activateComponentSneaky(e), Platform::runLater))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    // non-blocking
    @JsAccessible
    public CompletableFuture<Void> deactivateComponent(String componentID) {
        return CompletableFuture.runAsync(() -> harmonyPluginInterface.deactivateComponents(new String[]{componentID}, false),
                Platform::runLater);
    }

    // non-blocking
    @JsAccessible
    public CompletableFuture<Void> deactivateComponents(List<String> componentIDs) {
        return CompletableFuture.runAsync(() -> harmonyPluginInterface.deactivateComponents(componentIDs.toArray(new String[0]), false),
                Platform::runLater);
    }

    // Sneaky suffix says that this method is wrapping a Checked Exception inside RuntimeException
    private void activateComponentSneaky(String componentID) {
        try {
            harmonyPluginInterface.activateComponent(componentID);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to activate Component.", ex);
        }
    }
    
    private void activateComponentSneaky(String groupID, String componentID) {
        try {
            harmonyPluginInterface.activateComponents(new String[]{componentID}, groupID, false);
        } catch (Exception ex) {
            Log.printException(ex);
        }
    }

    @JsAccessible
    public CompletableFuture<Void> createConnection(List<List<String>> connections) {
        return CompletableFuture.runAsync(() -> harmonyPluginInterface.connectAttachments(connections, false),
                Platform::runLater);
    }

    @JsAccessible
    public CompletableFuture<Void> disconnectAttachment(List<List<String>> connections) {
        return CompletableFuture.runAsync(() -> harmonyPluginInterface.disconnenctAttachments(connections, false),
                Platform::runLater);
    }

}
