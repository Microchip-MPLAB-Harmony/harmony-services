package com.microchip.mh3.plugin.generic_plugin.database.attachment.dto;

import com.microchip.h3.database.component.attachment.ComponentAttachment;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
@ToString
public class TargetAttachment {
    
    private final String attachmentId;
    private final String componentId;
    
    public TargetAttachment(ComponentAttachment attachment) {
        attachmentId = attachment.getID();
        
        componentId = attachment.getOwner().getOwnerComponent().getID();
    }
    
}
