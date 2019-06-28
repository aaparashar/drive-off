package com.google.api.client.http;

import com.google.api.client.util.ArrayValueMap;
import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.Data;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.Types;
import com.google.api.client.util.escape.CharEscapers;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UrlEncodedParser implements HttpParser, ObjectParser {
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    @Deprecated
    private final String contentType;

    @Deprecated
    public static class Builder {
        private String contentType = UrlEncodedParser.CONTENT_TYPE;

        protected Builder() {
        }

        public UrlEncodedParser build() {
            return new UrlEncodedParser(this.contentType);
        }

        public final String getContentType() {
            return this.contentType;
        }

        public Builder setContentType(String contentType) {
            this.contentType = (String) Preconditions.checkNotNull(contentType);
            return this;
        }
    }

    @Deprecated
    public final String getContentType() {
        return this.contentType;
    }

    public UrlEncodedParser() {
        this(CONTENT_TYPE);
    }

    @Deprecated
    protected UrlEncodedParser(String contentType) {
        this.contentType = contentType;
    }

    @Deprecated
    public <T> T parse(HttpResponse response, Class<T> dataClass) throws IOException {
        Object newInstance = Types.newInstance(dataClass);
        parse(response.parseAsString(), newInstance);
        return newInstance;
    }

    public static void parse(String content, Object data) {
        if (content != null) {
            Class<?> clazz = data.getClass();
            ClassInfo classInfo = ClassInfo.of(clazz);
            List<Type> context = Arrays.asList(new Type[]{clazz});
            GenericData genericData = GenericData.class.isAssignableFrom(clazz) ? (GenericData) data : null;
            Map<Object, Object> map = Map.class.isAssignableFrom(clazz) ? (Map) data : null;
            ArrayValueMap arrayValueMap = new ArrayValueMap(data);
            int length = content.length();
            int nextEquals = content.indexOf(61);
            int amp;
            for (int cur = 0; cur < length; cur = amp + 1) {
                String name;
                String stringValue;
                amp = content.indexOf(38, cur);
                if (amp == -1) {
                    amp = length;
                }
                if (nextEquals == -1 || nextEquals >= amp) {
                    name = content.substring(cur, amp);
                    stringValue = "";
                } else {
                    name = content.substring(cur, nextEquals);
                    stringValue = CharEscapers.decodeUri(content.substring(nextEquals + 1, amp));
                    nextEquals = content.indexOf(61, amp + 1);
                }
                name = CharEscapers.decodeUri(name);
                FieldInfo fieldInfo = classInfo.getFieldInfo(name);
                if (fieldInfo != null) {
                    Type type = Data.resolveWildcardTypeOrTypeVariable(context, fieldInfo.getGenericType());
                    if (Types.isArray(type)) {
                        Class<?> rawArrayComponentType = Types.getRawArrayComponentType(context, Types.getArrayComponentType(type));
                        arrayValueMap.put(fieldInfo.getField(), (Class) rawArrayComponentType, parseValue(rawArrayComponentType, context, stringValue));
                    } else if (Types.isAssignableToOrFrom(Types.getRawArrayComponentType(context, type), Iterable.class)) {
                        Collection<Object> collection = (Collection) fieldInfo.getValue(data);
                        if (collection == null) {
                            collection = Data.newCollectionInstance(type);
                            fieldInfo.setValue(data, collection);
                        }
                        collection.add(parseValue(type == Object.class ? null : Types.getIterableParameter(type), context, stringValue));
                    } else {
                        fieldInfo.setValue(data, parseValue(type, context, stringValue));
                    }
                } else if (map != null) {
                    ArrayList<String> listValue = (ArrayList) map.get(name);
                    if (listValue == null) {
                        listValue = new ArrayList();
                        if (genericData != null) {
                            genericData.set(name, listValue);
                        } else {
                            map.put(name, listValue);
                        }
                    }
                    listValue.add(stringValue);
                }
            }
            arrayValueMap.setValues();
        }
    }

    private static Object parseValue(Type valueType, List<Type> context, String value) {
        return Data.parsePrimitiveValue(Data.resolveWildcardTypeOrTypeVariable(context, valueType), value);
    }

    public static Builder builder() {
        return new Builder();
    }

    public <T> T parseAndClose(InputStream in, Charset charset, Class<T> dataClass) throws IOException {
        return parseAndClose(new InputStreamReader(in, charset), (Class) dataClass);
    }

    public Object parseAndClose(InputStream in, Charset charset, Type dataType) throws IOException {
        return parseAndClose(new InputStreamReader(in, charset), dataType);
    }

    public <T> T parseAndClose(Reader reader, Class<T> dataClass) throws IOException {
        return parseAndClose(reader, (Type) dataClass);
    }

    public Object parseAndClose(Reader reader, Type dataType) throws IOException {
        Preconditions.checkArgument(dataType instanceof Class, "dataType has to be of type Class<?>");
        Object newInstance = Types.newInstance((Class) dataType);
        parse(CharStreams.toString((Readable) reader), newInstance);
        return newInstance;
    }
}
