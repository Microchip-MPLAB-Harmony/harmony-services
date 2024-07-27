package com.microchip.mh3.plugin.generic_plugin.database.component.dto;

import com.microchip.h3.database.component.Component;
import com.microchip.h3.database.component.FrameworkComponent;
import com.microchip.h3.database.component.GeneratorComponent;
import com.microchip.mh3.plugin.generic_plugin.database.attachment.dto.AttachmentDto;
import com.microchip.mh3.plugin.generic_plugin.database.component.controller.ComponentAgent;
import java.util.Collections;
import java.util.List;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
public class ComponentDto extends ComponentBaseDto {

    private final String componentType;
    private final String description;
    private final String groupId;
    private final Boolean isActive;
    private final Boolean isSelected;

    private final List<AttachmentDto> attachments;

    private final List<ComponentDto> instances;

    public ComponentDto(Component component) {
        super(component);
        componentType = component.getClass().getSimpleName();
        groupId = component.getGroup() != null ? component.getGroup().getID() : null;
        description = component.getDescription();

        isActive = ComponentAgent.singleton().isActive(component);

        isSelected = ComponentAgent.singleton().isSelected(component);

        if (component instanceof FrameworkComponent) {
            attachments = ((FrameworkComponent) component).getAttachments(null)
                    .stream()
                    .map(AttachmentDto::new)
                    .collect(toList());
        } else {
            attachments = Collections.emptyList();
        }

        if (component instanceof GeneratorComponent) {
            instances = ((GeneratorComponent) component).getInstances().stream().map(ComponentDto::new).collect(toList());
        } else {
            instances = null;
        }
    }

}
