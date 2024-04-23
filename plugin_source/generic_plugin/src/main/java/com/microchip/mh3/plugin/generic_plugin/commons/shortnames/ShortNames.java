package com.microchip.mh3.plugin.generic_plugin.commons.shortnames;

import java.util.List;
import java.util.Optional;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "short-names")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShortNames {

    @XmlAttribute(name = "device", required = false)
    private String device;
    @XmlElement(name = "value-group")
    private List<ValueGroup> valueGroups;

    public ShortNames() {
    }

    public ShortNames(String device, List<ValueGroup> valueGroups) {
        this.device = device;
        this.valueGroups = valueGroups;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public List<ValueGroup> getValueGroups() {
        return valueGroups;
    }

    public Optional<ValueGroup> getValueGroup(String id) {
        return valueGroups.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    public void setValueGroups(List<ValueGroup> valueGroups) {
        this.valueGroups = valueGroups;
    }

    @Override
    public String toString() {
        return "ShortNames{" + "device=" + device + ", valueGroups=" + valueGroups + '}';
    }

}
