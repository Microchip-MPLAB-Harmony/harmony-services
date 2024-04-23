package com.microchip.mh3.plugin.generic_plugin.database.event;

import com.microchip.h3.database.component.Component;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ComponentDto {

    private final String componentId;
    private final String componentType;

    public ComponentDto(Component component) {
        componentId = component.getID();
        componentType = component.getClass().getSimpleName();
    }

}
