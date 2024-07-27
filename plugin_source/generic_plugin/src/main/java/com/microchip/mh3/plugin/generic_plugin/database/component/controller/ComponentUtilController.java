package com.microchip.mh3.plugin.generic_plugin.database.component.controller;

import com.microchip.h3.database.component.GroupComponent;
import com.microchip.h3.database.component.InstanceComponent;
import com.microchip.mcc.harmony.Harmony3Library;
import com.microchip.mcc.harmony.HarmonyPluginInterface;
import com.microchip.mh3.plugin.generic_plugin.database.component.dto.ComponentDto;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.OptionalArg;
import java.util.stream.Stream;

@ControllerPath("ComponentUtil")
public class ComponentUtilController {

    private final HarmonyPluginInterface harmonyPluginInterface;

    private static final String ROOT_GROUP_ID = "__ROOTVIEW";

    public ComponentUtilController() {
        harmonyPluginInterface = Harmony3Library.harmonyPluginInterface();
    }

    @ControllerMethod
    public Response getComponents(Request request, String[] componentIds) {
        List<ComponentDto> components = Stream.concat(harmonyPluginInterface.getAvailableFrameworkComponents(), harmonyPluginInterface.getActiveComponents())
                .filter(component -> Stream.of(componentIds).anyMatch(e -> component.getID().equals(e)))
                .map(component -> new ComponentDto(component))
                .collect(Collectors.toList());

        return Response.success(components);
    }

    @ControllerMethod
    public Response getAllComponents(Request request) {
        List<ComponentDto> components = Stream.concat(harmonyPluginInterface.getAvailableFrameworkComponents(), harmonyPluginInterface.getActiveComponents())
                .filter(component -> !(component instanceof InstanceComponent))
                .map(component -> new ComponentDto(component))
                .collect(Collectors.toList());

        return Response.success(components);
    }

    @ControllerMethod
    public Response getAvailableComponents(Request request) {
        List<ComponentDto> components = harmonyPluginInterface.getAvailableFrameworkComponents()
                .map(component -> new ComponentDto(component))
                .collect(Collectors.toList());

        return Response.success(components);
    }

    @ControllerMethod
    public Response getActiveComponents(Request request) {
        List<ComponentDto> components = harmonyPluginInterface.getActiveComponents()
                .filter(component -> !(component instanceof InstanceComponent))
                .map(component -> new ComponentDto(component))
                .collect(Collectors.toList());

        return Response.success(components);
    }

    @ControllerMethod
    public Response getSelectedComponents(Request request) {
        List<ComponentDto> components = harmonyPluginInterface.getSelectedComponents()
                .stream()
                .map(component -> new ComponentDto(component))
                .collect(Collectors.toList());

        return Response.success(components);
    }

    @ControllerMethod
    public Response setSelectedComponent(Request request, String componentId) {
        harmonyPluginInterface.setSelectedComponent(componentId);
        return Response.success();
    }

    @ControllerMethod
    public Response addToSelectedComponents(Request request, String componentId) {
        harmonyPluginInterface.addSelectedComponent(componentId);
        return Response.success();
    }

    @ControllerMethod
    public Response removeFromSelectedComponents(Request request, String componentId) {
        harmonyPluginInterface.removeSelectedComponent(componentId);
        return Response.success();
    }

    @ControllerMethod
    public Response activateComponents(Request request, @OptionalArg String groupComponentId, String[] componentIds) {

        if (groupComponentId == null || ROOT_GROUP_ID.equals(groupComponentId)) {
            harmonyPluginInterface.activateComponents(componentIds, ROOT_GROUP_ID, false);
            return Response.success();
        } else {
            GroupComponent foundGroup = harmonyPluginInterface.findGroup(groupComponentId);

            if (foundGroup != null) {
                harmonyPluginInterface.activateComponents(componentIds, groupComponentId, false);
                return Response.success();
            } else {
                return Response.error("Unable to activate component(s) : " + Arrays.toString(componentIds) + ", Invalid Group Component ID: " + groupComponentId, request);
            }
        }
    }

    @ControllerMethod
    public Response deactivateComponents(Request request, String[] componentIds) {
        harmonyPluginInterface.deactivateComponents(componentIds, false);
        return Response.success();
    }

    @ControllerMethod
    public Response setActiveGroup(Request request, String groupComponentId) {
        if (harmonyPluginInterface.setActiveGroup(groupComponentId)) {
            return Response.success();
        } else {
            return Response.error("Unable to set the given Group Component ID as active group. Group Component ID : " + groupComponentId, request);
        }
    }

    @ControllerMethod
    public Response findGroupComponent(Request request, String groupComponentId) {
        GroupComponent foundGroup;
        if (ROOT_GROUP_ID.equals(groupComponentId)) {
            foundGroup = harmonyPluginInterface.getRootGroup();
        } else {
            foundGroup = harmonyPluginInterface.findGroup(groupComponentId);
        }

        if (foundGroup != null) {
            return Response.success(new ComponentDto(foundGroup));
        } else {
            return Response.success();
        }
    }

    @ControllerMethod
    public Response createGroupComponent(Request request, @OptionalArg String parentId, String groupComponentId) {
        if (parentId == null) {
            parentId = ROOT_GROUP_ID;
        }

        GroupComponent groupComponent = harmonyPluginInterface.createGroup(parentId, groupComponentId);

        if (groupComponent != null) {
            return Response.success(new ComponentDto(groupComponent));
        } else {
            return Response.error("Unable to create Group Component : " + groupComponentId, request);
        }
    }

}
