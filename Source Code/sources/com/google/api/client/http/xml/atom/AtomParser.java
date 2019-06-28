package com.google.api.client.http.xml.atom;

import com.google.api.client.http.xml.XmlHttpParser;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.Atom;

@Deprecated
public final class AtomParser extends XmlHttpParser {
    public AtomParser(XmlNamespaceDictionary namespaceDictionary) {
        super(namespaceDictionary, Atom.CONTENT_TYPE);
    }
}
