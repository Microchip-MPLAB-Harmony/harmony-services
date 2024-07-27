package com.microchip.mh3.plugin.generic_plugin.database.attachment.controller;

import com.microchip.h3.database.component.Component;
import com.microchip.h3.database.component.FrameworkComponent;
import com.microchip.h3.database.component.attachment.ComponentAttachment;
import com.microchip.mcc.harmony.Harmony3Library;
import com.microchip.mcc.harmony.HarmonyPluginInterface;
import com.microchip.mh3.plugin.generic_plugin.database.attachment.dto.AttachmentDto;
import com.microchip.mh3.plugin.generic_plugin.database.component.controller.ComponentAgent;
import com.microchip.mh3.plugin.generic_plugin.database.component.dto.ComponentBaseDto;
import com.microchip.mh3.plugin.generic_plugin.database.component.dto.ComponentDto;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerPath("Attachment")
public class AttachmentController {

    private final HarmonyPluginInterface harmonyPluginInterface;
    private final AttachmentAgent attachmentAgent;

    public AttachmentController() {
        harmonyPluginInterface = Harmony3Library.harmonyPluginInterface();

        attachmentAgent = AttachmentAgent.singleton();
    }

    @ControllerMethod
    public Response getAttachment(Request request, String componentId, String attachmentId) {

        Optional<FrameworkComponent> foundComponent = ComponentAgent.singleton().findComponentById(componentId);

        if (!foundComponent.isPresent()) {
            return Response.error("Component Not Found. Invalid Component ID : " + componentId, request);
        }

        Optional<ComponentAttachment> foundAttachment = foundComponent.get().getAttachments(null)
                .stream()
                .filter(e -> e.getID().equals(attachmentId))
                .findFirst();

        return foundAttachment.map(AttachmentDto::new)
                .map(Response::success)
                .orElse(Response.error("Attachment Not Found. Invalid Attachment ID : " + attachmentId, request));
    }

    @ControllerMethod
    public Response createConnection(Request request, String sourceComponentId, String sourceAttachmentId, String targetComponentId, String targetAttachmentId) {

        List<String> connection = Arrays.asList(sourceComponentId, sourceAttachmentId, targetComponentId, targetAttachmentId);

        if (harmonyPluginInterface.connectAttachments(Arrays.asList(connection), false)) {
            return Response.success();
        } else {
            return Response.error("Unable to create connection", request);
        }

    }

    @ControllerMethod
    public Response disconnectAttachment(Request request, String componentId, String attachmentId) {

        List<String> attachment = Arrays.asList(componentId, attachmentId);

        if (harmonyPluginInterface.disconnenctAttachments(Arrays.asList(attachment), false)) {
            return Response.success();
        } else {
            return Response.error("Unable to disconnect the attachment", request);
        }

    }

    @ControllerMethod
    public Response satisfiableComponents(Request request, String componentId, String attachmentId) {

        Optional<FrameworkComponent> foundComponent = ComponentAgent.singleton().findComponentById(componentId);

        if (!foundComponent.isPresent()) {
            return Response.error("Component Not Found. Invalid Component ID : " + componentId, request);
        }

        Optional<ComponentAttachment> foundAttachment = foundComponent.get().getAttachments(null)
                .stream()
                .filter(e -> e.getID().equals(attachmentId))
                .findFirst();

        if (!foundAttachment.isPresent()) {
            return Response.error("Attachment Not Found. Invalid Attachment ID : " + attachmentId, request);
        }

        ComponentAttachment attachment = foundAttachment.get();

        List<ComponentDto> components = Stream.concat(harmonyPluginInterface.getAvailableFrameworkComponents(), harmonyPluginInterface.getActiveComponents())
                .filter(e -> e instanceof FrameworkComponent)
                .filter(e -> e.canSatisfyAttachment(attachment))
                .map(ComponentDto::new)
                .collect(Collectors.toList());

        return Response.success(components);

    }

    @ControllerMethod
    public Response satisfiableComponents(Request request, String[] componentIds) {
        // componentId, attachmentId, list(ComponentDto)
        Map<String, Map<String, List<ComponentBaseDto>>> satisfiableComponents = Stream.concat(harmonyPluginInterface.getAvailableFrameworkComponents(), harmonyPluginInterface.getActiveComponents())
                .filter(component -> Stream.of(componentIds).anyMatch(e -> component.getID().equals(e)))
                .collect(Collectors.toMap(e -> e.getID(), attachmentAgent::satisfiableComponents));

        return Response.success(satisfiableComponents);

    }

}
