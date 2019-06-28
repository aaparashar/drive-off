package com.google.api.client.xml;

import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.Types;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlObjectParser implements ObjectParser {
    private final XmlNamespaceDictionary namespaceDictionary;

    public XmlObjectParser(XmlNamespaceDictionary namespaceDictionary) {
        this.namespaceDictionary = (XmlNamespaceDictionary) Preconditions.checkNotNull(namespaceDictionary);
    }

    public final XmlNamespaceDictionary getNamespaceDictionary() {
        return this.namespaceDictionary;
    }

    private Object readObject(XmlPullParser parser, Type dataType) throws XmlPullParserException, IOException {
        Preconditions.checkArgument(dataType instanceof Class, "dataType has to be of Class<?>");
        Object result = Types.newInstance((Class) dataType);
        Xml.parseElement(parser, result, this.namespaceDictionary, null);
        return result;
    }

    public <T> T parseAndClose(InputStream in, Charset charset, Class<T> dataClass) throws IOException {
        return parseAndClose(in, charset, (Type) dataClass);
    }

    public Object parseAndClose(InputStream in, Charset charset, Type dataType) throws IOException {
        try {
            XmlPullParser parser = Xml.createParser();
            parser.setInput(in, charset.name());
            Object readObject = readObject(parser, dataType);
            in.close();
            return readObject;
        } catch (XmlPullParserException e) {
            IOException exception = new IOException();
            exception.initCause(e);
            throw exception;
        } catch (Throwable th) {
            in.close();
        }
    }

    public <T> T parseAndClose(Reader reader, Class<T> dataClass) throws IOException {
        return parseAndClose(reader, (Type) dataClass);
    }

    public Object parseAndClose(Reader reader, Type dataType) throws IOException {
        try {
            XmlPullParser parser = Xml.createParser();
            parser.setInput(reader);
            Object readObject = readObject(parser, dataType);
            reader.close();
            return readObject;
        } catch (XmlPullParserException e) {
            IOException exception = new IOException();
            exception.initCause(e);
            throw exception;
        } catch (Throwable th) {
            reader.close();
        }
    }
}
