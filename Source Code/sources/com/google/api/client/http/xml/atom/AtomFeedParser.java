package com.google.api.client.http.xml.atom;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.Types;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.AbstractAtomFeedParser;
import com.google.api.client.xml.atom.Atom;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class AtomFeedParser<T, E> extends AbstractAtomFeedParser<T> {
    private final Class<E> entryClass;

    public AtomFeedParser(XmlNamespaceDictionary namespaceDictionary, XmlPullParser parser, InputStream inputStream, Class<T> feedClass, Class<E> entryClass) {
        super(namespaceDictionary, parser, inputStream, feedClass);
        this.entryClass = (Class) Preconditions.checkNotNull(entryClass);
    }

    public E parseNextEntry() throws IOException, XmlPullParserException {
        return super.parseNextEntry();
    }

    protected Object parseEntryInternal() throws IOException, XmlPullParserException {
        E result = Types.newInstance(this.entryClass);
        Xml.parseElement(getParser(), result, getNamespaceDictionary(), null);
        return result;
    }

    public final Class<E> getEntryClass() {
        return this.entryClass;
    }

    public static <T, E> AtomFeedParser<T, E> create(HttpResponse response, XmlNamespaceDictionary namespaceDictionary, Class<T> feedClass, Class<E> entryClass) throws IOException, XmlPullParserException {
        InputStream content = response.getContent();
        try {
            Atom.checkContentType(response.getContentType());
            XmlPullParser parser = Xml.createParser();
            parser.setInput(content, null);
            AtomFeedParser<T, E> result = new AtomFeedParser(namespaceDictionary, parser, content, feedClass, entryClass);
            content = null;
            return result;
        } finally {
            if (content != null) {
                content.close();
            }
        }
    }
}
