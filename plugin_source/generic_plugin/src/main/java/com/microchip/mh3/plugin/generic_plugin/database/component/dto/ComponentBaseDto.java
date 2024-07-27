package com.microchip.mh3.plugin.generic_plugin.database.component.dto;

import com.microchip.h3.database.component.Component;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
public class ComponentBaseDto {

    private final String componentId;
    private final String displayName;
    private final String displayType;

    public ComponentBaseDto(Component component) {
        componentId = component.getID();
        displayName = component.getDisplayName();
        displayType = component.getDisplayType();
    }
}
