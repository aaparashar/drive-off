package com.google.api.client.googleapis.json;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.util.StringUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.IOException;

public class GoogleJsonResponseException extends HttpResponseException {
    private static final long serialVersionUID = 409811126989994864L;
    private final transient GoogleJsonError details;
    @Deprecated
    private final transient JsonFactory jsonFactory;

    private GoogleJsonResponseException(JsonFactory jsonFactory, HttpResponse response, GoogleJsonError details, String message) {
        super(response, message);
        this.jsonFactory = jsonFactory;
        this.details = details;
    }

    public final GoogleJsonError getDetails() {
        return this.details;
    }

    @Deprecated
    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public static GoogleJsonResponseException from(JsonFactory jsonFactory, HttpResponse response) {
        Preconditions.checkNotNull(jsonFactory);
        GoogleJsonError details = null;
        String detailString = null;
        try {
            StringBuilder message;
            if (response.isSuccessStatusCode() || !HttpMediaType.equalsIgnoreParameters(Json.MEDIA_TYPE, response.getContentType()) || response.getContent() == null) {
                detailString = response.parseAsString();
                message = HttpResponseException.computeMessageBuffer(response);
                if (!Strings.isNullOrEmpty(detailString)) {
                    message.append(StringUtils.LINE_SEPARATOR).append(detailString);
                }
                return new GoogleJsonResponseException(jsonFactory, response, details, message.toString());
            }
            JsonParser parser = null;
            try {
                parser = jsonFactory.createJsonParser(response.getContent());
                JsonToken currentToken = parser.getCurrentToken();
                if (currentToken == null) {
                    currentToken = parser.nextToken();
                }
                if (currentToken != null) {
                    parser.skipToKey("error");
                    if (parser.getCurrentToken() != JsonToken.END_OBJECT) {
                        details = (GoogleJsonError) parser.parseAndClose(GoogleJsonError.class, null);
                        detailString = details.toPrettyString();
                    }
                }
                if (parser == null) {
                    response.ignore();
                    message = HttpResponseException.computeMessageBuffer(response);
                    if (Strings.isNullOrEmpty(detailString)) {
                        message.append(StringUtils.LINE_SEPARATOR).append(detailString);
                    }
                    return new GoogleJsonResponseException(jsonFactory, response, details, message.toString());
                }
                if (details == null) {
                    parser.close();
                }
                message = HttpResponseException.computeMessageBuffer(response);
                if (Strings.isNullOrEmpty(detailString)) {
                    message.append(StringUtils.LINE_SEPARATOR).append(detailString);
                }
                return new GoogleJsonResponseException(jsonFactory, response, details, message.toString());
            } catch (IOException exception) {
                exception.printStackTrace();
                if (parser == null) {
                    response.ignore();
                } else if (null == null) {
                    parser.close();
                }
            } catch (Throwable th) {
                if (parser == null) {
                    response.ignore();
                } else if (null == null) {
                    parser.close();
                }
            }
        } catch (IOException exception2) {
            exception2.printStackTrace();
        }
    }

    public static HttpResponse execute(JsonFactory jsonFactory, HttpRequest request) throws GoogleJsonResponseException, IOException {
        Preconditions.checkNotNull(jsonFactory);
        boolean originalThrowExceptionOnExecuteError = request.getThrowExceptionOnExecuteError();
        if (originalThrowExceptionOnExecuteError) {
            request.setThrowExceptionOnExecuteError(false);
        }
        HttpResponse response = request.execute();
        request.setThrowExceptionOnExecuteError(originalThrowExceptionOnExecuteError);
        if (!originalThrowExceptionOnExecuteError || response.isSuccessStatusCode()) {
            return response;
        }
        throw from(jsonFactory, response);
    }
}
