package com.google.api.client.googleapis.xml.atom;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Types;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.AbstractAtomFeedParser;
import com.google.api.client.xml.atom.Atom;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class MultiKindFeedParser<T> extends AbstractAtomFeedParser<T> {
    private final HashMap<String, Class<?>> kindToEntryClassMap = new HashMap();

    MultiKindFeedParser(XmlNamespaceDictionary namespaceDictionary, XmlPullParser parser, InputStream inputStream, Class<T> feedClass) {
        super(namespaceDictionary, parser, inputStream, feedClass);
    }

    public void setEntryClasses(Class<?>... entryClasses) {
        HashMap<String, Class<?>> kindToEntryClassMap = this.kindToEntryClassMap;
        for (Class<?> entryClass : entryClasses) {
            Field field = ClassInfo.of(entryClass).getField("@gd:kind");
            if (field == null) {
                throw new IllegalArgumentException("missing @gd:kind field for " + entryClass.getName());
            }
            String kind = (String) FieldInfo.getFieldValue(field, Types.newInstance(entryClass));
            if (kind == null) {
                throw new IllegalArgumentException("missing value for @gd:kind field in " + entryClass.getName());
            }
            kindToEntryClassMap.put(kind, entryClass);
        }
    }

    protected Object parseEntryInternal() throws IOException, XmlPullParserException {
        XmlPullParser parser = getParser();
        String kind = parser.getAttributeValue(GoogleAtom.GD_NAMESPACE, "kind");
        Class<?> entryClass = (Class) this.kindToEntryClassMap.get(kind);
        if (entryClass == null) {
            throw new IllegalArgumentException("unrecognized kind: " + kind);
        }
        Object result = Types.newInstance(entryClass);
        Xml.parseElement(parser, result, getNamespaceDictionary(), null);
        return result;
    }

    public static <T, E> MultiKindFeedParser<T> create(HttpResponse response, XmlNamespaceDictionary namespaceDictionary, Class<T> feedClass, Class<E>... entryClasses) throws IOException, XmlPullParserException {
        InputStream content = response.getContent();
        try {
            Atom.checkContentType(response.getContentType());
            XmlPullParser parser = Xml.createParser();
            parser.setInput(content, null);
            MultiKindFeedParser<T> result = new MultiKindFeedParser(namespaceDictionary, parser, content, feedClass);
            result.setEntryClasses(entryClasses);
            return result;
        } finally {
            content.close();
        }
    }
}
