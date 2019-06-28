package com.google.api.client.xml;

import com.google.api.client.util.GenericData;

public class GenericXml extends GenericData implements Cloneable {
    public String name;
    public XmlNamespaceDictionary namespaceDictionary;

    public GenericXml clone() {
        return (GenericXml) super.clone();
    }

    public String toString() {
        XmlNamespaceDictionary namespaceDictionary = this.namespaceDictionary;
        if (namespaceDictionary == null) {
            namespaceDictionary = new XmlNamespaceDictionary();
        }
        return namespaceDictionary.toStringOf(this.name, this);
    }
}
