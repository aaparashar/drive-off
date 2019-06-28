package com.google.api.client.googleapis.batch;

import com.google.api.client.googleapis.GoogleHeaders;
import java.io.IOException;

public interface BatchCallback<T, E> {
    void onFailure(E e, GoogleHeaders googleHeaders) throws IOException;

    void onSuccess(T t, GoogleHeaders googleHeaders);
}
