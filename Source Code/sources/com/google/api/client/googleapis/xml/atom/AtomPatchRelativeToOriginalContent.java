package com.google.api.client.googleapis.xml.atom;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.xml.AbstractXmlHttpContent;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.Atom;
import com.google.common.base.Preconditions;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public final class AtomPatchRelativeToOriginalContent extends AbstractXmlHttpContent {
    private final Object originalEntry;
    private final Object patchedEntry;

    public AtomPatchRelativeToOriginalContent(XmlNamespaceDictionary namespaceDictionary, Object originalEntry, Object patchedEntry) {
        super(namespaceDictionary);
        this.originalEntry = Preconditions.checkNotNull(originalEntry);
        this.patchedEntry = Preconditions.checkNotNull(patchedEntry);
    }

    protected void writeTo(XmlSerializer serializer) throws IOException {
        getNamespaceDictionary().serialize(serializer, Atom.ATOM_NAMESPACE, "entry", GoogleAtom.computePatch(this.patchedEntry, this.originalEntry));
    }

    public AtomPatchRelativeToOriginalContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }

    public final Object getPatchedEntry() {
        return this.patchedEntry;
    }

    public final Object getOriginalEntry() {
        return this.originalEntry;
    }
}
