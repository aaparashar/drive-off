package com.google.api.client.http.xml.atom;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.xml.AbstractXmlHttpContent;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.Atom;
import com.google.common.base.Preconditions;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public class AtomContent extends AbstractXmlHttpContent {
    private final Object entry;
    private final boolean isEntry;

    protected AtomContent(XmlNamespaceDictionary namespaceDictionary, Object entry, boolean isEntry) {
        super(namespaceDictionary);
        setMediaType(new HttpMediaType(Atom.MEDIA_TYPE));
        this.entry = Preconditions.checkNotNull(entry);
        this.isEntry = isEntry;
    }

    public static AtomContent forEntry(XmlNamespaceDictionary namespaceDictionary, Object entry) {
        return new AtomContent(namespaceDictionary, entry, true);
    }

    public static AtomContent forFeed(XmlNamespaceDictionary namespaceDictionary, Object feed) {
        return new AtomContent(namespaceDictionary, feed, false);
    }

    @Deprecated
    public AtomContent setType(String type) {
        super.setType(type);
        return this;
    }

    public AtomContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }

    public final void writeTo(XmlSerializer serializer) throws IOException {
        getNamespaceDictionary().serialize(serializer, Atom.ATOM_NAMESPACE, this.isEntry ? "entry" : "feed", this.entry);
    }

    public final boolean isEntry() {
        return this.isEntry;
    }

    public final Object getData() {
        return this.entry;
    }
}
