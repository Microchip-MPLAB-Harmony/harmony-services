package com.microchip.mh3.plugin.generic_plugin.commons.shortnames;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "value")
@XmlAccessorType(XmlAccessType.FIELD)
public class Value {

    @XmlAttribute(name = "caption")
    private String caption;
    @XmlAttribute(name = "name")
    private String name;

    public Value() {
    }

    public Value(String caption, String name) {
        this.caption = caption;
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Value{" + "caption=" + caption + ", name=" + name + "}\n";
    }
}
