package com.google.api.client.http.xml;

import com.google.api.client.http.HttpParser;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.Types;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@Deprecated
public class XmlHttpParser implements HttpParser {
    public static final String CONTENT_TYPE = "application/xml";
    private final String contentType;
    private final XmlNamespaceDictionary namespaceDictionary;

    public static class Builder {
        private String contentType = XmlHttpParser.CONTENT_TYPE;
        private final XmlNamespaceDictionary namespaceDictionary;

        protected Builder(XmlNamespaceDictionary namespaceDictionary) {
            this.namespaceDictionary = (XmlNamespaceDictionary) Preconditions.checkNotNull(namespaceDictionary);
        }

        public XmlHttpParser build() {
            return new XmlHttpParser(this.namespaceDictionary, this.contentType);
        }

        public final String getContentType() {
            return this.contentType;
        }

        public Builder setContentType(String contentType) {
            this.contentType = (String) Preconditions.checkNotNull(contentType);
            return this;
        }

        public final XmlNamespaceDictionary getNamespaceDictionary() {
            return this.namespaceDictionary;
        }
    }

    public XmlHttpParser(XmlNamespaceDictionary namespaceDictionary) {
        this(namespaceDictionary, CONTENT_TYPE);
    }

    protected XmlHttpParser(XmlNamespaceDictionary namespaceDictionary, String contentType) {
        this.namespaceDictionary = (XmlNamespaceDictionary) Preconditions.checkNotNull(namespaceDictionary);
        this.contentType = contentType;
    }

    public final String getContentType() {
        return this.contentType;
    }

    public <T> T parse(HttpResponse response, Class<T> dataClass) throws IOException {
        InputStream content = response.getContent();
        try {
            T result = Types.newInstance(dataClass);
            XmlPullParser parser = Xml.createParser();
            parser.setInput(content, null);
            Xml.parseElement(parser, result, this.namespaceDictionary, null);
            content.close();
            return result;
        } catch (XmlPullParserException e) {
            IOException exception = new IOException();
            exception.initCause(e);
            throw exception;
        } catch (Throwable th) {
            content.close();
        }
    }

    public final XmlNamespaceDictionary getNamespaceDictionary() {
        return this.namespaceDictionary;
    }

    public static Builder builder(XmlNamespaceDictionary namespaceDictionary) {
        return new Builder(namespaceDictionary);
    }
}
