package com.google.api.client.xml;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.xml.XmlHttpParser;
import com.google.api.client.util.Data;
import com.google.api.client.util.FieldInfo;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class Xml {
    public static final String MEDIA_TYPE = new HttpMediaType(XmlHttpParser.CONTENT_TYPE).setCharsetParameter(Charsets.UTF_8).build();
    static final String TEXT_CONTENT = "text()";
    private static XmlPullParserFactory factory;

    public static class CustomizeParser {
        public boolean stopBeforeStartTag(String namespace, String localName) {
            return false;
        }

        public boolean stopAfterEndTag(String namespace, String localName) {
            return false;
        }
    }

    private static synchronized XmlPullParserFactory getParserFactory() throws XmlPullParserException {
        XmlPullParserFactory xmlPullParserFactory;
        synchronized (Xml.class) {
            if (factory == null) {
                factory = XmlPullParserFactory.newInstance(System.getProperty("org.xmlpull.v1.XmlPullParserFactory"), null);
                factory.setNamespaceAware(true);
            }
            xmlPullParserFactory = factory;
        }
        return xmlPullParserFactory;
    }

    public static XmlSerializer createSerializer() {
        try {
            return getParserFactory().newSerializer();
        } catch (XmlPullParserException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static XmlPullParser createParser() throws XmlPullParserException {
        return getParserFactory().newPullParser();
    }

    public static String toStringOf(Object element) {
        return new XmlNamespaceDictionary().toStringOf(null, element);
    }

    private static void parseAttributeOrTextContent(String stringValue, Field field, Type valueType, List<Type> context, Object destination, GenericXml genericXml, Map<String, Object> destinationMap, String name) {
        if (field != null || genericXml != null || destinationMap != null) {
            if (field != null) {
                valueType = field.getGenericType();
            }
            setValue(parseValue(valueType, context, stringValue), field, destination, genericXml, destinationMap, name);
        }
    }

    private static void setValue(Object value, Field field, Object destination, GenericXml genericXml, Map<String, Object> destinationMap, String name) {
        if (field != null) {
            FieldInfo.setFieldValue(field, destination, value);
        } else if (genericXml != null) {
            genericXml.set(name, value);
        } else {
            destinationMap.put(name, value);
        }
    }

    public static void parseElement(XmlPullParser parser, Object destination, XmlNamespaceDictionary namespaceDictionary, CustomizeParser customizeParser) throws IOException, XmlPullParserException {
        ArrayList<Type> context = new ArrayList();
        context.add(destination.getClass());
        parseElementInternal(parser, context, destination, null, namespaceDictionary, customizeParser);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean parseElementInternal(org.xmlpull.v1.XmlPullParser r50, java.util.ArrayList<java.lang.reflect.Type> r51, java.lang.Object r52, java.lang.reflect.Type r53, com.google.api.client.xml.XmlNamespaceDictionary r54, com.google.api.client.xml.Xml.CustomizeParser r55) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
        r0 = r52;
        r3 = r0 instanceof com.google.api.client.xml.GenericXml;
        if (r3 == 0) goto L_0x009c;
    L_0x0006:
        r3 = r52;
        r3 = (com.google.api.client.xml.GenericXml) r3;
        r8 = r3;
    L_0x000b:
        if (r8 != 0) goto L_0x009f;
    L_0x000d:
        r0 = r52;
        r3 = r0 instanceof java.util.Map;
        if (r3 == 0) goto L_0x009f;
    L_0x0013:
        r3 = java.util.Map.class;
        r0 = r52;
        r3 = r3.cast(r0);
        r3 = (java.util.Map) r3;
        r9 = r3;
    L_0x001e:
        if (r9 != 0) goto L_0x0022;
    L_0x0020:
        if (r52 != 0) goto L_0x00a2;
    L_0x0022:
        r33 = 0;
    L_0x0024:
        r3 = r50.getEventType();
        if (r3 != 0) goto L_0x002d;
    L_0x002a:
        r50.next();
    L_0x002d:
        r0 = r50;
        r1 = r54;
        parseNamespacesForElement(r0, r1);
        if (r8 == 0) goto L_0x0054;
    L_0x0036:
        r0 = r54;
        r8.namespaceDictionary = r0;
        r45 = r50.getName();
        r46 = r50.getNamespace();
        r0 = r54;
        r1 = r46;
        r26 = r0.getNamespaceAliasForUriErrorOnUnknown(r1);
        r3 = r26.length();
        if (r3 != 0) goto L_0x00ac;
    L_0x0050:
        r0 = r45;
        r8.name = r0;
    L_0x0054:
        if (r52 == 0) goto L_0x00d8;
    L_0x0056:
        r30 = r50.getAttributeCount();
        r39 = 0;
    L_0x005c:
        r0 = r39;
        r1 = r30;
        if (r0 >= r1) goto L_0x00d8;
    L_0x0062:
        r0 = r50;
        r1 = r39;
        r31 = r0.getAttributeName(r1);
        r0 = r50;
        r1 = r39;
        r32 = r0.getAttributeNamespace(r1);
        r3 = r32.length();
        if (r3 != 0) goto L_0x00c8;
    L_0x0078:
        r29 = "";
    L_0x007a:
        r3 = 1;
        r0 = r29;
        r1 = r32;
        r2 = r31;
        r10 = getFieldName(r3, r0, r1, r2);
        if (r33 != 0) goto L_0x00d1;
    L_0x0087:
        r4 = 0;
    L_0x0088:
        r0 = r50;
        r1 = r39;
        r3 = r0.getAttributeValue(r1);
        r5 = r53;
        r6 = r51;
        r7 = r52;
        parseAttributeOrTextContent(r3, r4, r5, r6, r7, r8, r9, r10);
        r39 = r39 + 1;
        goto L_0x005c;
    L_0x009c:
        r8 = 0;
        goto L_0x000b;
    L_0x009f:
        r9 = 0;
        goto L_0x001e;
    L_0x00a2:
        r3 = r52.getClass();
        r33 = com.google.api.client.util.ClassInfo.of(r3);
        goto L_0x0024;
    L_0x00ac:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r26;
        r3 = r3.append(r0);
        r6 = ":";
        r3 = r3.append(r6);
        r0 = r45;
        r3 = r3.append(r0);
        r45 = r3.toString();
        goto L_0x0050;
    L_0x00c8:
        r0 = r54;
        r1 = r32;
        r29 = r0.getNamespaceAliasForUriErrorOnUnknown(r1);
        goto L_0x007a;
    L_0x00d1:
        r0 = r33;
        r4 = r0.getField(r10);
        goto L_0x0088;
    L_0x00d8:
        r27 = new com.google.api.client.util.ArrayValueMap;
        r0 = r27;
        r1 = r52;
        r0.<init>(r1);
        r42 = 0;
    L_0x00e3:
        r35 = r50.next();
        switch(r35) {
            case 1: goto L_0x00eb;
            case 2: goto L_0x012c;
            case 3: goto L_0x00f1;
            case 4: goto L_0x0109;
            default: goto L_0x00ea;
        };
    L_0x00ea:
        goto L_0x00e3;
    L_0x00eb:
        r42 = 1;
    L_0x00ed:
        r27.setValues();
        return r42;
    L_0x00f1:
        if (r55 == 0) goto L_0x0106;
    L_0x00f3:
        r3 = r50.getNamespace();
        r6 = r50.getName();
        r0 = r55;
        r3 = r0.stopAfterEndTag(r3, r6);
        if (r3 == 0) goto L_0x0106;
    L_0x0103:
        r42 = 1;
    L_0x0105:
        goto L_0x00ed;
    L_0x0106:
        r42 = 0;
        goto L_0x0105;
    L_0x0109:
        if (r52 == 0) goto L_0x00e3;
    L_0x010b:
        if (r33 != 0) goto L_0x0123;
    L_0x010d:
        r4 = 0;
    L_0x010e:
        r11 = r50.getText();
        r18 = "text()";
        r12 = r4;
        r13 = r53;
        r14 = r51;
        r15 = r52;
        r16 = r8;
        r17 = r9;
        parseAttributeOrTextContent(r11, r12, r13, r14, r15, r16, r17, r18);
        goto L_0x00e3;
    L_0x0123:
        r3 = "text()";
        r0 = r33;
        r4 = r0.getField(r3);
        goto L_0x010e;
    L_0x012c:
        if (r55 == 0) goto L_0x0141;
    L_0x012e:
        r3 = r50.getNamespace();
        r6 = r50.getName();
        r0 = r55;
        r3 = r0.stopBeforeStartTag(r3, r6);
        if (r3 == 0) goto L_0x0141;
    L_0x013e:
        r42 = 1;
        goto L_0x00ed;
    L_0x0141:
        if (r52 != 0) goto L_0x0158;
    L_0x0143:
        r3 = 1;
        r6 = 0;
        r0 = r50;
        r1 = r51;
        parseTextContentForElement(r0, r1, r3, r6);
    L_0x014c:
        if (r42 != 0) goto L_0x0155;
    L_0x014e:
        r3 = r50.getEventType();
        r6 = 1;
        if (r3 != r6) goto L_0x00e3;
    L_0x0155:
        r42 = 1;
        goto L_0x00ed;
    L_0x0158:
        r0 = r50;
        r1 = r54;
        parseNamespacesForElement(r0, r1);
        r46 = r50.getNamespace();
        r0 = r54;
        r1 = r46;
        r26 = r0.getNamespaceAliasForUriErrorOnUnknown(r1);
        r3 = 0;
        r6 = r50.getName();
        r0 = r26;
        r1 = r46;
        r10 = getFieldName(r3, r0, r1, r6);
        if (r33 != 0) goto L_0x01c5;
    L_0x017a:
        r4 = 0;
    L_0x017b:
        if (r4 != 0) goto L_0x01cc;
    L_0x017d:
        r38 = r53;
    L_0x017f:
        r0 = r51;
        r1 = r38;
        r38 = com.google.api.client.util.Data.resolveWildcardTypeOrTypeVariable(r0, r1);
        r0 = r38;
        r3 = r0 instanceof java.lang.Class;
        if (r3 == 0) goto L_0x01d1;
    L_0x018d:
        r3 = r38;
        r3 = (java.lang.Class) r3;
        r36 = r3;
    L_0x0193:
        r0 = r38;
        r3 = r0 instanceof java.lang.reflect.ParameterizedType;
        if (r3 == 0) goto L_0x01a1;
    L_0x0199:
        r3 = r38;
        r3 = (java.lang.reflect.ParameterizedType) r3;
        r36 = com.google.api.client.util.Types.getRawClass(r3);
    L_0x01a1:
        r41 = com.google.api.client.util.Types.isArray(r38);
        if (r4 != 0) goto L_0x01d4;
    L_0x01a7:
        if (r9 != 0) goto L_0x01d4;
    L_0x01a9:
        if (r8 != 0) goto L_0x01d4;
    L_0x01ab:
        r40 = 1;
    L_0x01ad:
        if (r40 != 0) goto L_0x01b5;
    L_0x01af:
        r3 = com.google.api.client.util.Data.isPrimitive(r38);
        if (r3 == 0) goto L_0x01f2;
    L_0x01b5:
        r43 = 1;
    L_0x01b7:
        if (r43 == 0) goto L_0x014c;
    L_0x01b9:
        r3 = r50.next();
        switch(r3) {
            case 1: goto L_0x01c1;
            case 2: goto L_0x01d7;
            case 3: goto L_0x01da;
            case 4: goto L_0x01dd;
            default: goto L_0x01c0;
        };
    L_0x01c0:
        goto L_0x01b7;
    L_0x01c1:
        r42 = 1;
        goto L_0x00ed;
    L_0x01c5:
        r0 = r33;
        r4 = r0.getField(r10);
        goto L_0x017b;
    L_0x01cc:
        r38 = r4.getGenericType();
        goto L_0x017f;
    L_0x01d1:
        r36 = 0;
        goto L_0x0193;
    L_0x01d4:
        r40 = 0;
        goto L_0x01ad;
    L_0x01d7:
        r43 = r43 + 1;
        goto L_0x01b7;
    L_0x01da:
        r43 = r43 + -1;
        goto L_0x01b7;
    L_0x01dd:
        if (r40 != 0) goto L_0x01b7;
    L_0x01df:
        r3 = 1;
        r0 = r43;
        if (r0 != r3) goto L_0x01b7;
    L_0x01e4:
        r3 = r50.getText();
        r5 = r53;
        r6 = r51;
        r7 = r52;
        parseAttributeOrTextContent(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x01b7;
    L_0x01f2:
        if (r38 == 0) goto L_0x0200;
    L_0x01f4:
        if (r36 == 0) goto L_0x02b9;
    L_0x01f6:
        r3 = java.util.Map.class;
        r0 = r36;
        r3 = com.google.api.client.util.Types.isAssignableToOrFrom(r0, r3);
        if (r3 == 0) goto L_0x02b9;
    L_0x0200:
        r13 = com.google.api.client.util.Data.newMapInstance(r36);
        r34 = r51.size();
        if (r38 == 0) goto L_0x0211;
    L_0x020a:
        r0 = r51;
        r1 = r38;
        r0.add(r1);
    L_0x0211:
        if (r38 == 0) goto L_0x025a;
    L_0x0213:
        r3 = java.util.Map.class;
        r0 = r36;
        r3 = r3.isAssignableFrom(r0);
        if (r3 == 0) goto L_0x025a;
    L_0x021d:
        r14 = com.google.api.client.util.Types.getMapValueParameter(r38);
    L_0x0221:
        r0 = r51;
        r14 = com.google.api.client.util.Data.resolveWildcardTypeOrTypeVariable(r0, r14);
        r11 = r50;
        r12 = r51;
        r15 = r54;
        r16 = r55;
        r42 = parseElementInternal(r11, r12, r13, r14, r15, r16);
        if (r38 == 0) goto L_0x023c;
    L_0x0235:
        r0 = r51;
        r1 = r34;
        r0.remove(r1);
    L_0x023c:
        if (r9 == 0) goto L_0x025c;
    L_0x023e:
        r44 = r9.get(r10);
        r44 = (java.util.Collection) r44;
        if (r44 != 0) goto L_0x0253;
    L_0x0246:
        r44 = new java.util.ArrayList;
        r3 = 1;
        r0 = r44;
        r0.<init>(r3);
        r0 = r44;
        r9.put(r10, r0);
    L_0x0253:
        r0 = r44;
        r0.add(r13);
        goto L_0x014c;
    L_0x025a:
        r14 = 0;
        goto L_0x0221;
    L_0x025c:
        if (r4 == 0) goto L_0x0295;
    L_0x025e:
        r37 = com.google.api.client.util.FieldInfo.of(r4);
        r3 = java.lang.Object.class;
        r0 = r36;
        if (r0 != r3) goto L_0x028c;
    L_0x0268:
        r0 = r37;
        r1 = r52;
        r44 = r0.getValue(r1);
        r44 = (java.util.Collection) r44;
        if (r44 != 0) goto L_0x0285;
    L_0x0274:
        r44 = new java.util.ArrayList;
        r3 = 1;
        r0 = r44;
        r0.<init>(r3);
        r0 = r37;
        r1 = r52;
        r2 = r44;
        r0.setValue(r1, r2);
    L_0x0285:
        r0 = r44;
        r0.add(r13);
        goto L_0x014c;
    L_0x028c:
        r0 = r37;
        r1 = r52;
        r0.setValue(r1, r13);
        goto L_0x014c;
    L_0x0295:
        r28 = r52;
        r28 = (com.google.api.client.xml.GenericXml) r28;
        r0 = r28;
        r44 = r0.get(r10);
        r44 = (java.util.Collection) r44;
        if (r44 != 0) goto L_0x02b2;
    L_0x02a3:
        r44 = new java.util.ArrayList;
        r3 = 1;
        r0 = r44;
        r0.<init>(r3);
        r0 = r28;
        r1 = r44;
        r0.set(r10, r1);
    L_0x02b2:
        r0 = r44;
        r0.add(r13);
        goto L_0x014c;
    L_0x02b9:
        if (r41 != 0) goto L_0x02c5;
    L_0x02bb:
        r3 = java.util.Collection.class;
        r0 = r36;
        r3 = com.google.api.client.util.Types.isAssignableToOrFrom(r0, r3);
        if (r3 == 0) goto L_0x03c7;
    L_0x02c5:
        r37 = com.google.api.client.util.FieldInfo.of(r4);
        r17 = 0;
        if (r41 == 0) goto L_0x031b;
    L_0x02cd:
        r49 = com.google.api.client.util.Types.getArrayComponentType(r38);
    L_0x02d1:
        r0 = r51;
        r1 = r49;
        r47 = com.google.api.client.util.Types.getRawArrayComponentType(r0, r1);
        r0 = r51;
        r1 = r49;
        r49 = com.google.api.client.util.Data.resolveWildcardTypeOrTypeVariable(r0, r1);
        r0 = r49;
        r3 = r0 instanceof java.lang.Class;
        if (r3 == 0) goto L_0x0320;
    L_0x02e7:
        r3 = r49;
        r3 = (java.lang.Class) r3;
        r48 = r3;
    L_0x02ed:
        r0 = r49;
        r3 = r0 instanceof java.lang.reflect.ParameterizedType;
        if (r3 == 0) goto L_0x02fb;
    L_0x02f3:
        r3 = r49;
        r3 = (java.lang.reflect.ParameterizedType) r3;
        r48 = com.google.api.client.util.Types.getRawClass(r3);
    L_0x02fb:
        r3 = com.google.api.client.util.Data.isPrimitive(r49);
        if (r3 == 0) goto L_0x0323;
    L_0x0301:
        r3 = 0;
        r0 = r50;
        r1 = r51;
        r2 = r49;
        r17 = parseTextContentForElement(r0, r1, r3, r2);
    L_0x030c:
        if (r41 == 0) goto L_0x03a3;
    L_0x030e:
        if (r4 != 0) goto L_0x0398;
    L_0x0310:
        r0 = r27;
        r1 = r47;
        r2 = r17;
        r0.put(r10, r1, r2);
        goto L_0x014c;
    L_0x031b:
        r49 = com.google.api.client.util.Types.getIterableParameter(r38);
        goto L_0x02d1;
    L_0x0320:
        r48 = 0;
        goto L_0x02ed;
    L_0x0323:
        if (r49 == 0) goto L_0x0331;
    L_0x0325:
        if (r48 == 0) goto L_0x0372;
    L_0x0327:
        r3 = java.util.Map.class;
        r0 = r48;
        r3 = com.google.api.client.util.Types.isAssignableToOrFrom(r0, r3);
        if (r3 == 0) goto L_0x0372;
    L_0x0331:
        r17 = com.google.api.client.util.Data.newMapInstance(r48);
        r34 = r51.size();
        if (r49 == 0) goto L_0x0342;
    L_0x033b:
        r0 = r51;
        r1 = r49;
        r0.add(r1);
    L_0x0342:
        if (r49 == 0) goto L_0x0370;
    L_0x0344:
        r3 = java.util.Map.class;
        r0 = r48;
        r3 = r3.isAssignableFrom(r0);
        if (r3 == 0) goto L_0x0370;
    L_0x034e:
        r14 = com.google.api.client.util.Types.getMapValueParameter(r49);
    L_0x0352:
        r0 = r51;
        r14 = com.google.api.client.util.Data.resolveWildcardTypeOrTypeVariable(r0, r14);
        r15 = r50;
        r16 = r51;
        r18 = r14;
        r19 = r54;
        r20 = r55;
        r42 = parseElementInternal(r15, r16, r17, r18, r19, r20);
        if (r49 == 0) goto L_0x030c;
    L_0x0368:
        r0 = r51;
        r1 = r34;
        r0.remove(r1);
        goto L_0x030c;
    L_0x0370:
        r14 = 0;
        goto L_0x0352;
    L_0x0372:
        r17 = com.google.api.client.util.Types.newInstance(r47);
        r34 = r51.size();
        r0 = r51;
        r1 = r38;
        r0.add(r1);
        r18 = 0;
        r15 = r50;
        r16 = r51;
        r19 = r54;
        r20 = r55;
        r42 = parseElementInternal(r15, r16, r17, r18, r19, r20);
        r0 = r51;
        r1 = r34;
        r0.remove(r1);
        goto L_0x030c;
    L_0x0398:
        r0 = r27;
        r1 = r47;
        r2 = r17;
        r0.put(r4, r1, r2);
        goto L_0x014c;
    L_0x03a3:
        if (r4 != 0) goto L_0x03be;
    L_0x03a5:
        r5 = r9.get(r10);
    L_0x03a9:
        r5 = (java.util.Collection) r5;
        if (r5 != 0) goto L_0x03b7;
    L_0x03ad:
        r5 = com.google.api.client.util.Data.newCollectionInstance(r38);
        r6 = r4;
        r7 = r52;
        setValue(r5, r6, r7, r8, r9, r10);
    L_0x03b7:
        r0 = r17;
        r5.add(r0);
        goto L_0x014c;
    L_0x03be:
        r0 = r37;
        r1 = r52;
        r5 = r0.getValue(r1);
        goto L_0x03a9;
    L_0x03c7:
        r20 = com.google.api.client.util.Types.newInstance(r36);
        r34 = r51.size();
        r0 = r51;
        r1 = r38;
        r0.add(r1);
        r21 = 0;
        r18 = r50;
        r19 = r51;
        r22 = r54;
        r23 = r55;
        r42 = parseElementInternal(r18, r19, r20, r21, r22, r23);
        r0 = r51;
        r1 = r34;
        r0.remove(r1);
        r21 = r4;
        r22 = r52;
        r23 = r8;
        r24 = r9;
        r25 = r10;
        setValue(r20, r21, r22, r23, r24, r25);
        goto L_0x014c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.xml.Xml.parseElementInternal(org.xmlpull.v1.XmlPullParser, java.util.ArrayList, java.lang.Object, java.lang.reflect.Type, com.google.api.client.xml.XmlNamespaceDictionary, com.google.api.client.xml.Xml$CustomizeParser):boolean");
    }

    private static String getFieldName(boolean isAttribute, String alias, String namespace, String name) {
        if (!isAttribute && alias.length() == 0) {
            return name;
        }
        StringBuilder buf = new StringBuilder((alias.length() + 2) + name.length());
        if (isAttribute) {
            buf.append('@');
        }
        if (alias != "") {
            buf.append(alias).append(':');
        }
        return buf.append(name).toString();
    }

    private static Object parseTextContentForElement(XmlPullParser parser, List<Type> context, boolean ignoreTextContent, Type textContentType) throws XmlPullParserException, IOException {
        Object result = null;
        int level = 1;
        while (level != 0) {
            switch (parser.next()) {
                case 1:
                    level = 0;
                    break;
                case 2:
                    level++;
                    break;
                case 3:
                    level--;
                    break;
                case 4:
                    if (!ignoreTextContent && level == 1) {
                        result = parseValue(textContentType, context, parser.getText());
                        break;
                    }
                default:
                    break;
            }
        }
        return result;
    }

    private static Object parseValue(Type valueType, List<Type> context, String value) {
        valueType = Data.resolveWildcardTypeOrTypeVariable(context, valueType);
        if (valueType == Double.class || valueType == Double.TYPE) {
            if (value.equals("INF")) {
                return new Double(Double.POSITIVE_INFINITY);
            }
            if (value.equals("-INF")) {
                return new Double(Double.NEGATIVE_INFINITY);
            }
        }
        if (valueType == Float.class || valueType == Float.TYPE) {
            if (value.equals("INF")) {
                return Float.valueOf(Float.POSITIVE_INFINITY);
            }
            if (value.equals("-INF")) {
                return Float.valueOf(Float.NEGATIVE_INFINITY);
            }
        }
        return Data.parsePrimitiveValue(valueType, value);
    }

    private static void parseNamespacesForElement(XmlPullParser parser, XmlNamespaceDictionary namespaceDictionary) throws XmlPullParserException {
        Preconditions.checkState(parser.getEventType() == 2, "expected start of XML element, but got something else (event type %s)", Integer.valueOf(parser.getEventType()));
        int depth = parser.getDepth();
        int nsStart = parser.getNamespaceCount(depth - 1);
        int nsEnd = parser.getNamespaceCount(depth);
        for (int i = nsStart; i < nsEnd; i++) {
            String namespace = parser.getNamespaceUri(i);
            if (namespaceDictionary.getAliasForUri(namespace) == null) {
                String originalAlias;
                String prefix = parser.getNamespacePrefix(i);
                if (prefix == null) {
                    originalAlias = "";
                } else {
                    originalAlias = prefix;
                }
                String alias = originalAlias;
                int suffix = 1;
                while (namespaceDictionary.getUriForAlias(alias) != null) {
                    suffix++;
                    alias = originalAlias + suffix;
                }
                namespaceDictionary.set(alias, namespace);
            }
        }
    }

    private Xml() {
    }
}
