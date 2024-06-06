package com.microchip.mh3.plugin.generic_plugin.database.component.controller;

import com.microchip.h3.database.component.Component;
import com.microchip.h3.database.component.FrameworkComponent;
import com.microchip.mcc.harmony.Harmony3Library;
import com.microchip.mcc.harmony.HarmonyPluginInterface;
import java.util.Optional;

public class ComponentAgent {

    private static ComponentAgent singleton;
    private final HarmonyPluginInterface harmonyPluginInterface;

    public static ComponentAgent singleton() {
        if (singleton == null) {
            singleton = new ComponentAgent();
        }
        return singleton;
    }

    public ComponentAgent() {
        this.harmonyPluginInterface = Harmony3Library.harmonyPluginInterface();
    }

    public Optional<FrameworkComponent> findComponentById(String componentId) {
        Optional<FrameworkComponent> foundComponent = this.harmonyPluginInterface.getActiveComponents().filter(e -> e.getID().equals(componentId)).findFirst();

        if (foundComponent.isPresent()) {
            return foundComponent;
        }

        return this.harmonyPluginInterface.getAvailableFrameworkComponents().filter(e -> e.getID().equals(componentId)).findFirst();
    }

    public boolean isActive(Component component) {
        return this.harmonyPluginInterface.getActiveComponents().anyMatch(e -> e.getID().equals(component.getID()));
    }

    public boolean isSelected(Component component) {
        return this.harmonyPluginInterface.getSelectedComponents().stream().anyMatch(e -> e.getID().equals(component.getID()));
    }

}
