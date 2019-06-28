package com.google.api.client.auth.oauth2;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.StringUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.IOException;

public class TokenResponseException extends HttpResponseException {
    private static final long serialVersionUID = 4020689092957439244L;
    private final transient TokenErrorResponse details;

    private TokenResponseException(HttpResponse response, TokenErrorResponse details, String message) {
        super(response, message);
        this.details = details;
    }

    public final TokenErrorResponse getDetails() {
        return this.details;
    }

    public static TokenResponseException from(JsonFactory jsonFactory, HttpResponse response) {
        Preconditions.checkNotNull(jsonFactory);
        TokenErrorResponse details = null;
        String detailString = null;
        String contentType = response.getContentType();
        try {
            StringBuilder message;
            if (response.isSuccessStatusCode() || contentType == null || !HttpMediaType.equalsIgnoreParameters(Json.MEDIA_TYPE, contentType)) {
                detailString = response.parseAsString();
                message = HttpResponseException.computeMessageBuffer(response);
                if (!Strings.isNullOrEmpty(detailString)) {
                    message.append(StringUtils.LINE_SEPARATOR).append(detailString);
                }
                return new TokenResponseException(response, details, message.toString());
            }
            details = (TokenErrorResponse) new JsonObjectParser(jsonFactory).parseAndClose(response.getContent(), response.getContentCharset(), TokenErrorResponse.class);
            detailString = details.toPrettyString();
            message = HttpResponseException.computeMessageBuffer(response);
            if (Strings.isNullOrEmpty(detailString)) {
                message.append(StringUtils.LINE_SEPARATOR).append(detailString);
            }
            return new TokenResponseException(response, details, message.toString());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
