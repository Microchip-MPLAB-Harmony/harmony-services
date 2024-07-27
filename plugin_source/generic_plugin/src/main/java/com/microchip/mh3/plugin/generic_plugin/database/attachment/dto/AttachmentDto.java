package com.microchip.mh3.plugin.generic_plugin.database.attachment.dto;

import com.microchip.h3.database.component.attachment.ComponentAttachment;
import com.microchip.h3.database.component.attachment.ComponentDirectCapability;
import com.microchip.h3.database.component.attachment.ComponentDirectDependency;
import com.microchip.h3.database.component.attachment.ComponentGenericCapability;
import com.microchip.h3.database.component.attachment.ComponentGenericDependency;
import com.microchip.h3.database.component.attachment.ComponentMultiCapability;
import com.microchip.h3.database.component.attachment.ComponentMultiDependency;
import com.microchip.mh3.plugin.generic_plugin.database.attachment.controller.AttachmentAgent;
import static com.microchip.mh3.plugin.generic_plugin.database.attachment.dto.AttachmentCardinality.DIRECT;
import static com.microchip.mh3.plugin.generic_plugin.database.attachment.dto.AttachmentCardinality.MULTI;
import static com.microchip.mh3.plugin.generic_plugin.database.attachment.dto.AttachmentCardinality.GENERIC;
import static com.microchip.mh3.plugin.generic_plugin.database.attachment.dto.AttachmentPotential.CAPABILITY;
import static com.microchip.mh3.plugin.generic_plugin.database.attachment.dto.AttachmentPotential.DEPENDENCY;
import com.microchip.mh3.plugin.generic_plugin.database.component.dto.ComponentBaseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
public class AttachmentDto {

    private final String attachmentId;
    private final String componentId;
    private final AttachmentCardinality cardinality; // DIRECT, MULTI, GENERIC
    private final AttachmentPotential potential; // CAPABILITY, DEPENDENCY
    private final String type;
    private final String displayType;
    private final Boolean generic;
    private final Boolean required;
    private final Boolean attachable;
    private final Boolean enabled;
    private final Boolean connected;
    private final List<TargetAttachment> targets;
    private final List<ComponentBaseDto> satisfiableComponents;

    public AttachmentDto(ComponentAttachment attachment) {
        attachmentId = attachment.getID();

        componentId = attachment.getOwner().getOwnerComponent().getID();

        if (attachment instanceof ComponentDirectCapability) {
            cardinality = DIRECT;
            potential = CAPABILITY;
            generic = false;
        } else if (attachment instanceof ComponentDirectDependency) {
            cardinality = DIRECT;
            potential = DEPENDENCY;
            generic = false;
        } else if (attachment instanceof ComponentGenericCapability) {
            cardinality = GENERIC;
            potential = CAPABILITY;
            generic = true;
        } else if (attachment instanceof ComponentGenericDependency) {
            cardinality = GENERIC;
            potential = DEPENDENCY;
            generic = true;
        } else if (attachment instanceof ComponentMultiCapability) {
            cardinality = MULTI;
            potential = CAPABILITY;
            generic = false;
        } else if (attachment instanceof ComponentMultiDependency) {
            cardinality = MULTI;
            potential = DEPENDENCY;
            generic = false;
        } else {
            cardinality = GENERIC;
            potential = CAPABILITY;
            generic = true;
        }

        type = attachment.getType();
        displayType = attachment.getDisplayType();

        enabled = attachment.getEnabled();
        attachable = attachment.isAttachable();
        connected = attachment.isConnected();
        required = attachment.isRequired();

        targets = attachment.getTargetAttachments().stream().map(TargetAttachment::new).collect(Collectors.toList());

        satisfiableComponents = AttachmentAgent.singleton().satisfiableComponents(attachment);
    }

}
