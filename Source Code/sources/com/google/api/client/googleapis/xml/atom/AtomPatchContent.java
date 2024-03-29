package com.google.api.client.googleapis.xml.atom;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.xml.atom.AtomContent;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;

public final class AtomPatchContent extends AtomContent {
    public AtomPatchContent(XmlNamespaceDictionary namespaceDictionary, Object patchEntry) {
        super(namespaceDictionary, patchEntry, true);
        setMediaType(new HttpMediaType(Xml.MEDIA_TYPE));
    }

    public AtomPatchContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }
}
