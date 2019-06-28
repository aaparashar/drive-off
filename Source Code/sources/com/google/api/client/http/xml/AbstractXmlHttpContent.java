package com.google.api.client.http.xml;

import com.google.api.client.http.AbstractHttpContent;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.OutputStream;
import org.xmlpull.v1.XmlSerializer;

public abstract class AbstractXmlHttpContent extends AbstractHttpContent {
    private final XmlNamespaceDictionary namespaceDictionary;

    protected abstract void writeTo(XmlSerializer xmlSerializer) throws IOException;

    protected AbstractXmlHttpContent(XmlNamespaceDictionary namespaceDictionary) {
        super(new HttpMediaType(Xml.MEDIA_TYPE));
        this.namespaceDictionary = (XmlNamespaceDictionary) Preconditions.checkNotNull(namespaceDictionary);
    }

    public final void writeTo(OutputStream out) throws IOException {
        XmlSerializer serializer = Xml.createSerializer();
        serializer.setOutput(out, getCharset().name());
        writeTo(serializer);
    }

    @Deprecated
    public AbstractXmlHttpContent setType(String type) {
        setMediaType(new HttpMediaType(type));
        return this;
    }

    public AbstractXmlHttpContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }

    public final XmlNamespaceDictionary getNamespaceDictionary() {
        return this.namespaceDictionary;
    }
}
