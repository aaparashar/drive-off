package com.google.api.client.json;

import com.google.api.client.util.GenericData;

public class GenericJson extends GenericData implements Cloneable {
    private JsonFactory jsonFactory;

    public final JsonFactory getFactory() {
        return this.jsonFactory;
    }

    public final void setFactory(JsonFactory factory) {
        this.jsonFactory = factory;
    }

    public String toString() {
        if (this.jsonFactory != null) {
            return this.jsonFactory.toString(this);
        }
        return super.toString();
    }

    public String toPrettyString() {
        if (this.jsonFactory != null) {
            return this.jsonFactory.toPrettyString(this);
        }
        return super.toString();
    }

    public GenericJson clone() {
        return (GenericJson) super.clone();
    }
}
