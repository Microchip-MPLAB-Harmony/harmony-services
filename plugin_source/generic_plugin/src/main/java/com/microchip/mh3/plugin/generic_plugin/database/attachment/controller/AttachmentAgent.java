package com.microchip.mh3.plugin.generic_plugin.database.attachment.controller;

import com.microchip.h3.database.component.Component;
import com.microchip.h3.database.component.FrameworkComponent;
import com.microchip.h3.database.component.attachment.ComponentAttachment;
import com.microchip.mcc.harmony.Harmony3Library;
import com.microchip.mcc.harmony.HarmonyPluginInterface;
import com.microchip.mh3.plugin.generic_plugin.database.component.controller.ComponentAgent;
import com.microchip.mh3.plugin.generic_plugin.database.component.dto.ComponentBaseDto;
import com.microchip.mh3.plugin.generic_plugin.database.component.dto.ComponentDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AttachmentAgent {

    private final HarmonyPluginInterface harmonyPluginInterface;
    private static AttachmentAgent singleton;

    public static AttachmentAgent singleton() {
        if (singleton == null) {
            singleton = new AttachmentAgent();
        }
        return singleton;
    }

    private AttachmentAgent() {
        harmonyPluginInterface = Harmony3Library.harmonyPluginInterface();
    }

    Optional<ComponentAttachment> findAttachment(String componentId, String attachmentId) {
        Optional<FrameworkComponent> foundComponent = ComponentAgent.singleton().findComponentById(componentId);

        if (!foundComponent.isPresent()) {
            return Optional.empty();
        }

        return foundComponent.get().getAttachments(null)
                .stream()
                .filter(e -> e.getID().equals(attachmentId))
                .findFirst();
    }

    public Map<String, List<ComponentBaseDto>> satisfiableComponents(FrameworkComponent component) {
        return component.getAttachments(null).stream().collect(Collectors.toMap(e -> e.getID(), this::satisfiableComponents));
    }

//    public List<ComponentDto> satisfiableComponents(ComponentAttachment attachment) {
//        return Stream.concat(harmonyPluginInterface.getAvailableFrameworkComponents(), harmonyPluginInterface.getActiveComponents())
//                .filter(e -> e instanceof FrameworkComponent)
//                .filter(e -> e.canSatisfyAttachment(attachment))
//                .map(ComponentDto::new)
//                .collect(Collectors.toList());
//    }

    public List<ComponentBaseDto> satisfiableComponents(ComponentAttachment attachment) {
        return Stream.concat(harmonyPluginInterface.getAvailableFrameworkComponents(), harmonyPluginInterface.getActiveComponents())
                .filter(e -> e instanceof FrameworkComponent)
                .filter(e -> e.canSatisfyAttachment(attachment))
                .map(ComponentBaseDto::new)
                .collect(Collectors.toList());
    }

}
