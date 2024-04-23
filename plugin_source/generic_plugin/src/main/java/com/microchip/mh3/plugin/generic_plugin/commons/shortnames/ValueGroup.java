package com.microchip.mh3.plugin.generic_plugin.commons.shortnames;

import java.util.List;
import java.util.Optional;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "value-group")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValueGroup {

    @XmlAttribute(name = "id")
    private String id;
    @XmlElement(name = "value")
    private List<Value> values;

    public ValueGroup() {
    }

    public ValueGroup(String id, List<Value> values) {
        this.id = id;
        this.values = values;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "ValueGroup{" + "id=" + id + ", values=" + values + "}\n";
    }

    public Optional<String> getName(String caption) {
        return values.stream()
                .filter(value -> value.getCaption().contains(caption))
                .map(value -> value.getName())
                .findAny();
    }

    public boolean isCaptionExist(String caption) {
        return values.stream()
                .map(Value::getCaption)
                .filter(e -> caption.equals(e))
                .findAny()
                .isPresent();
    }

    public boolean isNameExist(String name) {
        return values.stream()
                .map(Value::getName)
                .anyMatch(e -> name.equals(e));
    }
}
