package com.microchip.mh3.plugin.generic_plugin.database.component.controller;

import com.microchip.h3.database.component.FrameworkComponent;
import com.microchip.mcc.harmony.Harmony3Library;
import com.microchip.mcc.harmony.HarmonyPluginInterface;
import com.microchip.mh3.plugin.generic_plugin.database.component.dto.ComponentDto;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import java.util.Optional;

@ControllerPath("Component")
public class ComponentController {

    private final HarmonyPluginInterface harmonyPluginInterface;

    public ComponentController() {
        harmonyPluginInterface = Harmony3Library.harmonyPluginInterface();
    }

    @ControllerMethod
    public Response getComponent(Request request, String componentId) {

        Optional<FrameworkComponent> foundComponent = ComponentAgent.singleton().findComponentById(componentId);

        if (foundComponent.isPresent()) {
            return Response.success(new ComponentDto(foundComponent.get()));
        } else {
            return Response.error("Component Not Found. Invalid Component ID : " + componentId, request);
        }
    }

    @ControllerMethod
    public Response isActive(Request request, String componentId) {
        boolean isFound = harmonyPluginInterface.getActiveComponentIDs()
                .stream()
                .anyMatch(e -> e.equals(componentId));

        return Response.success(isFound);
    }

    @ControllerMethod
    public Response isSelected(Request request, String componentId) {
        boolean isFound = harmonyPluginInterface.getSelectedComponents()
                .stream()
                .anyMatch(e -> e.getID().equals(componentId));

        return Response.success(isFound);
    }

}
