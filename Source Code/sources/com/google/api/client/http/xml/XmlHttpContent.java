package com.google.api.client.http.xml;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.common.base.Preconditions;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public class XmlHttpContent extends AbstractXmlHttpContent {
    private final Object data;
    private final String elementName;

    public XmlHttpContent(XmlNamespaceDictionary namespaceDictionary, String elementName, Object data) {
        super(namespaceDictionary);
        this.elementName = (String) Preconditions.checkNotNull(elementName);
        this.data = Preconditions.checkNotNull(data);
    }

    @Deprecated
    public XmlHttpContent setType(String type) {
        super.setType(type);
        return this;
    }

    public final void writeTo(XmlSerializer serializer) throws IOException {
        getNamespaceDictionary().serialize(serializer, this.elementName, this.data);
    }

    public XmlHttpContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }

    public final String getElementName() {
        return this.elementName;
    }

    public final Object getData() {
        return this.data;
    }
}
