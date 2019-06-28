package com.google.api.client.json;

import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.Data;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Types;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class JsonParser {
    public abstract void close() throws IOException;

    public abstract BigInteger getBigIntegerValue() throws IOException;

    public abstract byte getByteValue() throws IOException;

    public abstract String getCurrentName() throws IOException;

    public abstract JsonToken getCurrentToken();

    public abstract BigDecimal getDecimalValue() throws IOException;

    public abstract double getDoubleValue() throws IOException;

    public abstract JsonFactory getFactory();

    public abstract float getFloatValue() throws IOException;

    public abstract int getIntValue() throws IOException;

    public abstract long getLongValue() throws IOException;

    public abstract short getShortValue() throws IOException;

    public abstract String getText() throws IOException;

    public abstract UnsignedInteger getUnsignedIntegerValue() throws IOException;

    public abstract UnsignedLong getUnsignedLongValue() throws IOException;

    public abstract JsonToken nextToken() throws IOException;

    public abstract JsonParser skipChildren() throws IOException;

    public final <T> T parseAndClose(Class<T> destinationClass, CustomizeJsonParser customizeParser) throws IOException {
        try {
            T parse = parse((Class) destinationClass, customizeParser);
            return parse;
        } finally {
            close();
        }
    }

    public final void skipToKey(String keyToFind) throws IOException {
        skipToKey(Collections.singleton(keyToFind));
    }

    public final String skipToKey(Set<String> keysToFind) throws IOException {
        JsonToken curToken = startParsingObjectOrArray();
        while (curToken == JsonToken.FIELD_NAME) {
            String key = getText();
            nextToken();
            if (keysToFind.contains(key)) {
                return key;
            }
            skipChildren();
            curToken = nextToken();
        }
        return null;
    }

    private JsonToken startParsing() throws IOException {
        JsonToken currentToken = getCurrentToken();
        if (currentToken == null) {
            currentToken = nextToken();
        }
        Preconditions.checkArgument(currentToken != null, "no JSON input found");
        return currentToken;
    }

    private JsonToken startParsingObjectOrArray() throws IOException {
        JsonToken currentToken = startParsing();
        switch (currentToken) {
            case START_OBJECT:
                currentToken = nextToken();
                boolean z = currentToken == JsonToken.FIELD_NAME || currentToken == JsonToken.END_OBJECT;
                Preconditions.checkArgument(z, currentToken);
                return currentToken;
            case START_ARRAY:
                return nextToken();
            default:
                return currentToken;
        }
    }

    public final void parseAndClose(Object destination, CustomizeJsonParser customizeParser) throws IOException {
        try {
            parse(destination, customizeParser);
        } finally {
            close();
        }
    }

    public final <T> T parse(Class<T> destinationClass, CustomizeJsonParser customizeParser) throws IOException {
        startParsing();
        return parse((Type) destinationClass, false, customizeParser);
    }

    public Object parse(Type dataType, boolean close, CustomizeJsonParser customizeParser) throws IOException {
        try {
            startParsing();
            Object parseValue = parseValue(null, dataType, new ArrayList(), null, customizeParser);
            return parseValue;
        } finally {
            if (close) {
                close();
            }
        }
    }

    public final void parse(Object destination, CustomizeJsonParser customizeParser) throws IOException {
        ArrayList context = new ArrayList();
        context.add(destination.getClass());
        parse(context, destination, customizeParser);
    }

    private void parse(ArrayList<Type> context, Object destination, CustomizeJsonParser customizeParser) throws IOException {
        if (destination instanceof GenericJson) {
            ((GenericJson) destination).setFactory(getFactory());
        }
        JsonToken curToken = startParsingObjectOrArray();
        Class<?> destinationClass = destination.getClass();
        ClassInfo classInfo = ClassInfo.of(destinationClass);
        boolean isGenericData = GenericData.class.isAssignableFrom(destinationClass);
        if (isGenericData || !Map.class.isAssignableFrom(destinationClass)) {
            while (curToken == JsonToken.FIELD_NAME) {
                String key = getText();
                nextToken();
                if (customizeParser == null || !customizeParser.stopAt(destination, key)) {
                    FieldInfo fieldInfo = classInfo.getFieldInfo(key);
                    if (fieldInfo != null) {
                        if (!fieldInfo.isFinal() || fieldInfo.isPrimitive()) {
                            Field field = fieldInfo.getField();
                            int contextSize = context.size();
                            context.add(field.getGenericType());
                            Object fieldValue = parseValue(field, fieldInfo.getGenericType(), context, destination, customizeParser);
                            context.remove(contextSize);
                            fieldInfo.setValue(destination, fieldValue);
                        } else {
                            throw new IllegalArgumentException("final array/object fields are not supported");
                        }
                    } else if (isGenericData) {
                        ((GenericData) destination).set(key, parseValue(null, null, context, destination, customizeParser));
                    } else {
                        if (customizeParser != null) {
                            customizeParser.handleUnrecognizedKey(destination, key);
                        }
                        skipChildren();
                    }
                    curToken = nextToken();
                } else {
                    return;
                }
            }
            return;
        }
        parseMap((Map) destination, Types.getMapValueParameter(destinationClass), context, customizeParser);
    }

    public final <T> Collection<T> parseArrayAndClose(Class<?> destinationCollectionClass, Class<T> destinationItemClass, CustomizeJsonParser customizeParser) throws IOException {
        try {
            Collection<T> parseArray = parseArray((Class) destinationCollectionClass, (Class) destinationItemClass, customizeParser);
            return parseArray;
        } finally {
            close();
        }
    }

    public final <T> void parseArrayAndClose(Collection<? super T> destinationCollection, Class<T> destinationItemClass, CustomizeJsonParser customizeParser) throws IOException {
        try {
            parseArray((Collection) destinationCollection, (Class) destinationItemClass, customizeParser);
        } finally {
            close();
        }
    }

    public final <T> Collection<T> parseArray(Class<?> destinationCollectionClass, Class<T> destinationItemClass, CustomizeJsonParser customizeParser) throws IOException {
        Collection destinationCollection = Data.newCollectionInstance(destinationCollectionClass);
        parseArray(destinationCollection, (Class) destinationItemClass, customizeParser);
        return destinationCollection;
    }

    public final <T> void parseArray(Collection<? super T> destinationCollection, Class<T> destinationItemClass, CustomizeJsonParser customizeParser) throws IOException {
        parseArray(destinationCollection, destinationItemClass, new ArrayList(), customizeParser);
    }

    private <T> void parseArray(Collection<T> destinationCollection, Type destinationItemType, ArrayList<Type> context, CustomizeJsonParser customizeParser) throws IOException {
        JsonToken curToken = startParsingObjectOrArray();
        while (curToken != JsonToken.END_ARRAY) {
            destinationCollection.add(parseValue(null, destinationItemType, context, destinationCollection, customizeParser));
            curToken = nextToken();
        }
    }

    private void parseMap(Map<String, Object> destinationMap, Type valueType, ArrayList<Type> context, CustomizeJsonParser customizeParser) throws IOException {
        JsonToken curToken = startParsingObjectOrArray();
        while (curToken == JsonToken.FIELD_NAME) {
            String key = getText();
            nextToken();
            if (customizeParser == null || !customizeParser.stopAt(destinationMap, key)) {
                destinationMap.put(key, parseValue(null, valueType, context, destinationMap, customizeParser));
                curToken = nextToken();
            } else {
                return;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.Object parseValue(java.lang.reflect.Field r21, java.lang.reflect.Type r22, java.util.ArrayList<java.lang.reflect.Type> r23, java.lang.Object r24, com.google.api.client.json.CustomizeJsonParser r25) throws java.io.IOException {
        /*
        r20 = this;
        r0 = r23;
        r1 = r22;
        r22 = com.google.api.client.util.Data.resolveWildcardTypeOrTypeVariable(r0, r1);
        r0 = r22;
        r15 = r0 instanceof java.lang.Class;
        if (r15 == 0) goto L_0x0053;
    L_0x000e:
        r15 = r22;
        r15 = (java.lang.Class) r15;
        r14 = r15;
    L_0x0013:
        r0 = r22;
        r15 = r0 instanceof java.lang.reflect.ParameterizedType;
        if (r15 == 0) goto L_0x0021;
    L_0x0019:
        r15 = r22;
        r15 = (java.lang.reflect.ParameterizedType) r15;
        r14 = com.google.api.client.util.Types.getRawClass(r15);
    L_0x0021:
        r13 = r20.getCurrentToken();
        r15 = com.google.api.client.json.JsonParser.C00041.$SwitchMap$com$google$api$client$json$JsonToken;
        r16 = r13.ordinal();
        r15 = r15[r16];
        switch(r15) {
            case 1: goto L_0x00d0;
            case 2: goto L_0x0055;
            case 3: goto L_0x0055;
            case 4: goto L_0x00d0;
            case 5: goto L_0x00d0;
            case 6: goto L_0x016c;
            case 7: goto L_0x016c;
            case 8: goto L_0x01bd;
            case 9: goto L_0x01bd;
            case 10: goto L_0x02b2;
            case 11: goto L_0x0318;
            default: goto L_0x0030;
        };
    L_0x0030:
        r15 = new java.lang.IllegalArgumentException;
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = r20.getCurrentName();
        r16 = r16.append(r17);
        r17 = ": unexpected JSON node type: ";
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r13);
        r16 = r16.toString();
        r15.<init>(r16);
        throw r15;
    L_0x0053:
        r14 = 0;
        goto L_0x0013;
    L_0x0055:
        r8 = com.google.api.client.util.Types.isArray(r22);
        if (r22 == 0) goto L_0x0067;
    L_0x005b:
        if (r8 != 0) goto L_0x0067;
    L_0x005d:
        if (r14 == 0) goto L_0x00bd;
    L_0x005f:
        r15 = java.util.Collection.class;
        r15 = com.google.api.client.util.Types.isAssignableToOrFrom(r14, r15);
        if (r15 == 0) goto L_0x00bd;
    L_0x0067:
        r15 = 1;
    L_0x0068:
        r16 = "%s: expected collection or array type but got %s for field %s";
        r17 = 3;
        r0 = r17;
        r0 = new java.lang.Object[r0];
        r17 = r0;
        r18 = 0;
        r19 = r20.getCurrentName();
        r17[r18] = r19;
        r18 = 1;
        r17[r18] = r22;
        r18 = 2;
        r17[r18] = r21;
        com.google.common.base.Preconditions.checkArgument(r15, r16, r17);
        r4 = 0;
        if (r25 == 0) goto L_0x0094;
    L_0x0088:
        if (r21 == 0) goto L_0x0094;
    L_0x008a:
        r0 = r25;
        r1 = r24;
        r2 = r21;
        r4 = r0.newInstanceForArray(r1, r2);
    L_0x0094:
        if (r4 != 0) goto L_0x009a;
    L_0x0096:
        r4 = com.google.api.client.util.Data.newCollectionInstance(r22);
    L_0x009a:
        r11 = 0;
        if (r8 == 0) goto L_0x00bf;
    L_0x009d:
        r11 = com.google.api.client.util.Types.getArrayComponentType(r22);
    L_0x00a1:
        r0 = r23;
        r11 = com.google.api.client.util.Data.resolveWildcardTypeOrTypeVariable(r0, r11);
        r0 = r20;
        r1 = r23;
        r2 = r25;
        r0.parseArray(r4, r11, r1, r2);
        if (r8 == 0) goto L_0x00ce;
    L_0x00b2:
        r0 = r23;
        r15 = com.google.api.client.util.Types.getRawArrayComponentType(r0, r11);
        r15 = com.google.api.client.util.Types.toArray(r4, r15);
    L_0x00bc:
        return r15;
    L_0x00bd:
        r15 = 0;
        goto L_0x0068;
    L_0x00bf:
        if (r14 == 0) goto L_0x00a1;
    L_0x00c1:
        r15 = java.lang.Iterable.class;
        r15 = r15.isAssignableFrom(r14);
        if (r15 == 0) goto L_0x00a1;
    L_0x00c9:
        r11 = com.google.api.client.util.Types.getIterableParameter(r22);
        goto L_0x00a1;
    L_0x00ce:
        r15 = r4;
        goto L_0x00bc;
    L_0x00d0:
        r15 = com.google.api.client.util.Types.isArray(r22);
        if (r15 != 0) goto L_0x014e;
    L_0x00d6:
        r15 = 1;
    L_0x00d7:
        r16 = "%s: expected object or map type but got %s for field %s";
        r17 = 3;
        r0 = r17;
        r0 = new java.lang.Object[r0];
        r17 = r0;
        r18 = 0;
        r19 = r20.getCurrentName();
        r17[r18] = r19;
        r18 = 1;
        r17[r18] = r22;
        r18 = 2;
        r17[r18] = r21;
        com.google.common.base.Preconditions.checkArgument(r15, r16, r17);
        r10 = 0;
        if (r14 == 0) goto L_0x0101;
    L_0x00f7:
        if (r25 == 0) goto L_0x0101;
    L_0x00f9:
        r0 = r25;
        r1 = r24;
        r10 = r0.newInstanceForObject(r1, r14);
    L_0x0101:
        if (r14 == 0) goto L_0x0150;
    L_0x0103:
        r15 = java.util.Map.class;
        r15 = com.google.api.client.util.Types.isAssignableToOrFrom(r14, r15);
        if (r15 == 0) goto L_0x0150;
    L_0x010b:
        r9 = 1;
    L_0x010c:
        if (r10 != 0) goto L_0x0380;
    L_0x010e:
        if (r9 != 0) goto L_0x0112;
    L_0x0110:
        if (r14 != 0) goto L_0x0152;
    L_0x0112:
        r10 = com.google.api.client.util.Data.newMapInstance(r14);
        r15 = r10;
    L_0x0117:
        r5 = r23.size();
        if (r22 == 0) goto L_0x0124;
    L_0x011d:
        r0 = r23;
        r1 = r22;
        r0.add(r1);
    L_0x0124:
        if (r9 == 0) goto L_0x015a;
    L_0x0126:
        r16 = com.google.api.client.util.GenericData.class;
        r0 = r16;
        r16 = r0.isAssignableFrom(r14);
        if (r16 != 0) goto L_0x015a;
    L_0x0130:
        r16 = java.util.Map.class;
        r0 = r16;
        r16 = r0.isAssignableFrom(r14);
        if (r16 == 0) goto L_0x0158;
    L_0x013a:
        r12 = com.google.api.client.util.Types.getMapValueParameter(r22);
    L_0x013e:
        if (r12 == 0) goto L_0x015a;
    L_0x0140:
        r6 = r15;
        r6 = (java.util.Map) r6;
        r0 = r20;
        r1 = r23;
        r2 = r25;
        r0.parseMap(r6, r12, r1, r2);
        goto L_0x00bc;
    L_0x014e:
        r15 = 0;
        goto L_0x00d7;
    L_0x0150:
        r9 = 0;
        goto L_0x010c;
    L_0x0152:
        r10 = com.google.api.client.util.Types.newInstance(r14);
        r15 = r10;
        goto L_0x0117;
    L_0x0158:
        r12 = 0;
        goto L_0x013e;
    L_0x015a:
        r0 = r20;
        r1 = r23;
        r2 = r25;
        r0.parse(r1, r15, r2);
        if (r22 == 0) goto L_0x00bc;
    L_0x0165:
        r0 = r23;
        r0.remove(r5);
        goto L_0x00bc;
    L_0x016c:
        if (r22 == 0) goto L_0x017c;
    L_0x016e:
        r15 = java.lang.Boolean.TYPE;
        if (r14 == r15) goto L_0x017c;
    L_0x0172:
        if (r14 == 0) goto L_0x01b7;
    L_0x0174:
        r15 = java.lang.Boolean.class;
        r15 = r14.isAssignableFrom(r15);
        if (r15 == 0) goto L_0x01b7;
    L_0x017c:
        r15 = 1;
    L_0x017d:
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = "%s: expected type Boolean or boolean but got %s for field %s";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r21;
        r16 = r0.append(r1);
        r16 = r16.toString();
        r17 = 3;
        r0 = r17;
        r0 = new java.lang.Object[r0];
        r17 = r0;
        r18 = 0;
        r19 = r20.getCurrentName();
        r17[r18] = r19;
        r18 = 1;
        r17[r18] = r22;
        r18 = 2;
        r17[r18] = r21;
        com.google.common.base.Preconditions.checkArgument(r15, r16, r17);
        r15 = com.google.api.client.json.JsonToken.VALUE_TRUE;
        if (r13 != r15) goto L_0x01b9;
    L_0x01b3:
        r15 = java.lang.Boolean.TRUE;
        goto L_0x00bc;
    L_0x01b7:
        r15 = 0;
        goto L_0x017d;
    L_0x01b9:
        r15 = java.lang.Boolean.FALSE;
        goto L_0x00bc;
    L_0x01bd:
        if (r21 == 0) goto L_0x01c9;
    L_0x01bf:
        r15 = com.google.api.client.json.JsonString.class;
        r0 = r21;
        r15 = r0.getAnnotation(r15);
        if (r15 != 0) goto L_0x01f3;
    L_0x01c9:
        r15 = 1;
    L_0x01ca:
        r16 = "%s: number type formatted as a JSON number cannot use @JsonString annotation on the field %s";
        r17 = 2;
        r0 = r17;
        r0 = new java.lang.Object[r0];
        r17 = r0;
        r18 = 0;
        r19 = r20.getCurrentName();
        r17[r18] = r19;
        r18 = 1;
        r17[r18] = r21;
        com.google.common.base.Preconditions.checkArgument(r15, r16, r17);
        if (r14 == 0) goto L_0x01ed;
    L_0x01e5:
        r15 = java.math.BigDecimal.class;
        r15 = r14.isAssignableFrom(r15);
        if (r15 == 0) goto L_0x01f5;
    L_0x01ed:
        r15 = r20.getDecimalValue();
        goto L_0x00bc;
    L_0x01f3:
        r15 = 0;
        goto L_0x01ca;
    L_0x01f5:
        r15 = java.math.BigInteger.class;
        if (r14 != r15) goto L_0x01ff;
    L_0x01f9:
        r15 = r20.getBigIntegerValue();
        goto L_0x00bc;
    L_0x01ff:
        r15 = com.google.common.primitives.UnsignedInteger.class;
        if (r14 != r15) goto L_0x0209;
    L_0x0203:
        r15 = r20.getUnsignedIntegerValue();
        goto L_0x00bc;
    L_0x0209:
        r15 = com.google.common.primitives.UnsignedLong.class;
        if (r14 != r15) goto L_0x0213;
    L_0x020d:
        r15 = r20.getUnsignedLongValue();
        goto L_0x00bc;
    L_0x0213:
        r15 = java.lang.Double.class;
        if (r14 == r15) goto L_0x021b;
    L_0x0217:
        r15 = java.lang.Double.TYPE;
        if (r14 != r15) goto L_0x0225;
    L_0x021b:
        r16 = r20.getDoubleValue();
        r15 = java.lang.Double.valueOf(r16);
        goto L_0x00bc;
    L_0x0225:
        r15 = java.lang.Long.class;
        if (r14 == r15) goto L_0x022d;
    L_0x0229:
        r15 = java.lang.Long.TYPE;
        if (r14 != r15) goto L_0x0237;
    L_0x022d:
        r16 = r20.getLongValue();
        r15 = java.lang.Long.valueOf(r16);
        goto L_0x00bc;
    L_0x0237:
        r15 = java.lang.Float.class;
        if (r14 == r15) goto L_0x023f;
    L_0x023b:
        r15 = java.lang.Float.TYPE;
        if (r14 != r15) goto L_0x0249;
    L_0x023f:
        r15 = r20.getFloatValue();
        r15 = java.lang.Float.valueOf(r15);
        goto L_0x00bc;
    L_0x0249:
        r15 = java.lang.Integer.class;
        if (r14 == r15) goto L_0x0251;
    L_0x024d:
        r15 = java.lang.Integer.TYPE;
        if (r14 != r15) goto L_0x025b;
    L_0x0251:
        r15 = r20.getIntValue();
        r15 = java.lang.Integer.valueOf(r15);
        goto L_0x00bc;
    L_0x025b:
        r15 = java.lang.Short.class;
        if (r14 == r15) goto L_0x0263;
    L_0x025f:
        r15 = java.lang.Short.TYPE;
        if (r14 != r15) goto L_0x026d;
    L_0x0263:
        r15 = r20.getShortValue();
        r15 = java.lang.Short.valueOf(r15);
        goto L_0x00bc;
    L_0x026d:
        r15 = java.lang.Byte.class;
        if (r14 == r15) goto L_0x0275;
    L_0x0271:
        r15 = java.lang.Byte.TYPE;
        if (r14 != r15) goto L_0x027f;
    L_0x0275:
        r15 = r20.getByteValue();
        r15 = java.lang.Byte.valueOf(r15);
        goto L_0x00bc;
    L_0x027f:
        r15 = new java.lang.IllegalArgumentException;
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = r20.getCurrentName();
        r16 = r16.append(r17);
        r17 = ": expected numeric type but got ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r22;
        r16 = r0.append(r1);
        r17 = " for field ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r21;
        r16 = r0.append(r1);
        r16 = r16.toString();
        r15.<init>(r16);
        throw r15;
    L_0x02b2:
        if (r14 == 0) goto L_0x02c8;
    L_0x02b4:
        r15 = java.lang.Number.class;
        r15 = r15.isAssignableFrom(r14);
        if (r15 == 0) goto L_0x02c8;
    L_0x02bc:
        if (r21 == 0) goto L_0x02ee;
    L_0x02be:
        r15 = com.google.api.client.json.JsonString.class;
        r0 = r21;
        r15 = r0.getAnnotation(r15);
        if (r15 == 0) goto L_0x02ee;
    L_0x02c8:
        r15 = 1;
    L_0x02c9:
        r16 = "%s: number field formatted as a JSON string must use the @JsonString annotation: %s";
        r17 = 2;
        r0 = r17;
        r0 = new java.lang.Object[r0];
        r17 = r0;
        r18 = 0;
        r19 = r20.getCurrentName();
        r17[r18] = r19;
        r18 = 1;
        r17[r18] = r21;
        com.google.common.base.Preconditions.checkArgument(r15, r16, r17);
        r15 = r20.getText();	 Catch:{ IllegalArgumentException -> 0x02f0 }
        r0 = r22;
        r15 = com.google.api.client.util.Data.parsePrimitiveValue(r0, r15);	 Catch:{ IllegalArgumentException -> 0x02f0 }
        goto L_0x00bc;
    L_0x02ee:
        r15 = 0;
        goto L_0x02c9;
    L_0x02f0:
        r7 = move-exception;
        r15 = new java.lang.IllegalArgumentException;
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = r20.getCurrentName();
        r16 = r16.append(r17);
        r17 = " for field ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r21;
        r16 = r0.append(r1);
        r16 = r16.toString();
        r0 = r16;
        r15.<init>(r0, r7);
        throw r15;
    L_0x0318:
        if (r14 == 0) goto L_0x0320;
    L_0x031a:
        r15 = r14.isPrimitive();
        if (r15 != 0) goto L_0x035a;
    L_0x0320:
        r15 = 1;
    L_0x0321:
        r16 = "%s: primitive number field but found a JSON null: %s";
        r17 = 2;
        r0 = r17;
        r0 = new java.lang.Object[r0];
        r17 = r0;
        r18 = 0;
        r19 = r20.getCurrentName();
        r17[r18] = r19;
        r18 = 1;
        r17[r18] = r21;
        com.google.common.base.Preconditions.checkArgument(r15, r16, r17);
        if (r14 == 0) goto L_0x0372;
    L_0x033c:
        r15 = r14.getModifiers();
        r15 = r15 & 1536;
        if (r15 == 0) goto L_0x0372;
    L_0x0344:
        r15 = java.util.Collection.class;
        r15 = com.google.api.client.util.Types.isAssignableToOrFrom(r14, r15);
        if (r15 == 0) goto L_0x035c;
    L_0x034c:
        r15 = com.google.api.client.util.Data.newCollectionInstance(r22);
        r15 = r15.getClass();
        r15 = com.google.api.client.util.Data.nullOf(r15);
        goto L_0x00bc;
    L_0x035a:
        r15 = 0;
        goto L_0x0321;
    L_0x035c:
        r15 = java.util.Map.class;
        r15 = com.google.api.client.util.Types.isAssignableToOrFrom(r14, r15);
        if (r15 == 0) goto L_0x0372;
    L_0x0364:
        r15 = com.google.api.client.util.Data.newMapInstance(r14);
        r15 = r15.getClass();
        r15 = com.google.api.client.util.Data.nullOf(r15);
        goto L_0x00bc;
    L_0x0372:
        r0 = r23;
        r1 = r22;
        r15 = com.google.api.client.util.Types.getRawArrayComponentType(r0, r1);
        r15 = com.google.api.client.util.Data.nullOf(r15);
        goto L_0x00bc;
    L_0x0380:
        r15 = r10;
        goto L_0x0117;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.json.JsonParser.parseValue(java.lang.reflect.Field, java.lang.reflect.Type, java.util.ArrayList, java.lang.Object, com.google.api.client.json.CustomizeJsonParser):java.lang.Object");
    }
}
