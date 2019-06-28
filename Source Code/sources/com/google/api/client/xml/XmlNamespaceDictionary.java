package com.google.api.client.xml;

import com.google.api.client.util.Data;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Types;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import org.xmlpull.v1.XmlSerializer;

public final class XmlNamespaceDictionary {
    private final HashMap<String, String> namespaceAliasToUriMap = new HashMap();
    private final HashMap<String, String> namespaceUriToAliasMap = new HashMap();

    class ElementSerializer {
        final List<String> attributeNames = new ArrayList();
        final List<Object> attributeValues = new ArrayList();
        private final boolean errorOnUnknown;
        final List<String> subElementNames = new ArrayList();
        final List<Object> subElementValues = new ArrayList();
        Object textValue = null;

        ElementSerializer(Object elementValue, boolean errorOnUnknown) {
            this.errorOnUnknown = errorOnUnknown;
            if (!Data.isPrimitive(elementValue.getClass()) || Data.isNull(elementValue)) {
                for (Entry<String, Object> entry : Data.mapOf(elementValue).entrySet()) {
                    Object fieldValue = entry.getValue();
                    if (!(fieldValue == null || Data.isNull(fieldValue))) {
                        String fieldName = (String) entry.getKey();
                        if ("text()".equals(fieldName)) {
                            this.textValue = fieldValue;
                        } else if (fieldName.charAt(0) == '@') {
                            this.attributeNames.add(fieldName.substring(1));
                            this.attributeValues.add(fieldValue);
                        } else {
                            this.subElementNames.add(fieldName);
                            this.subElementValues.add(fieldValue);
                        }
                    }
                }
                return;
            }
            this.textValue = elementValue;
        }

        void serialize(XmlSerializer serializer, String elementName) throws IOException {
            String elementLocalName = null;
            String elementNamespaceUri = null;
            if (elementName != null) {
                int colon = elementName.indexOf(58);
                elementLocalName = elementName.substring(colon + 1);
                elementNamespaceUri = XmlNamespaceDictionary.this.getNamespaceUriForAliasHandlingUnknown(this.errorOnUnknown, colon == -1 ? "" : elementName.substring(0, colon));
            }
            serialize(serializer, elementNamespaceUri, elementLocalName);
        }

        void serialize(XmlSerializer serializer, String elementNamespaceUri, String elementLocalName) throws IOException {
            int i;
            boolean errorOnUnknown = this.errorOnUnknown;
            if (elementLocalName == null) {
                if (errorOnUnknown) {
                    throw new IllegalArgumentException("XML name not specified");
                }
                elementLocalName = "unknownName";
            }
            serializer.startTag(elementNamespaceUri, elementLocalName);
            int num = this.attributeNames.size();
            for (i = 0; i < num; i++) {
                String attributeName = (String) this.attributeNames.get(i);
                int colon = attributeName.indexOf(58);
                serializer.attribute(colon == -1 ? null : XmlNamespaceDictionary.this.getNamespaceUriForAliasHandlingUnknown(errorOnUnknown, attributeName.substring(0, colon)), attributeName.substring(colon + 1), XmlNamespaceDictionary.toSerializedValue(this.attributeValues.get(i)));
            }
            if (this.textValue != null) {
                serializer.text(XmlNamespaceDictionary.toSerializedValue(this.textValue));
            }
            num = this.subElementNames.size();
            for (i = 0; i < num; i++) {
                Object subElementValue = this.subElementValues.get(i);
                String subElementName = (String) this.subElementNames.get(i);
                Class<? extends Object> valueClass = subElementValue.getClass();
                if ((subElementValue instanceof Iterable) || valueClass.isArray()) {
                    for (Object subElement : Types.iterableOf(subElementValue)) {
                        if (!(subElement == null || Data.isNull(subElement))) {
                            new ElementSerializer(subElement, errorOnUnknown).serialize(serializer, subElementName);
                        }
                    }
                } else {
                    new ElementSerializer(subElementValue, errorOnUnknown).serialize(serializer, subElementName);
                }
            }
            serializer.endTag(elementNamespaceUri, elementLocalName);
        }
    }

    public synchronized String getAliasForUri(String uri) {
        return (String) this.namespaceUriToAliasMap.get(Preconditions.checkNotNull(uri));
    }

    public synchronized String getUriForAlias(String alias) {
        return (String) this.namespaceAliasToUriMap.get(Preconditions.checkNotNull(alias));
    }

    public synchronized Map<String, String> getAliasToUriMap() {
        return Collections.unmodifiableMap(this.namespaceAliasToUriMap);
    }

    public synchronized Map<String, String> getUriToAliasMap() {
        return Collections.unmodifiableMap(this.namespaceUriToAliasMap);
    }

    public synchronized XmlNamespaceDictionary set(String alias, String uri) {
        String previousUri = null;
        String previousAlias = null;
        if (uri == null) {
            if (alias != null) {
                previousUri = (String) this.namespaceAliasToUriMap.remove(alias);
            }
        } else if (alias == null) {
            previousAlias = (String) this.namespaceUriToAliasMap.remove(uri);
        } else {
            previousUri = (String) this.namespaceAliasToUriMap.put(Preconditions.checkNotNull(alias), Preconditions.checkNotNull(uri));
            if (uri.equals(previousUri)) {
                previousUri = null;
            } else {
                previousAlias = (String) this.namespaceUriToAliasMap.put(uri, alias);
            }
        }
        if (previousUri != null) {
            this.namespaceUriToAliasMap.remove(previousUri);
        }
        if (previousAlias != null) {
            this.namespaceAliasToUriMap.remove(previousAlias);
        }
        return this;
    }

