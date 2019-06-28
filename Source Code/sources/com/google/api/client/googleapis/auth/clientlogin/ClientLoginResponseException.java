package com.google.api.client.googleapis.auth.clientlogin;

import com.google.api.client.googleapis.auth.clientlogin.ClientLogin.ErrorInfo;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

public class ClientLoginResponseException extends HttpResponseException {
    private static final long serialVersionUID = 4974317674023010928L;
    private final transient ErrorInfo details;

    ClientLoginResponseException(HttpResponse response, ErrorInfo details, String message) {
        super(response, message);
        this.details = details;
    }

    public final ErrorInfo getDetails() {
        return this.details;
    }
}
