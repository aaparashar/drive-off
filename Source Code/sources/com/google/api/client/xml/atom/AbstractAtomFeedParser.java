package com.google.api.client.xml.atom;

import com.google.api.client.util.Types;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class AbstractAtomFeedParser<T> {
    private final Class<T> feedClass;
    private boolean feedParsed;
    private final InputStream inputStream;
    private final XmlNamespaceDictionary namespaceDictionary;
    private final XmlPullParser parser;

    protected abstract Object parseEntryInternal() throws IOException, XmlPullParserException;

    protected AbstractAtomFeedParser(XmlNamespaceDictionary namespaceDictionary, XmlPullParser parser, InputStream inputStream, Class<T> feedClass) {
        this.namespaceDictionary = (XmlNamespaceDictionary) Preconditions.checkNotNull(namespaceDictionary);
        this.parser = (XmlPullParser) Preconditions.checkNotNull(parser);
        this.inputStream = (InputStream) Preconditions.checkNotNull(inputStream);
        this.feedClass = (Class) Preconditions.checkNotNull(feedClass);
    }

    public final XmlPullParser getParser() {
        return this.parser;
    }

    public final InputStream getInputStream() {
        return this.inputStream;
    }

    public final Class<T> getFeedClass() {
        return this.feedClass;
    }

    public final XmlNamespaceDictionary getNamespaceDictionary() {
        return this.namespaceDictionary;
    }

    public T parseFeed() throws IOException, XmlPullParserException {
        boolean close = true;
        try {
            this.feedParsed = true;
            T result = Types.newInstance(this.feedClass);
            Xml.parseElement(this.parser, result, this.namespaceDictionary, StopAtAtomEntry.INSTANCE);
            close = false;
            return result;
        } finally {
            if (close) {
                close();
            }
        }
    }

    public Object parseNextEntry() throws IOException, XmlPullParserException {
        Object result = null;
        if (!this.feedParsed) {
            this.feedParsed = true;
            Xml.parseElement(this.parser, null, this.namespaceDictionary, StopAtAtomEntry.INSTANCE);
        }
        boolean close = true;
        try {
            if (this.parser.getEventType() == 2) {
                result = parseEntryInternal();
                this.parser.next();
                close = false;
            } else if (1 != null) {
                close();
            }
            return result;
        } finally {
            if (close) {
                close();
            }
        }
    }

    public void close() throws IOException {
        this.inputStream.close();
    }
}