    public String toStringOf(String elementName, Object element) {
        try {
            StringWriter writer = new StringWriter();
            XmlSerializer serializer = Xml.createSerializer();
            serializer.setOutput(writer);
            serialize(serializer, elementName, element, false);
            return writer.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void serialize(XmlSerializer serializer, String elementNamespaceUri, String elementLocalName, Object element) throws IOException {
        serialize(serializer, elementNamespaceUri, elementLocalName, element, true);
    }

    public void serialize(XmlSerializer serializer, String elementName, Object element) throws IOException {
        serialize(serializer, elementName, element, true);
    }

    private void serialize(XmlSerializer serializer, String elementNamespaceUri, String elementLocalName, Object element, boolean errorOnUnknown) throws IOException {
        startDoc(serializer, element, errorOnUnknown, elementNamespaceUri == null ? null : getAliasForUri(elementNamespaceUri)).serialize(serializer, elementNamespaceUri, elementLocalName);
        serializer.endDocument();
    }

    private void serialize(XmlSerializer serializer, String elementName, Object element, boolean errorOnUnknown) throws IOException {
        String elementAlias = "";
        if (elementName != null) {
            int colon = elementName.indexOf(58);
            if (colon != -1) {
                elementAlias = elementName.substring(0, colon);
            }
        }
        startDoc(serializer, element, errorOnUnknown, elementAlias).serialize(serializer, elementName);
        serializer.endDocument();
    }

    private ElementSerializer startDoc(XmlSerializer serializer, Object element, boolean errorOnUnknown, String elementAlias) throws IOException {
        serializer.startDocument(null, null);
        SortedSet<String> aliases = new TreeSet();
        computeAliases(element, aliases);
        if (elementAlias != null) {
            aliases.add(elementAlias);
        }
        for (String alias : aliases) {
            serializer.setPrefix(alias, getNamespaceUriForAliasHandlingUnknown(errorOnUnknown, alias));
        }
        return new ElementSerializer(element, errorOnUnknown);
    }

    private void computeAliases(Object element, SortedSet<String> aliases) {
        for (Entry<String, Object> entry : Data.mapOf(element).entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                String name = (String) entry.getKey();
                if (!"text()".equals(name)) {
                    int colon = name.indexOf(58);
                    boolean isAttribute = name.charAt(0) == '@';
                    if (!(colon == -1 && isAttribute)) {
                        String alias;
                        if (colon == -1) {
                            alias = "";
                        } else {
                            alias = name.substring(name.charAt(0) == '@' ? 1 : 0, colon);
                        }
                        aliases.add(alias);
                    }
                    Class<?> valueClass = value.getClass();
                    if (!(isAttribute || Data.isPrimitive(valueClass))) {
                        if ((value instanceof Iterable) || valueClass.isArray()) {
                            for (Object subValue : Types.iterableOf(value)) {
                                computeAliases(subValue, aliases);
                            }
                        } else {
                            computeAliases(value, aliases);
                        }
                    }
                }
            }
        }
    }

    String getNamespaceUriForAliasHandlingUnknown(boolean errorOnUnknown, String alias) {
        String result = getUriForAlias(alias);
        if (result != null) {
            return result;
        }
        boolean z;
        if (errorOnUnknown) {
            z = false;
        } else {
            z = true;
        }
        String str = "unrecognized alias: %s";
        Object[] objArr = new Object[1];
        objArr[0] = alias.length() == 0 ? "(default)" : alias;
        Preconditions.checkArgument(z, str, objArr);
        return "http://unknown/" + alias;
    }

    String getNamespaceAliasForUriErrorOnUnknown(String namespaceUri) {
        boolean z;
        String result = getAliasForUri(namespaceUri);
        if (result != null) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "invalid XML: no alias declared for namesapce <%s>; work-around by setting XML namepace directly by calling the set method of %s", namespaceUri, XmlNamespaceDictionary.class.getName());
        return result;
    }

    static String toSerializedValue(Object value) {
        if (value instanceof Float) {
            Float f = (Float) value;
            if (f.floatValue() == Float.POSITIVE_INFINITY) {
                return "INF";
            }
            if (f.floatValue() == Float.NEGATIVE_INFINITY) {
                return "-INF";
            }
        }
        if (value instanceof Double) {
            Double d = (Double) value;
            if (d.doubleValue() == Double.POSITIVE_INFINITY) {
                return "INF";
            }
            if (d.doubleValue() == Double.NEGATIVE_INFINITY) {
                return "-INF";
            }
        }
        if ((value instanceof String) || (value instanceof Number) || (value instanceof Boolean)) {
            return value.toString();
        }
        if (value instanceof DateTime) {
            return ((DateTime) value).toStringRfc3339();
        }
        if (value instanceof Enum) {
            return FieldInfo.of((Enum) value).getName();
        }
        throw new IllegalArgumentException("unrecognized value type: " + value.getClass());
    }
}
