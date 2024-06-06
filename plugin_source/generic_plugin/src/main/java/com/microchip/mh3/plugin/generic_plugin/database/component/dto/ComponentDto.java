package com.microchip.mh3.plugin.generic_plugin.database.component.dto;

import com.microchip.h3.database.component.Component;
import com.microchip.mh3.plugin.generic_plugin.database.component.controller.ComponentAgent;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
public class ComponentDto {

    private final String componentId;
    private final String componentType;
    private final String description;
    private final String displayName;
    private final String displayType;
    private final String groupId;
    private final Boolean isActive;
    private final Boolean isSelected;

    public ComponentDto(Component component) {
        componentId = component.getID();
        componentType = component.getClass().getSimpleName();
        groupId = component.getGroup() != null ? component.getGroup().getID() : null;
        description = component.getDescription();
        displayName = component.getDisplayName();
        displayType = component.getDisplayType();
        
        isActive = ComponentAgent.singleton().isActive(component);
        
        isSelected = ComponentAgent.singleton().isSelected(component);
    }

}
