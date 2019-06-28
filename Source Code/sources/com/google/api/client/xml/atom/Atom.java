package com.google.api.client.xml.atom;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.xml.Xml.CustomizeParser;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;

public final class Atom {
    public static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
    @Deprecated
    public static final String CONTENT_TYPE = "application/atom+xml";
    public static final String MEDIA_TYPE = new HttpMediaType(CONTENT_TYPE).setCharsetParameter(Charsets.UTF_8).build();

    static final class StopAtAtomEntry extends CustomizeParser {
        static final StopAtAtomEntry INSTANCE = new StopAtAtomEntry();

        StopAtAtomEntry() {
        }

        public boolean stopBeforeStartTag(String namespace, String localName) {
            return "entry".equals(localName) && Atom.ATOM_NAMESPACE.equals(namespace);
        }
    }

    private Atom() {
    }

    public static void checkContentType(String contentType) {
        boolean z;
        if (contentType != null) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z);
        Preconditions.checkArgument(HttpMediaType.equalsIgnoreParameters(MEDIA_TYPE, contentType), "Wrong content type: expected <" + MEDIA_TYPE + "> but got <%s>", contentType);
    }
}
