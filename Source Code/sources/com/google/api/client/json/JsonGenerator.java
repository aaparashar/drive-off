package com.google.api.client.json;

import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.Data;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Types;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map.Entry;

public abstract class JsonGenerator {
    public abstract void close() throws IOException;

    public abstract void flush() throws IOException;

    public abstract JsonFactory getFactory();

    public abstract void writeBoolean(boolean z) throws IOException;

    public abstract void writeEndArray() throws IOException;

    public abstract void writeEndObject() throws IOException;

    public abstract void writeFieldName(String str) throws IOException;

    public abstract void writeNull() throws IOException;

    public abstract void writeNumber(double d) throws IOException;

    public abstract void writeNumber(float f) throws IOException;

    public abstract void writeNumber(int i) throws IOException;

    public abstract void writeNumber(long j) throws IOException;

    public abstract void writeNumber(UnsignedInteger unsignedInteger) throws IOException;

    public abstract void writeNumber(UnsignedLong unsignedLong) throws IOException;

    public abstract void writeNumber(String str) throws IOException;

    public abstract void writeNumber(BigDecimal bigDecimal) throws IOException;

    public abstract void writeNumber(BigInteger bigInteger) throws IOException;

    public abstract void writeStartArray() throws IOException;

    public abstract void writeStartObject() throws IOException;

    public abstract void writeString(String str) throws IOException;

    public final void serialize(Object value) throws IOException {
        if (value != null) {
            Class<?> valueClass = value.getClass();
            if (Data.isNull(value)) {
                writeNull();
            } else if (value instanceof String) {
                writeString((String) value);
            } else if (value instanceof BigDecimal) {
                writeNumber((BigDecimal) value);
            } else if (value instanceof BigInteger) {
                writeNumber((BigInteger) value);
            } else if (value instanceof UnsignedInteger) {
                writeNumber((UnsignedInteger) value);
            } else if (value instanceof UnsignedLong) {
                writeNumber((UnsignedLong) value);
            } else if (value instanceof Double) {
                writeNumber(((Double) value).doubleValue());
            } else if (value instanceof Long) {
                writeNumber(((Long) value).longValue());
            } else if (value instanceof Float) {
                writeNumber(((Float) value).floatValue());
            } else if ((value instanceof Integer) || (value instanceof Short) || (value instanceof Byte)) {
                writeNumber(((Number) value).intValue());
            } else if (value instanceof Boolean) {
                writeBoolean(((Boolean) value).booleanValue());
            } else if (value instanceof DateTime) {
                writeString(((DateTime) value).toStringRfc3339());
            } else if ((value instanceof Iterable) || valueClass.isArray()) {
                writeStartArray();
                for (Object o : Types.iterableOf(value)) {
                    serialize(o);
                }
                writeEndArray();
            } else if (valueClass.isEnum()) {
                String name = FieldInfo.of((Enum) value).getName();
                if (name == null) {
                    writeNull();
                } else {
                    writeString(name);
                }
            } else {
                writeStartObject();
                ClassInfo classInfo = ClassInfo.of(valueClass);
                for (Entry<String, Object> entry : Data.mapOf(value).entrySet()) {
                    Object fieldValue = entry.getValue();
                    if (fieldValue != null) {
                        String fieldName = (String) entry.getKey();
                        if (fieldValue instanceof Number) {
                            Field field = classInfo.getField(fieldName);
                            if (!(field == null || field.getAnnotation(JsonString.class) == null)) {
                                fieldValue = fieldValue.toString();
                            }
                        }
                        writeFieldName(fieldName);
                        serialize(fieldValue);
                    }
                }
                writeEndObject();
            }
        }
    }

    public void enablePrettyPrint() throws IOException {
    }
}
