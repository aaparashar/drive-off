package com.google.api.client.googleapis.batch.json;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.batch.BatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonErrorContainer;
import java.io.IOException;

public abstract class JsonBatchCallback<T> implements BatchCallback<T, GoogleJsonErrorContainer> {
    public abstract void onFailure(GoogleJsonError googleJsonError, GoogleHeaders googleHeaders) throws IOException;

    public final void onFailure(GoogleJsonErrorContainer e, GoogleHeaders responseHeaders) throws IOException {
        onFailure(e.getError(), responseHeaders);
    }
}
